package de.linusdev.lutils.optional.impl;

import de.linusdev.lutils.optional.Container;
import de.linusdev.lutils.optional.ListContainer;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BasicContainer<V> extends Container<V> {
    public BasicContainer(@Nullable Object key, boolean exists, @Nullable V value) {
        super(key, exists, value);
    }

    @Override
    protected <N> Container<N> createNewContainer(@Nullable N value) {
        return new BasicContainer<>(key, exists, value);
    }

    @Override
    protected <N> ListContainer<N> createNewListContainer(@Nullable List<N> list) {
        return new BasicListContainer<>(key, exists, list);
    }
}
