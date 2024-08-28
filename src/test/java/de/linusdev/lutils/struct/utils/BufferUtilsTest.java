package de.linusdev.lutils.struct.utils;

import de.linusdev.lutils.nat.struct.utils.BufferUtils;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class BufferUtilsTest {

    @Test
    void createAligned() {

        ByteBuffer buffer;
        int alignment;

        alignment = 8;
        buffer = BufferUtils.createAligned(100, alignment);

        assertEquals(100, buffer.capacity());
        assertEquals(0, BufferUtils.getHeapAddress(buffer) % alignment);

        alignment = 4;
        buffer = BufferUtils.createAligned(100, alignment);

        assertEquals(100, buffer.capacity());
        assertEquals(0, BufferUtils.getHeapAddress(buffer) % alignment);

        alignment = 2;
        buffer = BufferUtils.createAligned(100, alignment);

        assertEquals(100, buffer.capacity());
        assertEquals(0, BufferUtils.getHeapAddress(buffer) % alignment);

        alignment = 16;
        buffer = BufferUtils.createAligned(100, alignment);

        assertEquals(100, buffer.capacity());
        assertEquals(0, BufferUtils.getHeapAddress(buffer) % alignment);

    }

    @Test
    void getHeapAddress() {
        ByteBuffer buffer = BufferUtils.createAligned(100, 8);

        assertEquals(0, BufferUtils.getHeapAddress(buffer) % 8);
    }

    @Test
    void readString() {
        byte[] bs = "hallo".getBytes(StandardCharsets.UTF_8);
        ByteBuffer buffer = BufferUtils.createAligned(bs.length, 8);

        for(byte b : bs)
            buffer.put(b);

        buffer.position(0);
        assertEquals("hallo", BufferUtils.readString(buffer, false));

    }
}