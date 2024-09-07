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

package de.linusdev.lutils.nat.string;

import de.linusdev.lutils.nat.array.NativeInt8Array;
import de.linusdev.lutils.nat.struct.abstracts.Structure;
import de.linusdev.lutils.nat.struct.abstracts.StructureStaticVariables;
import de.linusdev.lutils.nat.struct.annos.SVWrapper;
import de.linusdev.lutils.nat.struct.annos.StructValue;
import de.linusdev.lutils.nat.struct.info.StructureInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.StandardCharsets;

/**
 * A Structure that can contain a null (0-byte) terminated utf-8 string.
 * @see #get()
 * @see #set(String)
 */
public class NullTerminatedUTF8String extends NativeInt8Array {

    /**
     * @see StructureStaticVariables#newUnallocated()
     */
    public static NullTerminatedUTF8String newUnallocated() {
        return new NullTerminatedUTF8String(null, false, null);
    }

    /**
     * @see StructureStaticVariables#newAllocatable(StructValue)
     */
    public static NullTerminatedUTF8String newAllocatable(@NotNull StructValue structValue) {
        return new NullTerminatedUTF8String(structValue, true, null);
    }

    /**
     * @see StructureStaticVariables#newAllocated(StructValue)
     */
    public static NullTerminatedUTF8String newAllocated(@NotNull StructValue structValue) {
        NullTerminatedUTF8String ret = newAllocatable(structValue);
        ret.allocate();
        return ret;
    }

    public static NullTerminatedUTF8String newAllocatable(@NotNull String defaultValue) {
        byte[] bytes = defaultValue.getBytes(StandardCharsets.UTF_8);
        return new NullTerminatedUTF8String(SVWrapper.length(bytes.length + 1), true, bytes);
    }

    public static NullTerminatedUTF8String newAllocated(@NotNull String string) {
        NullTerminatedUTF8String natString = newAllocatable(string);
        natString.allocate();
        return natString;
    }

    protected final byte @Nullable [] defaultValue;

    protected NullTerminatedUTF8String(
            @Nullable StructValue structValue,
            boolean generateInfo,
            byte @Nullable [] defaultValue
    ) {
        super(structValue, generateInfo);
        this.defaultValue = defaultValue;
    }

    @Override
    protected void useBuffer(@NotNull Structure mostParentStructure, int offset, @NotNull StructureInfo info) {
        super.useBuffer(mostParentStructure, offset, info);
        if(defaultValue != null)
            set(defaultValue);
    }

    /**
     * Set content of this structure to given {@code value}. A 0-byte will be added
     * after {@code value} has been added in utf-8 format.
     * @throws java.nio.BufferOverflowException if given {@code value} (and a 0-byte) does not fit into this structure
     * @param value {@link String} value
     */
    public void set(@NotNull String value) {
        byteBuf.clear();

        byteBuf.put(value.getBytes(StandardCharsets.UTF_8));
        byteBuf.put((byte) 0);

        byteBuf.clear();
    }

    /**
     * Set content of this structure to given {@code value}. A 0-byte will always be added
     * @param value string as byte[]
     */
    @Override
    public void set(byte @NotNull [] value) {
        super.set(value);
        byteBuf.put(value.length, (byte) 0);
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
