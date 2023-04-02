/*
 * Copyright (c) 2023 Linus Andera all rights reserved
 */

package de.linusdev.lutils.http_WIP.version;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public enum HTTPVersions implements HTTPVersion {

    HTTP_0_9("0.9"),
    HTTP_1_0("1.0", "1"),
    HTTP_1_1("1.1"),
    HTTP_2_0("2.0", "2"),
    HTTP_3_0("3.0", "3"),

    ;

    private final static Map<String, HTTPVersions> versions = new HashMap<>(HTTPVersions.values().length);

    static {
        for(HTTPVersions v : HTTPVersions.values()) {
            for(String version : v.version) {
                versions.put(v.getName() + "/" + version, v);
            }
        }
    }

    public static HTTPVersion of(@NotNull String version) {
        return versions.get(version);
    }

    private final @NotNull String @NotNull [] version;

    HTTPVersions(@NotNull String @NotNull ... version) {
        this.version = version;
    }

    @Override
    public @NotNull String getName() {
        return "HTTP";
    }

    @Override
    public @NotNull String getVersion() {
        return version[0];
    }
}
