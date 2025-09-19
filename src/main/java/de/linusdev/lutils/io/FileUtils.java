/*
 * Copyright (c) 2024-2025 Linus Andera
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.linusdev.lutils.io;

import de.linusdev.lutils.io.tmp.TmpDir;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.BiPredicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileUtils {

    /**
     * The temporary directory of the OS.
     */
    public static @NotNull Path getTemporaryDirectory() {
        return Paths.get(System.getProperty("java.io.tmpdir"));
    }

    private static boolean tmpDirsChecked = false;

    public static synchronized @NotNull TmpDir getTemporaryDirectory(
            @NotNull String id,
            long deleteAfter,
            @NotNull TimeUnit deleteAfterTimeUnit
    ) throws IOException {
        Path lutilsDir = getTemporaryDirectory().resolve("de.linusdev.lutils");

        if(!Files.exists(lutilsDir))
            Files.createDirectory(lutilsDir);

        if(!Files.isDirectory(lutilsDir))
            throw new Error("Lutils dir '" + lutilsDir + "' cannot be created.");

        if(!tmpDirsChecked) {
            tmpDirsChecked = true;

            // Start checking which old tmp dirs can be deleted.
            try(var dirs = Files.list(lutilsDir)) {
                dirs.forEach(path -> {
                    if(!Files.isDirectory(path))
                        return;

                    TmpDir tmpDir;
                    try {
                        tmpDir = new TmpDir(path);
                    } catch (IOException e) {
                        // This is not a valid tmp dir, delete it
                        try {
                            FileUtils.deleteDirectoryRecursively(path);
                        } catch (IOException ignored) {
                            // Cannot be deleted -> Ignore
                        }

                        return;
                    }

                    try {
                        tmpDir.deleteIfItShouldBeDeleted();
                    } catch (IOException e) {
                        // Cannot be deleted -> Ignore
                    }
                });
            }
        }

        Path tmpDir = lutilsDir.resolve(id + "_" + UUID.randomUUID());
        Files.createDirectory(tmpDir);
        return new TmpDir(tmpDir, deleteAfterTimeUnit.toMillis(deleteAfter));
    }

    /**
     * Copies a file or directory.<br>
     * If {@code source} is a file, {@code target} must not exist.<br>
     * If {@code source} is a directory, {@code target} must not exist or be an empty directory.<br>
     * @param source source file or directory
     * @param target target location to copy to
     * @throws IOException while copying or if the above restrictions are not met
     */
    public static void copyDirectoryRecursively(
            @NotNull Path source,
            @NotNull Path target
    ) throws IOException {

        if(Files.isRegularFile(source)) {
            if(Files.exists(target)) {
                throw new FileAlreadyExistsException("File '" + target + "' already exists.");
            }
            Files.copy(source, target);
            return;

        } else if (Files.isDirectory(target)) {
            try (var stream = Files.list(target)) {
                if(stream.findAny().isPresent()) {
                    throw new FileAlreadyExistsException("Directory '" + target + "' is not empty.");
                }
            }

        }

        if(!Files.exists(target)) {
            Files.createDirectory(target);
        }

        Files.walkFileTree(source,
                new SimpleFileVisitor<>() {
                    @Override
                    public @NotNull FileVisitResult preVisitDirectory(@NotNull Path dir, @NotNull BasicFileAttributes attrs) throws IOException {
                        var relPath = source.relativize(dir);
                        if(!relPath.toString().isEmpty())
                            Files.copy(dir, target.resolve(relPath), StandardCopyOption.COPY_ATTRIBUTES);
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public @NotNull FileVisitResult visitFile(@NotNull Path file, @NotNull BasicFileAttributes attrs) throws IOException {
                        Files.copy(file, target.resolve(source.relativize(file)), StandardCopyOption.COPY_ATTRIBUTES);
                        return FileVisitResult.CONTINUE;
                    }
                }
        );
    }

    /**
     * Deletes given file or directory. If it is a directory, it will be deleted recursively.
     * @param dir file or directory to delete
     * @throws IOException while deleting
     */
    public static void deleteDirectoryRecursively(
            @NotNull Path dir
    ) throws IOException {
        if(!Files.isDirectory(dir))
            Files.delete(dir);

        Files.walkFileTree(dir, new FileVisitor<>() {
            @Override
            public @NotNull FileVisitResult preVisitDirectory(Path dir, @NotNull BasicFileAttributes attrs) {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public @NotNull FileVisitResult visitFile(Path file, @NotNull BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public @NotNull FileVisitResult visitFileFailed(Path file, @NotNull IOException exc) {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public @NotNull FileVisitResult postVisitDirectory(Path dir, @Nullable IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    public static @NotNull List<@NotNull Path> collectInFileTree(
            @NotNull Path start,
            @NotNull BiPredicate<@NotNull Path, @NotNull BasicFileAttributes> predicate
    ) throws IOException {
        FileCollector collector = new FileCollector(predicate);

        Files.walkFileTree(start,
                EnumSet.of(FileVisitOption.FOLLOW_LINKS),
                Integer.MAX_VALUE,
                collector
        );

        return collector.getCollectedFiles();
    }

    private static final @NotNull Pattern FILE_ENDING_PATTERN = Pattern.compile(".*\\.(?<end>[^./\\\\]+)$");

    /**
     * Get the file ending of given {@code file}.
     * @param file the file
     * @return the file ending or {@code null} if this file has no file ending.
     */
    public static @Nullable String getFileEnding(@NotNull Path file) {
        return getFileEnding(file.toString());
    }

    /**
     * Get the file ending of given {@code file}.
     * @param filePath the path to the file as string
     * @return the file ending or {@code null} if this file has no file ending.
     */
    public static @Nullable String getFileEnding(@NotNull String filePath) {
        Matcher matcher = FILE_ENDING_PATTERN.matcher(filePath);

        if(!matcher.find())
            return null;

        return matcher.group("end");
    }

    /**
     * Get the file name including the file ending of given {@code filePath}
     * @param filePath the path to the file
     * @return file name as described above.
     */
    public static @NotNull String getFileName(@NotNull String filePath) {
        int index = filePath.lastIndexOf('/');
        index = Math.max(index, filePath.lastIndexOf('\\'));

        if(index == -1)
            return filePath;

        return filePath.substring(index + 1);
    }

    /**
     * Get the path of the parent directory of given file. Ending in {@code /} or {@code \}.
     * If {@code filePath} does not contain {@code /} or {@code \}, an empty string will be returned.
     * @param filePath path to the file
     * @return path of the parent directory or an empty string as described above.
     */
    public static @NotNull String getParentDirectory(@NotNull String filePath) {
        int index = filePath.lastIndexOf('/');
        index = Math.max(index, filePath.lastIndexOf('\\'));

        if(index == -1)
            return "";

        return filePath.substring(0, index + 1);
    }

}
