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

package de.linusdev.lutils.http.header.value;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BasicHeaderValueImpl implements BasicHeaderValue {

    private @Nullable List<@NotNull String> values;
    private @Nullable Map<@NotNull String, @NotNull String> parameters;

    public BasicHeaderValueImpl(
            @Nullable List<@NotNull String> values,
            @Nullable Map<@NotNull String, @NotNull String> parameters) {
        this.values = values;
        this.parameters = parameters;
    }

    public BasicHeaderValueImpl(
            @NotNull String value) {
        this.values = new ArrayList<>(1);
        this.values.add(value);
        this.parameters = null;
    }

    /**
     * get the values of this header-value
     * @return list of string values
     */
    public @NotNull List<@NotNull String> getValues() {
        if(values == null) values = new ArrayList<>(1);
        return values;
    }

    @Override
    public @NotNull Map<@NotNull String, @NotNull String> getParameters() {
        if(parameters == null) parameters = new HashMap<>(2);
        return parameters;
    }

    @Override
    public String asString() {
        return PARSER.parse(this);
    }
}
