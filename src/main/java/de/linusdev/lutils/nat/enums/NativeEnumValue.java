package de.linusdev.lutils.nat.enums;

import de.linusdev.lutils.math.vector.buffer.intn.BBInt1;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class NativeEnumValue<M extends NativeEnumMember> extends BBInt1 {

    public void set(@Nullable M value) {
        if(value == null) set(0);
        else set(value.getValue());
    }

}
