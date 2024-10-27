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

package de.linusdev.lutils.html.lhtml;

import de.linusdev.lutils.html.HtmlAddable;
import org.jetbrains.annotations.NotNull;

public interface LhtmlTemplate extends LhtmlIdentifiable, LhtmlHasTemplates {

    /**
     * Get the placeholder with given {@code id} of this template. If no such placeholder exists,
     * a {@link NullPointerException} will be thrown.
     */
    @NotNull HtmlAddable getPlaceholder(@NotNull String id);

    /**
     * Set the replace-value with given {@code key} to given {@code value}.
     */
    void setValue(@NotNull String key, @NotNull String value);

}
