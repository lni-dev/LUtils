package de.linusdev.lutils.nat.pointer;

import de.linusdev.lutils.nat.NativeParsable;

public class TypedPointer64Impl<T extends NativeParsable> implements TypedPointer64<T> {
    private long pointer = 0L;

    public TypedPointer64Impl() {}

    public TypedPointer64Impl(long pointer) {
        this.pointer = pointer;
    }

    @Override
    public long get() {
        return pointer;
    }

    @Override
    public void set(long pointer) {
        this.pointer = pointer;
    }
}
