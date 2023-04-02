/*
 * Copyright (c) 2023 Linus Andera all rights reserved
 */

package de.linusdev.lutils.http_WIP.method;

import org.jetbrains.annotations.NotNull;

/**
 * HTTP Request Method
 */
public interface RequestMethod {

    static @NotNull RequestMethod of(@NotNull String method) {
        try {
            return  Methods.valueOf(method);
        } catch (IllegalArgumentException e) {
            return () -> method;
        }
    }


    /**
     * Checks if the given {@link RequestMethod methods} are the same.
     * @param first {@link RequestMethod}
     * @param second {@link RequestMethod}
     * @return {@code true} if {@link #getName()} of both {@link RequestMethod methods} are the same
     */
    @SuppressWarnings("unused")
    static boolean equals(@NotNull RequestMethod first, @NotNull RequestMethod second) {
        return first == second || first.getName().equals(second.getName());
    }

    /**
     * Name of this {@link RequestMethod}. for example: "GET"
     * @return name as {@link String}
     */
    @NotNull String getName();

}
