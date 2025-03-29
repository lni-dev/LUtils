/*
 * Copyright (c) 2024-2025 Linus Andera
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

package de.linusdev.lutils.nat.string;

import de.linusdev.lutils.nat.array.NativeInt16Array;
import de.linusdev.lutils.nat.struct.abstracts.StructureStaticVariables;
import de.linusdev.lutils.nat.struct.annos.StructValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.StandardCharsets;

/**
 * A Structure that can contain a null (0-byte) terminated utf-8 string.
 * @see #get()
 * @see #set(String)
 */
public class NullTerminatedUTF16String extends NativeInt16Array {

    /**
     * @see StructureStaticVariables#newUnallocated()
     */
    public static NullTerminatedUTF16String newUnallocated() {
        return new NullTerminatedUTF16String(null, false);
    }

    /**
     * @see StructureStaticVariables#newAllocatable(StructValue)
     */
    public static NullTerminatedUTF16String newAllocatable(@NotNull StructValue structValue) {
        return new NullTerminatedUTF16String(structValue, true);
    }

    /**
     * @see StructureStaticVariables#newAllocated(StructValue)
     */
    public static NullTerminatedUTF16String newAllocated(@NotNull StructValue structValue) {
        NullTerminatedUTF16String ret = newAllocatable(structValue);
        ret.allocate();
        return ret;
    }

    protected NullTerminatedUTF16String(
            @Nullable StructValue structValue,
            boolean generateInfo
    ) {
        super(structValue, generateInfo);
    }

    /**
     * Set content of this structure to given {@code value}. A 0-byte will be added
     * after {@code value} has been added in utf-8 format.
     * @throws java.nio.BufferOverflowException if given {@code value} (and two 0-byte) does not fit into this structure
     * @param value {@link String} value
     */
    public void set(@NotNull String value) {
        byteBuf.clear();

        byteBuf.put(value.getBytes(StandardCharsets.UTF_16));
        byteBuf.put((byte) 0);
        byteBuf.put((byte) 0);

        byteBuf.clear();
    }

    /**
     * Get the content of this structure as {@link String}.
     * Reads until the first 0-byte.
     */
    public @NotNull String get() {
        byteBuf.clear();

        byte[] bytes = new byte[length()];
        byteBuf.get(bytes);

        int index = 0;
        for(int i = 0; i < bytes.length; i++) {
            // For UTF-16 two bytes must be zero
            if(bytes[i] == 0 && bytes[i+1] == 0) {
                index = i;
                break;
            }
        }

        return new String(bytes, 0, index, StandardCharsets.UTF_16);
    }

    @Override
    public String toString() {
        return toString("utf-16-string", get());
    }
}
