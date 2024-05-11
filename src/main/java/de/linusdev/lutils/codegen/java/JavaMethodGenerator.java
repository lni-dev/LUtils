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
        JavaAnnotation annotation = new JavaAnnotation(annotationClass);
        annotations.add(annotation);
        ft.addImport(annotation.type.getRequiredImports());
        return annotation;
    }

    @Override
    public void addAnnotation(@NotNull JavaAnnotation annotation) {
        annotations.add(annotation);
        ft.addImport(annotation.type.getRequiredImports());
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

    @Override
    public @NotNull JavaDocGenerator setJavaDoc() {
        if(javaDoc == null)
            javaDoc = new JavaDocGenerator();
        return javaDoc;
    }
}
