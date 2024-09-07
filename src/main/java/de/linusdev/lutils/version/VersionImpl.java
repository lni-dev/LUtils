
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

package de.linusdev.lutils.version;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VersionImpl implements Version{

    private final @NotNull ReleaseType type;
    private final @NotNull SimpleVersion version;
    private final @Nullable String prefix;
    private final @Nullable String postfix;

    public VersionImpl(@NotNull ReleaseType type, @NotNull SimpleVersion version, @Nullable String prefix, @Nullable String postfix) {
        this.type = type;
        this.version = version;
        this.prefix = prefix;
        this.postfix = postfix;
    }

    @Override
    public @NotNull ReleaseType type() {
        return type;
    }

    @Override
    public @NotNull SimpleVersion version() {
        return version;
    }

    @Override
    public @Nullable String prefix() {
        return prefix;
    }

    @Override
    public @Nullable String postfix() {
        return postfix;
    }

    @Override
    public String toString() {
        return getAsUserFriendlyString();
    }

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override
    public boolean equals(Object obj) {
        return Version.equals(this, obj);
    }
}