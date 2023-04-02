/*
 * Copyright (c) 2023 Linus Andera all rights reserved
 */

package de.linusdev.lutils.http_WIP.header;

import org.jetbrains.annotations.NotNull;

public class HeaderImpl implements Header {

    private final @NotNull String key;
    private final @NotNull String value;

    public HeaderImpl(@NotNull String key, @NotNull String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public @NotNull String getKey() {
        return key;
    }

    @Override
    public @NotNull String getValue() {
        return value;
    }
}
