/*
 * Copyright (c) 2023-2025 Linus Andera
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
