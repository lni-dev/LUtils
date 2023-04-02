/*
 * Copyright (c) 2023 Linus Andera all rights reserved
 */

package de.linusdev.lutils.http_WIP.version;

import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public interface HTTPVersion {

    /**
     * The first part of the version. For example in "HTTP/1.1" it would be "HTTP"
     * @return name as {@link String}
     */
    @NotNull String getName();

    /**
     * The first part of the version. For example in "HTTP/1.1" it would be "1.1"
     * @return version as {@link String}
     */
    @NotNull String getVersion();

    /**
     * This {@link HTTPVersion} as {@link String}. For example "HTTP/1.1"
     * @return this {@link HTTPVersion} as {@link String}.
     */
    default @NotNull String asString() {
        return getName() + "/" + getVersion();
    }

}
