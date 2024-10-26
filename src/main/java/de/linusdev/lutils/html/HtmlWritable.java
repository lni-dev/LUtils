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

package de.linusdev.lutils.html;

import de.linusdev.lutils.html.parser.HtmlWritingState;
import de.linusdev.lutils.interfaces.Writable;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Interface, that allows writing the html represented by this object to a {@link Writer}
 * or an {@link OutputStream}.
 */
public interface HtmlWritable extends Writable {

    /**
     * Write html to given {@code writer} using given {@code state}.
     * @param state {@link HtmlWritingState} to use while writing.
     * @param writer {@link Writer} to write to.
     * @throws IOException while writing
     */
    void write(@NotNull HtmlWritingState state, @NotNull Writer writer) throws IOException;

    /**
     * Calls {@link #write(HtmlWritingState, Writer)} with a writing-state created by {@code new HtmlWritingState("  ")}
     */
    default void write(@NotNull Writer writer) throws IOException {
        write(new HtmlWritingState("  "), writer);
    }

    /**
     * Calls {@link #write(Writer)} with {@code new OutputStreamWriter(out, StandardCharsets.UTF_8)}.
     * Also {@link OutputStreamWriter#flush() flushes} the writer after writing.
     */
    default void write(@NotNull OutputStream out) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);
        write(writer);
        writer.flush();
    }

    /**
     * Writes the html to a string.
     */
    default @NotNull String writeToString() {
        StringWriter writer = new StringWriter();
        try {
            write(writer);
        } catch (IOException ignored) {}
        return writer.toString();
    }

}
