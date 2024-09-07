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
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumSet;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileUtils {

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

    private static final @NotNull Pattern FILE_ENDING_PATTERN = Pattern.compile(".*\\.(?<end>[^.]+)$");
    public static @Nullable String getFileEnding(@NotNull Path file) {
        String name = file.getFileName().toString();

        Matcher matcher = FILE_ENDING_PATTERN.matcher(name);

        if(!matcher.find())
            return null;

        return matcher.group("end");
    }

}
