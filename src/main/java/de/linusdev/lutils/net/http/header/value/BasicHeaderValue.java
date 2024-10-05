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

package de.linusdev.lutils.net.http.header.value;

import de.linusdev.lutils.net.http.header.Header;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@link #PARSER} can parse a header-value of the following format:<br>
 * <pre>
 *{@code value, value, ...; paramKey=paramValue; ...}
 * </pre>
 * @see #getValues()
 * @see #get(String)
 */
public interface BasicHeaderValue extends HeaderValue {

    HeaderValueParser<BasicHeaderValue> PARSER = new HeaderValueParser<>() {
        @Override
        public @NotNull BasicHeaderValue parse(@NotNull Header header) {
            String value = header.getValue();

            String[] split = value.split(";");
            String[] values = split[0].split(",");

            for (int i = 0; i < values.length; i++)
                values[i] = values[i].strip();

            Map<String, String> parameters = new HashMap<>(2);
            for (int i = 1; i < split.length; i++) {
                String[] parameter = split[i].split("=");
                parameters.put(parameter[0].strip(), parameter[1].strip());
            }

            return new BasicHeaderValueImpl(new ArrayList<>(List.of(values)), parameters);
        }

        @Override
        public @NotNull String parse(@NotNull BasicHeaderValue value) {
            StringBuilder sb = new StringBuilder();

            boolean first = true;
            for (String val : value.getValues()) {
                if (!first)
                    sb.append(", ");
                first = false;

                sb.append(val);
            }

            if (value.getParameters().isEmpty()) {
                return sb.toString();
            }

            for (var entry : value.getParameters().entrySet()) {
                sb.append("; ")
                        .append(entry.getKey())
                        .append("=")
                        .append(entry.getValue());
            }

            return sb.toString();
        }
    };

    /**
     * get the values of this header-value. Changing this List also changes this {@link BasicHeaderValue}.
     * @return list of string values
     */
    @NotNull List<@NotNull String> getValues();

    /**
     * get the parameters of this header-value. Changing this Map also changes this {@link BasicHeaderValue}.
     * @return map of parameters
     */
    @NotNull Map<@NotNull String, @NotNull String> getParameters();

    /**
     * Get parameter with given name.
     * @param name parameter name
     * @return parameter or {@code null}, if no parameter with given key exists.
     */
    default @Nullable String get(@NotNull String name) {
        return getParameters().get(name);
    }

    /**
     * Set parameter with given name to given value
     * @param name parameter name
     * @param value parameter value or {@code null} to remove this parameter
     */
    default @NotNull BasicHeaderValue set(@NotNull String name, @Nullable String value) {
        if(value == null)
            getParameters().remove(name);
        else
            getParameters().put(name, value);

        return this;
    }

    /**
     * @see #add(String, boolean)
     */
    default @NotNull BasicHeaderValue add(@NotNull String value) {
        add(value, true);
        return this;
    }

    /**
     * Adds given value to this header-values {@link #getValues() value list}.
     * @param value value to add
     * @param check {@code true}: Only adds given value if it is not already in the {@link #getValues() value list}
     * {@code false}: Does not check if value is already in the {@link #getValues() value list}
     */
    default @NotNull BasicHeaderValue add(@NotNull String value, boolean check) {
        if(check && contains(value)) return this;
        getValues().add(value);
        return this;
    }

    /**
     * Removes given value to this header-values {@link #getValues() value list}.
     * @param value value to remove
     */
    default @NotNull BasicHeaderValue remove(@NotNull String value) {
        getValues().remove(value);
        return this;
    }

    /**
     * Check if given value is contained in {@link #getValues()}.
     * @param value value to check existence of
     * @return {@code true} if given value is contained in {@link #getValues()}
     */
    default boolean contains(@NotNull String value) {
        for(String val : getValues()) {
            if(value.equals(val))
                return true;
        }

        return false;
    }
}
