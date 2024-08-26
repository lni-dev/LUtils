package de.linusdev.lutils.result;

public record BiResult<T, U>(
        T result1,
        U result2
) implements Result {

    @Override
    public int count() {
        return 2;
    }

    @Override
    public Object get(int index) {
        if (index == 0) return result1;
        if (index == 1) return result2;
        throw new IndexOutOfBoundsException(index);
    }
}
