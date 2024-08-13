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
