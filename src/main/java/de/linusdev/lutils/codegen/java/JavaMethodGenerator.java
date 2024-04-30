package de.linusdev.lutils.codegen.java;

import de.linusdev.lutils.codegen.GeneratorState;
import de.linusdev.lutils.codegen.PartGenerator;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class JavaMethodGenerator implements JavaAnnotateable, PartGenerator<JavaSourceGenerator> {

    protected final @NotNull JavaFileState ft;

    protected final @NotNull JavaClass parentClass;
    protected final @NotNull JavaClass returnType;
    protected final @NotNull String name;

    protected boolean isStatic = false;
    protected boolean isFinal = false;
    protected boolean isConstructor = false;
    protected @NotNull JavaVisibility visibility = JavaVisibility.PACKAGE_PRIVATE;

    protected @NotNull List<JavaAnnotation> annotations = new ArrayList<>();
    protected @NotNull List<JavaLocalVariable> parameters = new ArrayList<>();
    protected @NotNull JavaBlockContents body = new JavaBlockContents();

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

    public void setConstructor(boolean constructor) {
        isConstructor = constructor;
    }

    public boolean isConstructor() {
        return isConstructor;
    }

    public @NotNull String getName() {
        return name;
    }

    public @NotNull JavaClass getReturnType() {
        return returnType;
    }

    public <T> @NotNull JavaLocalVariable addParameter(
            @NotNull String name,
            @NotNull JavaClass type
    ) {
        JavaLocalVariable variable = new JavaLocalVariable(ft, type, name);
        ft.addImport(type.getRequiredImport());
        parameters.add(variable);
        return variable;
    }

    public @NotNull List<JavaLocalVariable> getParameters() {
        return parameters;
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

    public void body(@NotNull Consumer<JavaBlockContents> consumer) {
        consumer.accept(body);
    }

    @Override
    public void write(@NotNull Appendable writer, @NotNull GeneratorState<JavaSourceGenerator> state) throws IOException {
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
                .append(state.getSg().javaMethodOpenExpression(this))
                .append(state.getSg().javaLineBreak());

        // Method Body
        body.write(writer, state);

        // Close Method
        writer
                .append(state.getSg().javaLineBreak())
                .append(state.getIndent())
                .append(state.getSg().javaMethodCloseExpression());
    }
}
