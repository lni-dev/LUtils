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

public class JavaVariable implements JavaAnnotateable, PartGenerator<JavaSourceGenerator> {

    protected final @Nullable JavaFileState ft;

    protected final @NotNull JavaClass parentClass;
    protected final @NotNull JavaClass type;
    protected final @NotNull String name;

    protected boolean isStatic = false;
    protected boolean isFinal = false;
    protected @NotNull JavaVisibility visibility = JavaVisibility.PACKAGE_PRIVATE;
    protected @Nullable JavaExpression defaultValue;

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
        JavaAnnotation annotation = new JavaAnnotation(annotationClass);
        addAnnotation(annotation);
        return annotation;
    }

    @Override
    public void addAnnotation(@NotNull JavaAnnotation annotation) {
        if(ft == null)
            throw new IllegalStateException("Cannot add an annotation to a non generator variable.");
        annotations.add(annotation);
        ft.addImport(annotation.type.getRequiredImport());
    }

    @Override
    public void write(@NotNull Appendable writer, @NotNull GeneratorState<JavaSourceGenerator> codeState) throws IOException {
        for(JavaAnnotation annotation : annotations)
            writer
                    .append(codeState.getIndent())
                    .append(codeState.getSg().javaAnnotation(annotation))
                    .append(codeState.getSg().javaLineBreak());

        writer
                .append(codeState.getIndent())
                .append(codeState.getSg().javaVariableExpression(this));
    }
}
