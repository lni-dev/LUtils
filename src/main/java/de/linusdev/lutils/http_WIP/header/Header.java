/*
 * Copyright (c) 2023 Linus Andera all rights reserved
 */

package de.linusdev.lutils.http_WIP.header;

import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public interface Header {

    /**
     * Create new {@link Header}.
     * @param header header string with the following format: "key: value"
     * @return new {@link Header}.
     */
    static @NotNull Header of(@NotNull String header) {
        int sep = header.indexOf(':');
        return new HeaderImpl(header.substring(0, sep), header.substring(sep + 2));
    }

    /**
     * Create new {@link Header}.
     * @param key header key
     * @param value header value
     * @return new {@link Header}
     */
    static @NotNull Header of(@NotNull String key, @NotNull String value) {
        return new HeaderImpl(key, value);
    }

    /**
     * Key of this {@link Header}.
     * @return key as {@link String}
     */
    @NotNull String getKey();

    /**
     * Value of this {@link Header}.
     * @return value as {@link String}
     */
    @NotNull String getValue();

    /**
     * This header as {@link String}.
     * @return this header
     */
    default @NotNull String asString() {
        return getKey() + ": " + getValue();
    }
}
