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

package de.linusdev.lutils.data;

import de.linusdev.lutils.collections.BiIterable;
import de.linusdev.lutils.data.json.parser.JsonParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Data extends BiIterable<String, Object>, Datable {

    /**
     * Current amount of entries contained in this data instance.
     */
    int size();

    /**
     * Create a json string using {@link JsonParser#DEFAULT_INSTANCE}.
     */
    default @NotNull String toJsonString() {
        return JsonParser.DEFAULT_INSTANCE.writeDataToString(this);
    }

    /**
     * How this data should be parsed
     * @return {@link ParseType}
     */
    default @NotNull ParseType parseType() {
        return ParseType.NORMAL;
    }

    @Override
    default @Nullable Data getData() {
        return this;
    }

}
