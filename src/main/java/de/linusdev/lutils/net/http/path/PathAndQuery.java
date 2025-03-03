/*
 * Copyright (c) 2025 Linus Andera
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

package de.linusdev.lutils.net.http.path;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Http path. Does not support escape character.
 */
public class PathAndQuery {

    private final @NotNull String path;
    private final @NotNull Map<String, String> parameters;

    public PathAndQuery(@NotNull String path) {
        int sepIndex = path.indexOf('?');

        if(sepIndex == -1) {
            this.path = path;
            this.parameters = Map.of();
        } else {
            this.path = path.substring(0, sepIndex);
            this.parameters = new HashMap<>();

            String parameterString = path.substring(sepIndex + 1);
            String[] params = parameterString.split("&");

            for (String param : params) {
                String[] pair = param.split("=");
                parameters.put(pair[0], pair[1]);
            }
        }
    }

    public @NotNull String getPath() {
        return path;
    }

    public @NotNull Map<String, String> getParameters() {
        return parameters;
    }
}
