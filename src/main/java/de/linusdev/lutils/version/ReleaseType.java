

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

package de.linusdev.lutils.version;

import de.linusdev.lutils.other.UnknownConstantException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum ReleaseType {

    DEVELOPMENT_BUILD("dev"),

    EARLY_ACCESS("early-access"),

    RELEASE(null),
    SNAPSHOT("snapshot"),

    CLOSED_ALPHA("closed-alpha"),
    CLOSED_BETA("closed-beta"),
    ALPHA("alpha"),
    BETA("beta"),

    ;

    public static @NotNull ReleaseType ofAppendix(@Nullable String appendix) {
        if(appendix == null)
            return RELEASE;
        for (ReleaseType type : values()) {
            if(type.appendix != null && type.appendix.equals(appendix))
                return type;
        }

        throw new UnknownConstantException(appendix);
    }

    private final @Nullable String appendix;

    ReleaseType(@Nullable String appendix) {
        this.appendix = appendix;
    }

    public @Nullable String appendix() {
        return appendix;
    }
}
