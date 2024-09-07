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

import de.linusdev.lutils.codegen.GeneratorState;
import de.linusdev.lutils.codegen.PartGenerator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JavaLocalVariable implements JavaAssignable, JavaAnnotateable, PartGenerator<JavaSourceGeneratorHelper> {

    protected final @Nullable JavaFileState ft;

    protected final @NotNull JavaClass type;
    protected final @NotNull String name;

    protected boolean isFinal = false;
    protected @Nullable JavaExpression defaultValue;

    protected @NotNull List<JavaAnnotation> annotations = new ArrayList<>();

    public JavaLocalVariable(
            @Nullable JavaFileState ft,
            @NotNull JavaClass type,
            @NotNull String name
    ) {
        this.ft = ft;
        this.type = type;
        this.name = name;
    }

    public void setFinal(boolean jFinal) {
        isFinal = jFinal;
    }

    public boolean isFinal() {
        return isFinal;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    public @NotNull JavaClass getType() {
        return type;
    }

    public @Nullable JavaExpression getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(@Nullable JavaExpression defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JavaVariable)) return false;

        JavaVariable variable = (JavaVariable) o;
        return name.equals(variable.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public @NotNull JavaAnnotation addAnnotation(@NotNull JavaClass annotationClass) {
        if(ft == null)
            throw new IllegalStateException("Cannot add an annotation to a non generator variable.");
        JavaAnnotation annotation = new JavaAnnotation(ft, annotationClass);
        annotations.add(annotation);
        return annotation;
    }

    public @NotNull List<JavaAnnotation> getAnnotations() {
        return annotations;
    }

    @Override
    public void write(@NotNull Appendable writer, @NotNull GeneratorState<JavaSourceGeneratorHelper> codeState) throws IOException {
        for(JavaAnnotation annotation : annotations)
            writer.append(codeState.getSg().javaAnnotation(annotation)).append(" ");

        writer.append(codeState.getSg().javaDeclareLocalVariableExpression(this));
    }
}
