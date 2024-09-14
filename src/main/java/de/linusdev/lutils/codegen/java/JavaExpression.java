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
import java.util.Arrays;
import java.util.Collection;

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

    static @NotNull JavaExpression javaAssert(
            @NotNull JavaExpression expression
    ) {
        return new JavaExpression() {
            @Override
            public @NotNull String getExprString(@NotNull JavaSourceGeneratorHelper sg) {
                return "assert " + expression.getExprString(sg);
            }

            @Override
            public @Nullable Collection<JavaImport> getRequiredImports() {
                return expression.getRequiredImports();
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

                if(variable.getType().getRequiredImports() != null)
                    imports.addAll(variable.getType().getRequiredImports());

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

    static @NotNull JavaExpression callConstructorOf(
            @NotNull JavaClass javaClass,
            @NotNull JavaExpression @NotNull ... parameters
    ) {
        return new JavaExpression() {
            @Override
            public @NotNull String getExprString(
                    @NotNull JavaSourceGeneratorHelper sg
            ) {
                return sg.javaMethodCall("new " + javaClass.getTypeName(), parameters);
            }

            @Override
            public @Nullable Collection<JavaImport> getRequiredImports() {
                return javaClass.getRequiredImports();
            }
        };
    }

    static @NotNull JavaExpression thisExpression() {
        return new JavaExpression() {

            @Override
            public @NotNull String getExprString(@NotNull JavaSourceGeneratorHelper sg) {
                return "this";
            }
        };
    }

    static @NotNull JavaExpression callMethod(
            @NotNull JavaMethod method,
            @NotNull JavaExpression @NotNull ... parameters
    ) {
        return new JavaExpression() {
            @Override
            public @NotNull String getExprString(@NotNull JavaSourceGeneratorHelper sg) {
                if(method.isStatic())
                    return sg.javaStaticMethodCall(method.getParentClass(), method.getName(), parameters);
                return sg.javaMethodCall(method.getName(), parameters);
            }

            @Override
            public @Nullable Collection<JavaImport> getRequiredImports() {
                if(method.isStatic() && method.getParentClass().getRequiredImports() != null) {
                    ArrayList<JavaImport> imports = new ArrayList<>(method.getParentClass().getRequiredImports());
                    imports.addAll(collectImports(parameters));
                    return imports;
                }
                return collectImports(parameters);
            }
        };
    }

    /**
     * Expression example: String.class
     */
    static @NotNull JavaExpression classInstanceOfClass(
            @NotNull JavaClass javaClass
    ) {
        return new JavaExpression() {
            @Override
            public @NotNull String getExprString(@NotNull JavaSourceGeneratorHelper sg) {
                return javaClass.getName() + ".class";
            }

            @Override
            public @Nullable Collection<JavaImport> getRequiredImports() {
                return javaClass.getRequiredImports();
            }
        };
    }

    static @NotNull JavaExpression publicStaticVariable(JavaVariable variable) {
        return new JavaExpression() {
            @Override
            public @NotNull String getExprString(@NotNull JavaSourceGeneratorHelper sg) {
                return variable.parentClass.getName() + "." + variable.getExprString(sg);
            }

            @Override
            public @Nullable Collection<JavaImport> getRequiredImports() {
                return variable.parentClass.getRequiredImports();
            }
        };
    }

    private static @NotNull Collection<JavaImport> collectImports(
            @NotNull JavaExpression @NotNull ... expressions
    ) {
        ArrayList<JavaImport> imports = new ArrayList<>();
        for (JavaExpression parameter : expressions) {
            if(parameter.getRequiredImports() != null)
                imports.addAll(parameter.getRequiredImports());
        }
        return imports;
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
