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
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class JavaVariable implements
        JavaAssignable,
        JavaAnnotateable,
        PartGenerator<JavaSourceGeneratorHelper>,
        JavaDocable
{

    protected final @Nullable JavaFileState ft;

    protected final @NotNull JavaClass parentClass;
    protected final @NotNull JavaClass type;
    protected final @NotNull String name;

    protected boolean isStatic = false;
    protected boolean isFinal = false;
    protected @NotNull JavaVisibility visibility = JavaVisibility.PACKAGE_PRIVATE;
    protected @Nullable JavaExpression defaultValue;
    protected @Nullable JavaDocGenerator javaDoc = null;

    protected @NotNull List<JavaAnnotation> annotations = new ArrayList<>();

    public static <T> @NotNull JavaVariable of(
            @NotNull Class<?> clazz,
            @NotNull String varName
    ) {
        try {


            if(clazz.isAnnotation()) {
                Method m = clazz.getMethod(varName);

                return new JavaVariable(
                        null, JavaClass.ofClass(clazz),
                        JavaClass.ofClass(m.getReturnType()),
                        varName
                );
            }

            Field f = clazz.getField(varName);

            JavaVariable variable = new JavaVariable(
                    null, JavaClass.ofClass(clazz),
                    JavaClass.ofClass(f.getType()),
                    varName
            );
            variable.setFinal(Modifier.isFinal(f.getModifiers()));
            variable.setStatic(Modifier.isStatic( f.getModifiers()));


            return variable;
        } catch (NoSuchFieldException | NoSuchMethodException e) {
            throw new IllegalStateException(e);
        }
    }

    public JavaVariable(
            @Nullable JavaFileState ft,
            @NotNull JavaClass parentClass,
            @NotNull JavaClass type,
            @NotNull String name
    ) {
        this.ft = ft;
        this.parentClass = parentClass;
        this.type = type;
        this.name = name;
    }

    public void setVisibility(@NotNull JavaVisibility visibility) {
        this.visibility = visibility;
    }

    public @NotNull JavaVisibility getVisibility() {
        return visibility;
    }

    public void setStatic(boolean jStatic) {
        isStatic = jStatic;
    }

    public boolean isStatic() {
        return isStatic;
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
        return JavaClass.equals(parentClass, variable.parentClass) && name.equals(variable.name);
    }

    @Override
    public int hashCode() {
        int result = JavaClass.hashcode(parentClass);
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public @NotNull JavaAnnotation addAnnotation(@NotNull JavaClass annotationClass) {
        if(ft == null)
            throw new IllegalStateException("Cannot add an annotation to a non generator variable.");
        JavaAnnotation annotation = new JavaAnnotation(ft, annotationClass);
        annotations.add(annotation);
        return annotation;
    }

    @Override
    public void write(@NotNull Appendable writer, @NotNull GeneratorState<JavaSourceGeneratorHelper> state) throws IOException {

        //JavaDoc
        if(javaDoc != null)
            javaDoc.write(writer, state);

        for(JavaAnnotation annotation : annotations)
            writer
                    .append(state.getIndent())
                    .append(state.getSg().javaAnnotation(annotation))
                    .append(state.getSg().javaLineBreak());

        writer
                .append(state.getIndent())
                .append(state.getSg().javaVariableExpression(this));
    }

    @Override
    public @NotNull JavaDocGenerator setJavaDoc() {
        if(javaDoc == null)
            javaDoc = new JavaDocGenerator();
        return javaDoc;
    }
}
