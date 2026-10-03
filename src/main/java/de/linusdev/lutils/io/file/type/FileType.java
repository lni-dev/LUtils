/*
 * Copyright (c) 2026 Linus Andera
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

package de.linusdev.lutils.io.file.type;

import de.linusdev.lutils.net.http.header.contenttype.ContentType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface FileType {

    static boolean equals(@NotNull FileType that,  @NotNull FileType other) {
        if(that == other)
            return true;

        for (ContentType thatType : that.associatedContentTypes()) {
            for (ContentType otherType : other.associatedContentTypes()) {
                if(ContentType.equals(thatType, otherType))
                    return true;
            }
        }

        return false;
    }

    @NotNull String fullName();

    @NotNull String @NotNull [] fileEndings();

    default @Nullable ContentType contentType() {
        var associatedContentTypes = associatedContentTypes();
        return associatedContentTypes.length == 0 ? null : associatedContentTypes[0];
    }

    @NotNull ContentType @NotNull [] associatedContentTypes();

}
