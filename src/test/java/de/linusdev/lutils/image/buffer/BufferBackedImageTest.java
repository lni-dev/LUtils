package de.linusdev.lutils.image.buffer;

import de.linusdev.lutils.image.Image;
import de.linusdev.lutils.image.png.reader.PNGReader;
import de.linusdev.lutils.nat.struct.abstracts.Structure;
import de.linusdev.lutils.nat.struct.annos.SVWrapper;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BufferBackedImageTest {

    @Test
    void test() throws IOException {

        @NotNull Image read = PNGReader.readFromResource("de/linusdev/lutils/image/buffer/grass_side-v1.png");

        System.out.println("Original:");
        System.out.println(Image.printable(read));

        @NotNull BufferBackedImage bufferBackedImage = Structure.allocate(
                new BufferBackedImage(SVWrapper.imageSize(read.getWidth(), read.getHeight()), true)
        );

        assertEquals(read.getHeight(), bufferBackedImage.getHeight());
        assertEquals(read.getWidth(), bufferBackedImage.getWidth());
        assertEquals(4, bufferBackedImage.getAlignment());
        assertEquals(read.getWidth() * read.getHeight() * 4, bufferBackedImage.getRequiredSize());

        Image.copy(read, bufferBackedImage);

        assertTrue(Image.equalsRGBA(read, bufferBackedImage));

        System.out.println("Copied: ");
        System.out.println(Image.printable(bufferBackedImage));

    }
}