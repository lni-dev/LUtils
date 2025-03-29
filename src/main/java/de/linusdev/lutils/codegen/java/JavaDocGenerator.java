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

package de.linusdev.lutils.codegen.java;

import de.linusdev.lutils.codegen.GeneratorState;
import de.linusdev.lutils.codegen.PartGenerator;
import de.linusdev.lutils.html.HtmlUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class JavaDocGenerator implements PartGenerator<JavaSourceGeneratorHelper> {

    public static @NotNull String escape(@NotNull String text) {
        return HtmlUtils.escape(text, false);
    }

    private final StringBuilder text = new StringBuilder();

    public void addText(@NotNull String text) {
        addText(text, false);
    }

    public void addText(@NotNull String text, boolean escape) {
        if(escape)
            text = escape(text);
        this.text.append(text);
    }

    public void addTextNl(@NotNull String text) {
        addTextNl(text, false);
    }

    public void addTextNl(@NotNull String text, boolean escape) {
        if(escape)
            text = escape(text);
        this.text.append(text).append("\n");
    }

    public void addAtText(@NotNull String at, @NotNull String text) {
        addAtText(at, text, false);
    }

    public void addAtText(@NotNull String at, @NotNull String text, boolean escape) {
        if(escape)
            text = escape(text);
        this.text.append("\n").append("@").append(at).append(" ").append(text).append("\n");
    }


    @Override
    public void write(@NotNull Appendable writer, @NotNull GeneratorState<JavaSourceGeneratorHelper> state) throws IOException {

        writer.append(state.getSg().javaLineBreak());
        writer.append(state.getIndent()).append("/**").append(state.getSg().javaLineBreak());

        String fin = text.toString();
        String[] lines = fin.split("\n");
        for (String line : lines) {
            writer
                    .append(state.getIndent())
                    .append(" * ")
                    .append(line)
                    .append(state.getSg().javaLineBreak());
        }

        writer.append(state.getIndent()).append(" */");
        writer.append(state.getSg().javaLineBreak());

    }
}
