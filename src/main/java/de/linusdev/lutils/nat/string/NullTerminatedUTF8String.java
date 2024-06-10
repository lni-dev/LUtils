package de.linusdev.lutils.nat.string;

import de.linusdev.lutils.nat.array.NativeInt8Array;
import de.linusdev.lutils.nat.struct.annos.StructValue;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;

//TODO: Test + Doc
public class NullTerminatedUTF8String extends NativeInt8Array {

    public NullTerminatedUTF8String(@NotNull StructValue structValue) {
        super(structValue);
    }

    public NullTerminatedUTF8String() {
    }

    public void set(@NotNull String value) {
        byteBuf.clear();

        byteBuf.put(value.getBytes(StandardCharsets.UTF_8));
        byteBuf.put((byte) 0);

        byteBuf.clear();
    }

    public @NotNull String get() {
        byteBuf.clear();

        byte[] bytes = new byte[length()];
        byteBuf.get(bytes);

        int index = 0;
        for(int i = 0; i < bytes.length; i++) {
            if(bytes[i] == 0) {
                index = i;
            }
        }

        return new String(bytes, 0, index);
    }
}
