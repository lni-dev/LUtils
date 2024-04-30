package de.linusdev.lutils.codegen.java;

import de.linusdev.lutils.codegen.GeneratorState;
import de.linusdev.lutils.codegen.PartGenerator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JavaClassGenerator implements JavaClass, PartGenerator<JavaSourceGenerator>, JavaAnnotateable {

    protected final @NotNull JavaFileState ft;
    protected final @NotNull JavaSourceGenerator sg;
    protected final @NotNull JavaPackage jPackage;

    protected @NotNull JavaVisibility visibility = JavaVisibility.PUBLIC;
    protected @NotNull JavaClassType type = JavaClassType.CLASS;
    protected @Nullable String name = null;
    protected @Nullable JavaClass extendedClass = null;
    protected @NotNull JavaClass @NotNull [] implementedClasses = new JavaClass[0];

    protected @NotNull List<JavaAnnotation> annotations = new ArrayList<>();
    protected @NotNull List<JavaVariable> variables = new ArrayList<>();
    protected @NotNull List<JavaMethodGenerator> methods = new ArrayList<>();

    public JavaClassGenerator(
            @NotNull JavaFileState ft,
            @NotNull JavaSourceGenerator sg,
            @NotNull JavaPackage jPackage
    ) {
        this.ft = ft;
        this.sg = sg;
        this.jPackage = jPackage;
    }

    public @NotNull JavaVariable addVariable(
            @NotNull JavaClass type,
            @NotNull String name
    ) {
        JavaVariable variable = new JavaVariable(ft, this, type, name);
        ft.addImport(type.getRequiredImport());
        variables.add(variable);
        return variable;
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
            ft.addImport(extendedClass.getRequiredImport());

        this.extendedClass = extendedClass;
    }

    public void setImplementedClasses(@NotNull JavaClass @NotNull [] implementedClasses) {
        for (JavaClass implementedClass : implementedClasses) {
            if(implementedClass.isPrimitive() || implementedClass.isArray())
                throw new IllegalStateException("Cannot implement a primitive or array.");

            ft.addImport(implementedClass.getRequiredImport());
        }

        this.implementedClasses = implementedClasses;
    }

    @Override
    public @NotNull JavaAnnotation addAnnotation(@NotNull JavaClass annotationClass) {
        JavaAnnotation annotation = new JavaAnnotation(annotationClass);
        annotations.add(annotation);
        ft.addImport(annotation.type.getRequiredImport());
        return annotation;
    }

    @Override
    public void addAnnotation(@NotNull JavaAnnotation annotation) {
        annotations.add(annotation);
        ft.addImport(annotation.type.getRequiredImport());
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

    @Override
    public @NotNull JavaPackage getPackage() {
        return jPackage;
    }

    @Override
    public @NotNull String getName() {
        if(name == null)
            throw new IllegalStateException("Class name must be set.");
        return name;
    }

    @Override
    public void write(@NotNull Appendable writer, @NotNull GeneratorState<JavaSourceGenerator> state) throws IOException {

        if(name == null)
            throw new IllegalStateException("Class name must not be null.");

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
                .append(sg.javaClassOpenExpression(visibility, type, name, extendedClass, implementedClasses))
                .append(sg.javaLineBreak())
                .append(sg.javaLineBreak());
        state.increaseIndent();

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

        // End Class
        state.decreaseIndent();
        writer.append(state.getIndent()).append(sg.javaClassEndExpression());

    }
}