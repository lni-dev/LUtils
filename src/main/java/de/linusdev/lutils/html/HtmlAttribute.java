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

import de.linusdev.lutils.html.parser.HtmlWritingState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.Writer;

public interface HtmlAttribute extends HtmlWritable{

    /**
     * The type of the attribute
     */
    @NotNull HtmlAttributeType<?> type();

    /**
     * Attribute value or {@code null} if this attribute has no value.
     */
    @Nullable String value();

    @NotNull HtmlAttribute copy();

    @Override
    default void write(@NotNull HtmlWritingState state, @NotNull Writer writer) throws IOException {
        writer.append(type().name());

        String value = value();
        if(value != null) {
            writer.append("=\"").append(HtmlUtils.escape(value, true)).append("\"");
        }
    }
}
