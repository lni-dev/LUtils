package de.linusdev.lutils.codegen.java;

import de.linusdev.lutils.codegen.GeneratorState;
import de.linusdev.lutils.codegen.PartGenerator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.*;

public interface JavaExpression extends PartGenerator<JavaSourceGeneratorHelper> {

    static @NotNull JavaExpression ofCode(
            @NotNull String code,
            @NotNull JavaImport @Nullable ... requiredImports
    ) {
        return new JavaExpression() {
            @Override
            public @NotNull String getExprString(@NotNull JavaSourceGeneratorHelper sg) {
                return code;
            }

            @Override
            public @Nullable Collection<JavaImport> getRequiredImports() {
                return requiredImports == null ? null : Arrays.asList(requiredImports);
            }
        };
    }

    static @NotNull JavaExpression ofString(@NotNull String string) {
        return new JavaExpression() {
            @Override
            public @NotNull String getExprString(@NotNull JavaSourceGeneratorHelper sg) {
                return "\"" + string.replace("\"", "\\\"") + "\"";
            }

            @Override
            public @Nullable Collection<JavaImport> getRequiredImports() {
                return null;
            }
        };
    }

    static @NotNull JavaExpression assign(
            @NotNull JavaAssignable variable,
            @NotNull JavaExpression expression
    ) {
        return new JavaExpression() {
            @Override
            public @NotNull String getExprString(@NotNull JavaSourceGeneratorHelper sg) {
                return variable.getName() + " " + sg.javaAssign() + " " + expression.getExprString(sg);
            }

            @Override
            public @Nullable Collection<JavaImport> getRequiredImports() {
                return expression.getRequiredImports();
            }
        };
    }

    static @NotNull JavaExpression declare(
            @NotNull JavaLocalVariable variable
    ) {

        return new JavaExpression() {
            @Override
            public @NotNull String getExprString(@NotNull JavaSourceGeneratorHelper sg) {
                return sg.javaDeclareLocalVariableExpression(variable);
            }

            @Override
            public @NotNull Collection<JavaImport> getRequiredImports() {
                int s = 0;
                if(variable.getDefaultValue() != null && variable.getDefaultValue().getRequiredImports() != null)
                    s += variable.getDefaultValue().getRequiredImports().size();
                ArrayList<JavaImport> imports = new ArrayList<>(s + 1);

                imports.add(variable.getType().getRequiredImport());
                if(s != 0)
                    imports.addAll(variable.getDefaultValue().getRequiredImports());

                return imports;
            }
        };
    }

    static @NotNull JavaExpression nullExpression() {
        return new JavaExpression() {
            @Override
            public @NotNull String getExprString(@NotNull JavaSourceGeneratorHelper sg) {
                return sg.javaNull();
            }
        };
    }

    static @NotNull JavaExpression numberPrimitive(@NotNull Number number) {
        return new JavaExpression() {
            @Override
            public @NotNull String getExprString(@NotNull JavaSourceGeneratorHelper sg) {
                return sg.javaNumberExpression(number);
            }
        };
    }

    static @NotNull JavaExpression booleanPrimitive(boolean bool) {
        return new JavaExpression() {
            @Override
            public @NotNull String getExprString(@NotNull JavaSourceGeneratorHelper sg) {
                return sg.javaBooleanExpression(bool);
            }
        };
    }

    static @NotNull JavaExpression callSuper(@NotNull JavaExpression @NotNull ... parameters) {
        return new JavaExpression() {
            @Override
            public @NotNull String getExprString(@NotNull JavaSourceGeneratorHelper sg) {
                return sg.javaMethodCall(sg.javaSuper(), parameters);
            }

            @Override
            public @NotNull Collection<JavaImport> getRequiredImports() {
                ArrayList<JavaImport> imports = new ArrayList<>();
                for (JavaExpression parameter : parameters) {
                    if(parameter.getRequiredImports() != null)
                        imports.addAll(parameter.getRequiredImports());
                }
                return imports;
            }
        };
    }

    static @NotNull JavaExpression returnExpr(@NotNull JavaExpression toReturn) {
        return new JavaExpression() {
            @Override
            public @NotNull String getExprString(@NotNull JavaSourceGeneratorHelper sg) {
                return "return " + toReturn.getExprString(sg);
            }

            @Override
            public @Nullable Collection<JavaImport> getRequiredImports() {
                return toReturn.getRequiredImports();
            }
        };
    }


    @NotNull String getExprString(@NotNull JavaSourceGeneratorHelper sg);

    default @Nullable Collection<JavaImport> getRequiredImports() {
        return null;
    }

    @Override
    default void write(@NotNull Appendable writer, @NotNull GeneratorState<JavaSourceGeneratorHelper> codeState) throws IOException {
        writer.append(codeState.getIndent()).append(getExprString(codeState.getSg()));
    }
}
