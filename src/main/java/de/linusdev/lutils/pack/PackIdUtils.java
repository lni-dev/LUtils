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

package de.linusdev.lutils.pack;

import de.linusdev.lutils.data.json.Json;
import de.linusdev.lutils.id.Identifier;
import de.linusdev.lutils.id.IdentifierType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PackIdUtils {

    public static @NotNull Identifier ofJson(@NotNull Json data, @Nullable IdentifierType expectedType) {
        String id = data.grab("id").requireNotNull(key -> new NullPointerException("Field 'id' is required.")).getAs();

        if(Identifier.isValidIdentifier(id)) {
            return expectedType == null ? Identifier.ofString(id) : Identifier.ofStringEnforceType(id, expectedType);
        }

        String type = data.grab("type").orDefaultIfNull("").getAs();
        String namespace = data.grab("namespace").orDefaultIfNull("").getAs();
        id = type + ":" + namespace + ":" + id;

        Identifier.assertValidIdentifier(id);
        return expectedType == null ? Identifier.ofString(id) : Identifier.ofStringEnforceType(id, expectedType);
    }

}
