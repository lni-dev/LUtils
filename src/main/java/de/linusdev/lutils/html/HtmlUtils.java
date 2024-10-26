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

import de.linusdev.lutils.other.UnknownConstantException;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

public class HtmlUtils {

    /**
     * Map for unescape operations.
     */
    public static final @NotNull HashMap<String, String> UNESCAPE_MAP = new HashMap<>(Map.of(
            "amp", "&",
            "quot", "\"",
            "lt", "<",
            "gt", ">",
            "Tab", "\t",
            "NewLine", "\n",
            "nbsp", " "
    ));


    /**
     * <ul>
     *     <li>
     *         Always replaces all occurrences of {@code &} in {@code text} with {@code &amp;}.
     *     </li>
     *     <li>
     *         If {@code attribute} is {@code true}, all {@code "} will be replaced with {@code &quot;}.
     *     </li>
     *     <li>
     *         If {@code attribute} is {@code false}, all {@code <} and {@code >} will be replaced with
     *         {@code &lt;} and {@code &gt;} respectively.
     *     </li>
     * </ul>
     * @param text the text to escape
     * @param attribute whether the text is an attribute value
     * @return escaped text as described above.
     */
    public static @NotNull String escape(@NotNull String text, boolean attribute) {
        text = text.replaceAll("&", "&amp;");

        if(attribute) {
            text = text.replaceAll("\"", "&quot;");
        } else {
            text = text.replaceAll("<", "&lt;");
            text = text.replaceAll(">", "&gt;");
        }

        return text;
    }

    /**
     * Unescapes html text. Does not work on all special escape-names. Always works on html escaped by
     * {@link #escape(String, boolean)}. Works on all escape-names contained in {@link #UNESCAPE_MAP}.
     */
    @SuppressWarnings("unused")
    public static @NotNull String unescape(@NotNull String text) {
        StringReader reader = new StringReader(text);
        Unescaper unescaper = new Unescaper();
        try {
            int r;
            while (((r = reader.read()) != -1)) {
                unescaper.append((char) r);
            }
        } catch (IOException ignored) {}

        return unescaper.getString();
    }

    private static @NotNull String decodeEscapeValue(@NotNull String value) {
        if(value.startsWith("#")) {
            int c = Integer.parseInt(value.substring(1));
            return Character.toString(c);
        }

        String c = UNESCAPE_MAP.get(value);
        if(c == null)
            throw new UnknownConstantException(value);
        return c;

    }

    /**
     * Useful class to {@link #unescape(String) unescape} an incoming stream of characters.
     * The same limitations as for {@link #unescape(String)} apply.
     */
    public static class Unescaper {
        private final StringBuilder sb = new StringBuilder();
        boolean amp = false;
        StringBuilder ampString = new StringBuilder();

        public void append(char c) {
            if(amp) {
                if(c == ';') {
                    sb.append(decodeEscapeValue(ampString.toString()));
                    amp = false;
                    ampString = new StringBuilder();
                } else {
                    ampString.append(c);
                }
                return;
            }

            if(c == '&') {
                amp = true;
                return;
            }

            sb.append(c);
        }

        public @NotNull String getString() {
            return sb.toString();
        }
    }

}
