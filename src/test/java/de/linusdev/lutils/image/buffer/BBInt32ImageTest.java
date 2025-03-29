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

package de.linusdev.lutils.image.buffer;

import de.linusdev.lutils.image.Image;
import de.linusdev.lutils.image.PixelFormat;
import de.linusdev.lutils.image.png.reader.PNGReader;
import de.linusdev.lutils.nat.struct.abstracts.Structure;
import de.linusdev.lutils.nat.struct.annos.SVWrapper;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BBInt32ImageTest {

    @Test
    void test() throws IOException {

        @NotNull Image read = PNGReader.readFromResource("de/linusdev/lutils/image/buffer/grass_side-v1.png");

        System.out.println("Original:");
        System.out.println(Image.printable(read));

        @NotNull BBInt32Image bufferBackedImage = Structure.allocate(
                new BBInt32Image(SVWrapper.imageSize(read.getWidth(), read.getHeight()), true, PixelFormat.A8R8G8B8_SRGB)
        );

        assertEquals(read.getHeight(), bufferBackedImage.getHeight());
        assertEquals(read.getWidth(), bufferBackedImage.getWidth());
        assertEquals(4, bufferBackedImage.getAlignment());
        assertEquals(read.getWidth() * read.getHeight() * 4, bufferBackedImage.getRequiredSize());

        Image.copy(read, bufferBackedImage);

        System.out.println("Copied: ");
        System.out.println(Image.printable(bufferBackedImage));

        assertTrue(Image.equals(read, bufferBackedImage));

    }
}