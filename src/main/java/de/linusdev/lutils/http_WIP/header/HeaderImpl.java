/*
 * Copyright (c) 2023 Linus Andera
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
