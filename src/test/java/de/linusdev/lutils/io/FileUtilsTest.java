package de.linusdev.lutils.io;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class FileUtilsTest {

    @Test
    void collectInFileTree() throws IOException {
        List<Path> paths = FileUtils.collectInFileTree(
                Paths.get("./src/test/resources/de/linusdev/lutils/io/FileUtilsTest"),
                (path, basicFileAttributes) -> path.getFileName().toString().endsWith(".a")
        );

        var names = paths.stream().map(path -> path.getFileName().toString()).sorted().toArray();

        assertArrayEquals(Stream.of( "a.a", "b.a", "d.a", "e.a").sorted().toArray(), names);
    }

    @Test
    void getFileEnding() {
        Path testPath = Paths.get("dir/someFileName.notEnding.txt");

        assertEquals("txt", FileUtils.getFileEnding(testPath));
    }
}