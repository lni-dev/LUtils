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

public class JavaEnumMemberGenerator implements PartGenerator<JavaSourceGeneratorHelper>, JavaAnnotateable, JavaDocable {

    protected final @NotNull JavaFileState ft;

    protected final @NotNull String name;
    protected final @NotNull JavaExpression @NotNull [] parameters;

    protected @Nullable JavaDocGenerator javaDoc = null;
    protected @NotNull List<JavaAnnotation> annotations = new ArrayList<>();

    public JavaEnumMemberGenerator(
            @NotNull JavaFileState ft, @NotNull String name,
            @NotNull JavaExpression @NotNull ... parameters
    ) {
        this.ft = ft;
        this.name = name;
        this.parameters = parameters;
    }

    @Override
    public void write(@NotNull Appendable writer, @NotNull GeneratorState<JavaSourceGeneratorHelper> state) throws IOException {

        //JavaDoc
        if(javaDoc != null)
            javaDoc.write(writer, state);

        // Annotations
        for (JavaAnnotation annotation : annotations) {
            writer
                    .append(state.getIndent())
                    .append(state.getSg().javaAnnotation(annotation))
                    .append(state.getSg().javaLineBreak());
        }

        writer.append(state.getIndent()).append(name);

        if(parameters.length > 0) {
            writer.append("(");

            boolean first = true;
            for (JavaExpression parameter : parameters) {
                if(first) first = false;
                else writer.append(", ");
                writer.append(parameter.getExprString(state.getSg()));
            }

            writer.append(")");
        }

    }

    @Override
    public @NotNull JavaAnnotation addAnnotation(@NotNull JavaClass annotationClass) {
        JavaAnnotation annotation = new JavaAnnotation(ft, annotationClass);
        annotations.add(annotation);
        return annotation;
    }

    @Override
    public @NotNull JavaDocGenerator setJavaDoc() {
        if(javaDoc == null)
            javaDoc = new JavaDocGenerator();
        return javaDoc;
    }
}
