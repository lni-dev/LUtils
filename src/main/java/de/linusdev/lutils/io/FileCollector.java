/*
 * Copyright (c) 2024 Linus Andera
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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;

public class FileCollector implements FileVisitor<Path> {

    private final @NotNull BiPredicate<@NotNull Path, @NotNull BasicFileAttributes> collectPredicate;
    private final @NotNull List<@NotNull Path> collectedFiles = new ArrayList<>();

    public FileCollector(
            @NotNull BiPredicate<@NotNull Path, @NotNull BasicFileAttributes> collectPredicate
    ) {
        this.collectPredicate = collectPredicate;
    }

    public @NotNull List<@NotNull Path> getCollectedFiles() {
        return collectedFiles;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        if(collectPredicate.test(file, attrs))
            collectedFiles.add(file);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, @Nullable IOException exc) {
        return FileVisitResult.CONTINUE;
    }
}
