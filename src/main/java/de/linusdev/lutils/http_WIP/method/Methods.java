/*
 * Copyright (c) 2023 Linus Andera all rights reserved
 */

package de.linusdev.lutils.http_WIP.method;

import org.jetbrains.annotations.NotNull;

public enum Methods implements RequestMethod {
    GET("GET"),
    HEAD("HEAD"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE"),
    CONNECT("CONNECT"),
    OPTIONS("OPTIONS"),
    TRACE("TRACE"),
    ;

    private final String name;

    Methods(String name) {
        this.name = name;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }


    @Override
    public String toString() {
        return name;
    }
}
