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
import java.util.Locale;

public class JavaClassGenerator implements
        JavaClass,
        PartGenerator<JavaSourceGeneratorHelper>,
        JavaAnnotateable,
        JavaDocable
{

    protected final @NotNull JavaFileState ft;
    protected final @NotNull JavaSourceGeneratorHelper sg;
    protected final @NotNull JavaPackage jPackage;

    protected final @Nullable JavaClassGenerator parent;
    protected @NotNull JavaVisibility visibility = JavaVisibility.PUBLIC;
    protected boolean isStatic = false;
    protected @NotNull JavaClassType type = JavaClassType.CLASS;
    protected @Nullable String name = null;
    protected @Nullable JavaClass extendedClass = null;
    protected @NotNull JavaClass @NotNull [] implementedClasses = new JavaClass[0];
    protected @Nullable JavaDocGenerator javaDoc = null;

    protected @NotNull List<JavaAnnotation> annotations = new ArrayList<>();
    protected @NotNull List<JavaVariable> variables = new ArrayList<>();
    protected @NotNull List<JavaMethodGenerator> methods = new ArrayList<>();
    protected @NotNull List<JavaClassGenerator> subClasses = new ArrayList<>();
    protected @NotNull List<JavaEnumMemberGenerator> enumMembers = new ArrayList<>();

    public JavaClassGenerator(
            @NotNull JavaFileState ft,
            @NotNull JavaSourceGeneratorHelper sg,
            @NotNull JavaPackage jPackage,
            @Nullable JavaClassGenerator parent
    ) {
        this.ft = ft;
        this.sg = sg;
        this.jPackage = jPackage;
        this.parent = parent;
    }

    public @NotNull JavaEnumMemberGenerator addEnumMember(
            @NotNull String name,
            @NotNull JavaExpression @NotNull ... parameters
    ) {
        var e = new JavaEnumMemberGenerator(ft, name, parameters);
        enumMembers.add(e);
        return e;
    }

    public @NotNull JavaClassGenerator addSubClass(boolean isStatic) {
        JavaClassGenerator clazz = new JavaClassGenerator(ft, sg, jPackage, this);
        clazz.setStatic(isStatic);
        subClasses.add(clazz);
        return clazz;
    }

    public @NotNull JavaVariable addVariable(
            @NotNull JavaClass type,
            @NotNull String name
    ) {
        JavaVariable variable = new JavaVariable(ft, this, type, name);
        ft.addImport(type.getRequiredImports());
        variables.add(variable);
        return variable;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public void setStatic(boolean aStatic) {
        isStatic = aStatic;
    }

    public void setType(@NotNull JavaClassType type) {
        this.type = type;
    }

    public void setName(@Nullable String name) {
        this.name = name;
    }

    public void setVisibility(@NotNull JavaVisibility visibility) {
        this.visibility = visibility;
    }

    public void setExtendedClass(@Nullable JavaClass extendedClass) {
        if(extendedClass != null && (extendedClass.isPrimitive() || extendedClass.isArray()))
            throw new IllegalStateException("Cannot extend a primitive or array.");

        if(extendedClass != null)
            ft.addImport(extendedClass.getRequiredImports());

        this.extendedClass = extendedClass;
    }

    public void setImplementedClasses(@NotNull JavaClass @NotNull [] implementedClasses) {
        for (JavaClass implementedClass : implementedClasses) {
            if(implementedClass.isPrimitive() || implementedClass.isArray())
                throw new IllegalStateException("Cannot implement a primitive or array.");

            ft.addImport(implementedClass.getRequiredImports());
        }

        this.implementedClasses = implementedClasses;
    }

    @Override
    public @NotNull JavaAnnotation addAnnotation(@NotNull JavaClass annotationClass) {
        JavaAnnotation annotation = new JavaAnnotation(ft, annotationClass);
        annotations.add(annotation);
        return annotation;
    }

    public JavaMethodGenerator addMethod(@NotNull JavaClass returnType, @NotNull String name) {
        JavaMethodGenerator method = new JavaMethodGenerator(
                ft,
                this,
                returnType,
                name
        );

        methods.add(method);
        return method;
    }

    public JavaMethodGenerator addGetter(@NotNull JavaVariable variable) {
        JavaMethodGenerator method = new JavaMethodGenerator(
                ft,
                this,
                variable.type,
                "get" + variable.name.substring(0, 1).toUpperCase(Locale.ROOT) + variable.name.substring(1)
        );

        method.body(block -> block.addExpression(JavaExpression.returnExpr(variable)));

        methods.add(method);
        return method;
    }

    public JavaMethodGenerator addConstructor() {
        JavaMethodGenerator method = new JavaMethodGenerator(
                ft,
                this,
                this,
                ""
        );

        method.setConstructor(true);

        methods.add(method);
        return method;
    }

    @Override
    public @NotNull JavaPackage getPackage() {
        return jPackage;
    }

    @Override
    public @NotNull String getName() {
        if(name == null)
            throw new IllegalStateException("Class name must be set.");
        if(parent != null)
            return parent.getName() + "." + name;
        return name;
    }


    @Override
    public void write(@NotNull Appendable writer, @NotNull GeneratorState<JavaSourceGeneratorHelper> state) throws IOException {

        if(name == null)
            throw new IllegalStateException("Class name must not be null.");

        //JavaDoc
        if(javaDoc != null)
            javaDoc.write(writer, state);

        // Annotations
        for (JavaAnnotation annotation : annotations) {
            writer
                    .append(state.getIndent())
                    .append(sg.javaAnnotation(annotation))
                    .append(sg.javaLineBreak());
        }

        // Start Class
        writer
                .append(state.getIndent())
                .append(sg.javaClassOpenExpression(visibility, isStatic, type, name, extendedClass, implementedClasses))
                .append(sg.javaLineBreak())
                .append(sg.javaLineBreak());
        state.increaseIndent();

        // Enum Members
        if(type == JavaClassType.ENUM) {
            for (JavaEnumMemberGenerator enumMember : enumMembers) {
                enumMember.write(writer, state);
                writer.append(",").append(sg.javaLineBreak());
            }

            writer
                    .append(state.getIndent())
                    .append(sg.javaExpressionEnd())
                    .append(sg.javaLineBreak())
                    .append(sg.javaLineBreak());
        }

        // Variables
        for (JavaVariable variable : variables) {
            variable.write(writer, state);
            writer.append(sg.javaExpressionEnd());
            writer.append(sg.javaLineBreak());
        }

        writer.append(sg.javaLineBreak());

        // Methods
        for (JavaMethodGenerator method : methods) {
            method.write(writer, state);
            writer.append(sg.javaLineBreak()).append(sg.javaLineBreak());
        }

        // Sub Classes
        for (JavaClassGenerator subClass : subClasses) {
            subClass.write(writer, state);
            writer.append(sg.javaLineBreak()).append(sg.javaLineBreak());
        }

        // End Class
        state.decreaseIndent();
        writer.append(state.getIndent()).append(sg.javaClassEndExpression());

    }

    @Override
    public @NotNull JavaDocGenerator setJavaDoc() {
        if(javaDoc == null)
            javaDoc = new JavaDocGenerator();
        return javaDoc;
    }
}
