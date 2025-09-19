/*
 * Copyright (c) 2023-2025 Linus Andera
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

package de.linusdev.lutils.nat.loader;

import de.linusdev.lutils.async.Future;
import de.linusdev.lutils.async.Nothing;
import de.linusdev.lutils.async.completeable.CompletableFuture;
import de.linusdev.lutils.async.error.ThrowableAsyncError;
import de.linusdev.lutils.async.manager.AsyncManager;
import de.linusdev.lutils.io.FileUtils;
import de.linusdev.lutils.io.ResourceUtils;
import de.linusdev.lutils.io.tmp.TmpDir;
import de.linusdev.lutils.os.OsArchitectureType;
import de.linusdev.lutils.os.OsType;
import de.linusdev.lutils.os.OsUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

/**
 * Helper class to load native libraries from resource files.
 * It will look for a library as passed to the constructor. If it cant find a library it will also look for libraries
 * with the same name but common prefixes or, if no file ending was passed, common file endings.
 * @see OsType#getDefaultNativeLibraryFileEnding()
 * @see OsType#getPossibleNativeLibraryFilePrefixes()
 * @see System#load(String) 
 */
@SuppressWarnings("unused")
public class ResourceNativeLibraryLoader {

    private static @Nullable Path TMP_DIR = null;
    private static final @NotNull AtomicInteger EXPORTED_COUNT = new AtomicInteger(0);

    /**
     * Export a native library from resources to a temporary directory.
     * @param caller only needed if a relative {@code libPath} is passed.
     * @param archTypePostFixer postfix to add for the {@link OsUtils#CURRENT_ARCH current architecture}. {@code null}
     *                          to add no arch type. Works even if file endings are passed in {@code libPaths}.
     * @param libPath Path to the resource file. See {@link ResourceNativeLibraryLoader} for more information.
     * @return A {@link Future} which will be completed once the library has been exported.
     */
    public static @NotNull Future<ExportedNativeLib, Nothing> export(
            @Nullable Class<?> caller,
            @Nullable Function<OsArchitectureType, String> archTypePostFixer,
            @NotNull String libPath
    ) {
        var fut = CompletableFuture.<ExportedNativeLib, Nothing>create(AsyncManager.DEFAULT, false);

        new Thread(() -> {
            String postfix = archTypePostFixer == null ? "" : archTypePostFixer.apply(OsUtils.CURRENT_ARCH);
            var resource = findLib(caller, libPath, postfix);

            try {
                Path file = getTmpDir().resolve(EXPORTED_COUNT.getAndIncrement() + "-" + FileUtils.getFileName(resource.getPath()));
                Files.copy(resource.openInputStream(), file);
                fut.complete(new ExportedNativeLib(file), Nothing.INSTANCE, null);
            } catch (IOException e) {
                fut.complete(null, Nothing.INSTANCE, new ThrowableAsyncError(e));
            }
        }).start();

        return fut;
    }

    private static @NotNull ResourceUtils.StreamURLConnection findLib(@Nullable Class<?> caller, @NotNull String libPath, @NotNull String postfix) {
        ArrayList<String> triedPaths = new ArrayList<>();

        String lib = FileUtils.getFileName(libPath);

        String tEnding;
        if((tEnding = FileUtils.getFileEnding(libPath)) != null) {
            lib = lib.substring(0, lib.length() - tEnding.length()) + postfix + tEnding;
        } else {
            lib = lib + postfix;
        }
        String path = FileUtils.getParentDirectory(libPath);

        String tried = path + lib;
        ResourceUtils.StreamURLConnection res = ResourceUtils.getURLConnectionOfResource(caller, tried, true);

        if(res != null)
            return res;
        triedPaths.add(tried);

        if(FileUtils.getFileEnding(lib) == null) {
            for (String ending : OsUtils.CURRENT_OS.getDefaultNativeLibraryFileEnding()) {

                tried = path + lib + "." + ending;
                res = ResourceUtils.getURLConnectionOfResource(caller, tried, true);

                if(res != null)
                    return res;
                triedPaths.add(tried);

                for (String prefix : OsUtils.CURRENT_OS.getPossibleNativeLibraryFilePrefixes()) {
                    tried = path + prefix + lib + "." + ending;
                    res = ResourceUtils.getURLConnectionOfResource(caller, tried, true);

                    if(res != null)
                        return res;
                    triedPaths.add(tried);
                }

            }
        } else {
            for (String prefix : OsUtils.CURRENT_OS.getPossibleNativeLibraryFilePrefixes()) {
                tried = path + prefix + lib;
                res = ResourceUtils.getURLConnectionOfResource(caller, tried, true);

                if(res != null)
                    return res;
                triedPaths.add(tried);
            }
        }

        StringBuilder sb = new StringBuilder();
        for (String triedPath : triedPaths)
            sb.append("\n - ").append(triedPath);
        throw new Error("Could not find the resource '" + libPath + "'. Tried the following locations:" + sb);
    }

    private static @NotNull Path getTmpDir() throws IOException {
        if(TMP_DIR == null) {
            TmpDir tmpDir = FileUtils.getTemporaryDirectory("native-lib-loader", 1, TimeUnit.DAYS);
            TMP_DIR = tmpDir.getPath();
            tmpDir.deleteOnExit();
        }

        return TMP_DIR;
    }

    @SuppressWarnings("ClassCanBeRecord")
    public static class ExportedNativeLib {
        private final @NotNull Path libFile;

        public ExportedNativeLib(@NotNull Path libFile) {
            this.libFile = libFile.toAbsolutePath();
        }

        /**
         * Get the path to the exported library. Useful if you want to load the library yourself.
         */
        public @NotNull Path getLibFilePath() {
            return libFile;
        }

        /**
         * {@link System#load(String) Load} the library.
         */
        public void load() {
            System.load(libFile.toString());
        }
    }
}
