/*
 * Copyright (c) 2024-2026 Linus Andera
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

import de.linusdev.lutils.nat.abi.ABI;
import de.linusdev.lutils.nat.array.NativeInt8Array;
import de.linusdev.lutils.nat.memory.IndexOutOfStructRange;
import de.linusdev.lutils.nat.struct.abstracts.Structure;
import de.linusdev.lutils.nat.struct.abstracts.StructureStaticVariables;
import de.linusdev.lutils.nat.struct.info.StructureInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.StandardCharsets;

/**
 * A Structure that can contain a null (0-byte) terminated utf-8 string.
 * <br><br>
 * Note: this class only works for {@link ABI abis} where strings are in memory continuously without padding (which is usually the case).
 * However, it is *not* checked during string creation if a given {@link ABI} supports this!
 * @see #get()
 * @see #set(String)
 */
public class NullTerminatedUTF8String extends NativeInt8Array {

    /**
     * @see StructureStaticVariables#newUnallocated()
     */
    public static NullTerminatedUTF8String newUnallocated() {
        return new NullTerminatedUTF8String();
    }

    /**
     * @see StructureStaticVariables#newAllocatable(ABI, int[], Class[]) 
     */
    public static NullTerminatedUTF8String newAllocatable(@Nullable ABI abi, int length) {
        return new NullTerminatedUTF8String(abi, length);
    }

    /**
     * @see StructureStaticVariables#newAllocatable(ABI, int[], Class[])
     */
    public static NullTerminatedUTF8String newAllocatable(@Nullable ABI abi, @NotNull String value) {
        return new NullTerminatedUTF8String(abi, value);
    }

    byte @Nullable [] defaultValue;

    protected NullTerminatedUTF8String(@Nullable ABI abi, int length) {
        super(abi, length);
    }

    protected NullTerminatedUTF8String(@Nullable ABI abi, @NotNull String value) {
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        super(abi, bytes.length + 1);
        this.defaultValue = bytes;
    }

    protected NullTerminatedUTF8String() {
        super();
    }

    @Override
    protected void useBuffer(@NotNull Structure mostParentStructure, long offset, @NotNull StructureInfo info) {
        super.useBuffer(mostParentStructure, offset, info);
        if(defaultValue != null) {
            set(defaultValue);
            setInt8(defaultValue.length, (byte)0);
        }
    }

    /**
     * Set content of this structure to given {@code value}. A 0-byte will be added
     * after {@code value} has been added in utf-8 format.
     * @throws java.nio.BufferOverflowException if given {@code value} (and a 0-byte) does not fit into this structure
     * @param value {@link String} value
     */
    public void set(@NotNull String value) {
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);

        if(bytes.length + 1 > length)
            throw new IndexOutOfStructRange(bytes.length, this);

        set(bytes);
        setInt8(bytes.length, (byte) 0);
    }

    /**
     * Get the content of this structure as {@link String}.
     * Reads until the first 0-byte.
     */
    public @NotNull String get() {
        byte[] bytes = new byte[length()];
        get(bytes);

        int index = 0;
        for(int i = 0; i < bytes.length; i++) {
            if(bytes[i] == 0) {
                index = i;
                break;
            }
        }

        return new String(bytes, 0, index, StandardCharsets.UTF_8);
    }

    @Override
    public String toString() {
        return toString("utf-8-string", get());
    }
}
