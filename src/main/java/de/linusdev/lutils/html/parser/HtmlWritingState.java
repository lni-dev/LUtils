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

package de.linusdev.lutils.html.parser;

import org.jetbrains.annotations.NotNull;

/**
 * Current writing state. tracks {@link #indent}.
 */
public class HtmlWritingState {

    private final @NotNull String baseIndent;

    /**
     * Current indent as string.
     */
    private @NotNull String indent = "";

    /**
     *
     * @param baseIndent base indent, which will be repeated.
     */
    public HtmlWritingState(@NotNull String baseIndent) {
        this.baseIndent = baseIndent;
    }

    public @NotNull String getIndent() {
        return indent;
    }

    /**
     * Increase indent.
     */
    public void addIndent() {
        indent += baseIndent;
    }

    /**
     * Decrease indent.
     */
    public void removeIndent() {
        indent = indent.substring(0, indent.length() - baseIndent.length());
    }

}
