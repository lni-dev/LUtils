package de.linusdev.lutils.result;

public record SingleResult<T>(
        T result
) implements Result {

    @Override
    public int count() {
        return 1;
    }

    @Override
    public Object get(int index) {
        if (index != 0)
            throw new IndexOutOfBoundsException(index);
        return result;
    }
}
