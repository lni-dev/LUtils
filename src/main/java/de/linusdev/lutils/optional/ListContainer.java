package de.linusdev.lutils.optional;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ListContainer<V> implements OptionalValue<List<V>> {
    private final @Nullable List<V> list;
    private final boolean exists;

    public ListContainer(@Nullable List<V> list, boolean exists) {
        this.list = list;
        this.exists = exists;
    }

    @Override
    public List<V> get() {
        return list;
    }

    @Override
    public boolean exists() {
        return exists;
    }
}
