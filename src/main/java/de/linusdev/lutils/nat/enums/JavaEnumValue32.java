package de.linusdev.lutils.nat.enums;

public class JavaEnumValue32 <M extends NativeEnumMember32> implements EnumValue32<M> {

    public int value;

    public JavaEnumValue32() {
        this.value = DEFAULT_VALUE;
    }

    @Override
    public void set(int value) {
        this.value = value;
    }

    @Override
    public int get() {
        return value;
    }
}
