package de.linusdev.lutils.codegen.java;

import org.jetbrains.annotations.NotNull;

public interface JavaMethod {

    static @NotNull JavaMethod of(
            @NotNull JavaClass parentClass,
            @NotNull JavaClass returnType,
            @NotNull String name,
            boolean isStatic
    ) {
        return new JavaMethod() {
            @Override
            public @NotNull JavaClass getParentClass() {
                return parentClass;
            }

            @Override
            public @NotNull JavaClass getReturnType() {
                return returnType;
            }

            @Override
            public @NotNull String getName() {
                return name;
            }

            @Override
            public boolean isStatic() {
                return isStatic;
            }
        };
    }

    static @NotNull JavaMethod of(
            @NotNull Class<?> parentClass,
            @NotNull Class<?> returnType,
            @NotNull String name,
            boolean isStatic
    ) {
        return of(
                JavaClass.ofClass(parentClass),
                JavaClass.ofClass(returnType),
                name,
                isStatic
        );
    }

    @NotNull JavaClass getParentClass();

    @NotNull JavaClass getReturnType();

    @NotNull String getName();

    boolean isStatic();
}
