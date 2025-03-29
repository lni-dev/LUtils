/*
 * Copyright (c) 2025 Linus Andera
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

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

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