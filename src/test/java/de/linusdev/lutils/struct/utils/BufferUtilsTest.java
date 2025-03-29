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

package de.linusdev.lutils.struct.utils;

import de.linusdev.lutils.nat.struct.utils.BufferUtils;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

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