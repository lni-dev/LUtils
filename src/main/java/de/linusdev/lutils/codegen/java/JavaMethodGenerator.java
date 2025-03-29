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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JavaMethodGenerator implements
        JavaAnnotateable,
        PartGenerator<JavaSourceGeneratorHelper>,
        JavaDocable,
        JavaMethod
{

    protected final @NotNull JavaFileState ft;

    protected final @NotNull JavaClass parentClass;
    protected final @NotNull JavaClass returnType;
    protected final @NotNull String name;

    protected boolean isStatic = false;
    protected boolean isNative = false;
    protected boolean noBody = false;
    protected boolean isFinal = false;
    protected boolean isConstructor = false;
    protected @NotNull JavaVisibility visibility = JavaVisibility.PACKAGE_PRIVATE;
    protected @Nullable JavaDocGenerator javaDoc = null;

    protected final @NotNull List<JavaAnnotation> annotations = new ArrayList<>();
    protected final @NotNull List<JavaLocalVariable> parameters = new ArrayList<>();
    protected final @NotNull JavaBlockContents body;

    public JavaMethodGenerator(
            @NotNull JavaFileState ft,
            @NotNull JavaClass parentClass,
            @NotNull JavaClass returnType,
            @NotNull String name
    ) {
        this.ft = ft;
        this.parentClass = parentClass;
        this.returnType = returnType;
        this.name = name;
        this.body = new JavaBlockContents(ft);
        ft.addImport(returnType.getRequiredImports());
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

    @Override
    public boolean isStatic() {
        return isStatic;
    }

    public void setFinal(boolean jFinal) {
        isFinal = jFinal;
    }

    public void setNative(boolean aNative) {
        isNative = aNative;
        setNoBody(true);
    }

    public void setNoBody(boolean noBody) {
        this.noBody = noBody;
    }

    public boolean isNative() {
        return isNative;
    }

    public boolean isNoBody() {
        return noBody;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public void setConstructor(boolean constructor) {
        isConstructor = constructor;
    }

    public boolean isConstructor() {
        return isConstructor;
    }

    public @NotNull String getName() {
        return name;
    }

    @Override
    public @NotNull JavaClass getParentClass() {
        return parentClass;
    }

    public @NotNull JavaClass getReturnType() {
        return returnType;
    }

    public <T> @NotNull JavaLocalVariable addParameter(
            @NotNull String name,
            @NotNull JavaClass type
    ) {
        JavaLocalVariable variable = new JavaLocalVariable(ft, type, name);
        ft.addImport(type.getRequiredImports());
        parameters.add(variable);
        return variable;
    }

    public @NotNull List<JavaLocalVariable> getParameters() {
        return parameters;
    }

    @Override
    public @NotNull JavaAnnotation addAnnotation(@NotNull JavaClass annotationClass) {
        JavaAnnotation annotation = new JavaAnnotation(ft, annotationClass);
        annotations.add(annotation);
        return annotation;
    }

    public void body(@NotNull JavaBlockContentsConsumer consumer) {
        consumer.accept(body);
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

        // Open Method
        writer
                .append(state.getIndent())
                .append(state.getSg().javaMethodOpenExpression(this));

        // Method Body
        if(!isNoBody()) {
            writer.append(state.getSg().javaLineBreak());
            body.write(writer, state);
            writer
                    .append(state.getSg().javaLineBreak())
                    .append(state.getIndent());
        }

        // Close Method
        writer.append(state.getSg().javaMethodCloseExpression(this));
    }

    @Override
    public @NotNull JavaDocGenerator setJavaDoc() {
        if(javaDoc == null)
            javaDoc = new JavaDocGenerator();
        return javaDoc;
    }
}
