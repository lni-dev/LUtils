package de.linusdev.lutils.nat.pointer;

class Pointer64Impl implements Pointer64 {
    private long pointer = 0L;

    public Pointer64Impl() {}

    public Pointer64Impl(long pointer) {
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