package de.linusdev.lutils.result;

public record TriResult<T, U, V>(
        T result1,
        U result2,
        V result3
) implements Result {

    @Override
    public int count() {
        return 3;
    }

    @Override
    public Object get(int index) {
        if (index == 0) return result1;
        if (index == 1) return result2;
        if (index == 2) return result3;
        throw new IndexOutOfBoundsException(index);
    }
}
