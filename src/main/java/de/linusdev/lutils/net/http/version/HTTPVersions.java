/*
 * Copyright (c) 2023-2024 Linus Andera
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.linusdev.lutils.net.http.version;

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
