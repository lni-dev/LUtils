package de.linusdev.lutils.optional.impl;

import de.linusdev.lutils.optional.ListContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BasicListContainer<V> extends ListContainer<V> {
    public BasicListContainer(@Nullable Object key, boolean exists, @Nullable List<V> list) {
        super(key, exists, list);
    }

    @Override
    protected @NotNull <N> ListContainer<N> createNew(@Nullable List<N> newValue) {
        return new BasicListContainer<>(key, exists, newValue);
    }
}
