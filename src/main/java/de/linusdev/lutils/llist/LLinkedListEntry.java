/*
 * Copyright (c) 2022-2023 Linus Andera all rights reserved
 */

package de.linusdev.lutils.llist;

import org.jetbrains.annotations.Nullable;

public class LLinkedListEntry<O> {

    private volatile @Nullable O value;
    private volatile @Nullable LLinkedListEntry<O> next;

    public LLinkedListEntry(@Nullable O value) {
        this.value = value;
    }

    public @Nullable LLinkedListEntry<O> getNext() {
        return next;
    }

    public synchronized void setNext(@Nullable LLinkedListEntry<O> next) {
        this.next = next;
    }

    public @Nullable O getValue() {
        return value;
    }

    public synchronized void setValue(@Nullable O value) {
        this.value = value;
    }
}
