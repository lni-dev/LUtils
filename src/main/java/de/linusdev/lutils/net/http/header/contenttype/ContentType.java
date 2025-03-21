/*
 * Copyright (c) 2024-2025 Linus Andera
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

package de.linusdev.lutils.net.http.header.contenttype;

import de.linusdev.lutils.net.http.header.Header;
import de.linusdev.lutils.net.http.header.HeaderNames;
import de.linusdev.lutils.net.http.header.value.BasicHeaderValue;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Content-type: {@code type/subtype}
 */
public interface ContentType extends BasicHeaderValue {

    @Contract("_, _ -> new")
    static @NotNull ContentType of(@NotNull String type, @NotNull String subtype) {
        return new ContentTypes(type, subtype);
    }

    static boolean equals(@NotNull ContentType that, @Nullable ContentType other) {
        if(that == other) return true;
        if(other == null) return false;
        return that.type().equals(other.type()) && that.subtype().equals(other.subtype());
    }

    /**
     * The type before the slash.
     */
    @NotNull String type();

    /**
     * The subtype after the slash.
     */
    @NotNull String subtype();

    /**
     * This {@link ContentType} as content-type {@link Header}
     */
    default Header asHeader() {
        return PARSER.parse(HeaderNames.CONTENT_TYPE, this);
    }

    @Override
    @NotNull
    default ContentType set(@NotNull String name, @Nullable String value) {
        BasicHeaderValue.super.set(name, value);
        return this;
    }

    @Override
    @NotNull
    default ContentType add(@NotNull String value, boolean check) {
        BasicHeaderValue.super.add(value, check);
        return this;
    }

    @Override
    @NotNull
    default ContentType add(@NotNull String value) {
        BasicHeaderValue.super.add(value);
        return this;
    }

    @Override
    @NotNull
    default ContentType remove(@NotNull String value) {
        BasicHeaderValue.super.remove(value);
        return this;
    }
}
