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

package de.linusdev.lutils.codegen.java;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class JavaAnnotation {
    protected final @NotNull JavaFileState ft;
    protected final @NotNull JavaClass type;
    protected @Nullable Map<JavaVariable, JavaExpression> values;

    public JavaAnnotation(
            @NotNull JavaFileState ft,
            @NotNull JavaClass type
    ) {
        this.ft = ft;
        this.type = type;
        ft.addImport(type.getRequiredImports());
    }

    public @NotNull JavaClass getType() {
        return type;
    }

    public @Nullable Map<JavaVariable, JavaExpression> getValues() {
        return values;
    }

    public <T> void setValue(@NotNull JavaVariable variable, @NotNull JavaExpression expression) {
        if(values == null)
            values = new HashMap<>();
        values.put(variable, expression);
        ft.addImport(expression.getRequiredImports());
    }
}
