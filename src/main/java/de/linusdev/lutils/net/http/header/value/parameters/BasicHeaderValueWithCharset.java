/*
 * Copyright (c) 2024 Linus Andera
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

package de.linusdev.lutils.net.http.header.value.parameters;

import de.linusdev.lutils.net.http.header.value.BasicHeaderValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface BasicHeaderValueWithCharset extends BasicHeaderValue {

    /**
     * {@link #set(String, String) Set} parameter 'charset' to given {@code charset}.
     * @param charset charset name or {@code null} to remove charset parameter
     */
    default @NotNull BasicHeaderValueWithCharset setCharset(@Nullable String charset) {
        set("charset", charset);
        return this;
    }

    default @Nullable String getCharset() {
        return get("charset");
    }

}
