/*
 * Copyright (c) 2023 Linus Andera all rights reserved
 */

package de.linusdev.lutils.http.body;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;

/**
 * Functional interface to parse an {@link InputStream} to custom body.
 * @param <B> body type
 */
@FunctionalInterface
public interface BodyParser<B> {

    /**
     * parses the body contained in given {@link InputStream} {@code in} to {@link B}.
     * @param in {@link InputStream} containing the http body.
     * @return parsed {@link B body}
     */
     @Nullable B parse(@NotNull InputStream in);

}
