/*
 * Copyright (c) 2023 Linus Andera all rights reserved
 */

package de.linusdev.lutils.http_WIP.body;

import org.jetbrains.annotations.NotNull;

import java.io.InputStream;

public interface BodySupplier {

    /**
     * the length of the body or -1 if the length is unknown.
     * @return length of this body or -1
     */
    int length();

    /**
     * {@link InputStream} containing the body. The returned stream must be closed by the method caller.
     * @return {@link InputStream}
     */
    @NotNull InputStream stream();

}
