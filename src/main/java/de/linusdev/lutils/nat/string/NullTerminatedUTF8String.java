package de.linusdev.lutils.nat.string;

import de.linusdev.lutils.nat.array.NativeInt8Array;
import de.linusdev.lutils.nat.struct.abstracts.StructureStaticVariables;
import de.linusdev.lutils.nat.struct.annos.SVWrapper;
import de.linusdev.lutils.nat.struct.annos.StructValue;
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
        return new NullTerminatedUTF8String(null, false);
    }

    /**
     * @see StructureStaticVariables#newAllocatable(StructValue)
     */
    public static NullTerminatedUTF8String newAllocatable(@NotNull StructValue structValue) {
        return new NullTerminatedUTF8String(structValue, true);
    }

    /**
     * @see StructureStaticVariables#newAllocated(StructValue)
     */
    public static NullTerminatedUTF8String newAllocated(@NotNull StructValue structValue) {
        NullTerminatedUTF8String ret = newAllocatable(structValue);
        ret.allocate();
        return ret;
    }

    public static NullTerminatedUTF8String ofString(@NotNull String string) {
        byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
        NullTerminatedUTF8String natString = NullTerminatedUTF8String.newAllocated(SVWrapper.length(bytes.length + 1));
        natString.set(string);
        return natString;
    }

    protected NullTerminatedUTF8String(
            @Nullable StructValue structValue,
            boolean generateInfo
    ) {
        super(structValue, generateInfo);
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
}
