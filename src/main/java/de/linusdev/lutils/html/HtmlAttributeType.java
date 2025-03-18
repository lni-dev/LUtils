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

package de.linusdev.lutils.html;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface HtmlAttributeType<V> {

    /**
     * {@link Object#hashCode() Hashcode} method all classes implementing this interface should use.
     */
    static int hashcode(@NotNull HtmlAttributeType<?> type) {
        return type.name().hashCode();
    }

    /**
     * Name or key of the attribute.
     */
    @NotNull String name();

    @Nullable V convertValue(@NotNull HtmlAttribute attribute);

}
