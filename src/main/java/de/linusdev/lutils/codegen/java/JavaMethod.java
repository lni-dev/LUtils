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
