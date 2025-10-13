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
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface JavaClass {

    static @NotNull JavaClass custom(
            @NotNull String jPackage,
            @NotNull String name
    ) {
        return new JavaClass() {
            @Override
            public @NotNull JavaPackage getPackage() {
                return new JavaPackage(jPackage);
            }

            @Override
            public @NotNull String getName() {
                return name;
            }

            @Override
            public boolean equals(Object obj) {
                if(!(obj instanceof JavaClass))
                    return false;

                return JavaClass.equals(this, (JavaClass) obj);
            }

            @Override
            public int hashCode() {
                return JavaClass.hashcode(this);
            }
        };
    }

    static @NotNull JavaClass ofClass(@NotNull Class<?> clazz) {
        JavaPackage p;

        if(clazz.isPrimitive() || (clazz.isArray() && clazz.getComponentType().isPrimitive()))
            p = null;
        else
            p = JavaPackage.ofClass(clazz);

        return new JavaClass() {

            private final @Nullable JavaPackage jPackage = p;

            @Override
            public @NotNull JavaPackage getPackage() {
                if(jPackage == null)
                    throw new UnsupportedOperationException("Primitive types have no package.");
                return jPackage;
            }

            @Override
            public @NotNull String getName() {
                return clazz.isArray() ? clazz.getComponentType().getSimpleName() : clazz.getSimpleName();
            }

            @Override
            public boolean isArray() {
                return clazz.isArray();
            }

            @Override
            public boolean isPrimitive() {
                return clazz.isArray() ? clazz.getComponentType().isPrimitive() : clazz.isPrimitive();
            }

            @Override
            public boolean equals(Object obj) {
                if(!(obj instanceof JavaClass))
                    return false;

                return JavaClass.equals(this, (JavaClass) obj);
            }

            @Override
            public int hashCode() {
                return JavaClass.hashcode(this);
            }
        };
    }

    static boolean equals(@NotNull JavaClass that, @NotNull JavaClass other) {
        if(!that.getPackage().equals(other.getPackage()))
            return false;

        if(!that.getName().equals(other.getName()))
            return false;

        if(!Arrays.equals(that.getGenerics(), other.getGenerics()))
            return false;

        return
                that.isPrimitive() == other.isPrimitive()
                        && that.isArray() == other.isArray()
                        && that.hasGenerics() == other.hasGenerics();
    }

    static int hashcode(@NotNull JavaClass that) {
        int result = that.getPackage().hashCode();
        result = 31 * result + that.getName().hashCode();
        result = 31 * result + Boolean.hashCode(that.isPrimitive());
        result = 31 * result + Boolean.hashCode(that.isArray());
        result = 31 * result + Boolean.hashCode(that.hasGenerics());
        return result;
    }

    /**
     *
     * @return {@link JavaPackage package}  of this class.
     * @throws UnsupportedOperationException if this is a primitive class
     */
    @NotNull JavaPackage getPackage();

    /**
     * Always without Array brackets.
     * @return class name.
     */
    @NotNull String getName();

    /**
     * @return "&lt;Class1, Class2, ...&gt;" or an empty string if {@link #hasGenerics()} is {@code false}
     */
    default @NotNull String getGenericsString() {
        if(hasGenerics()) {
            StringBuilder sb = new StringBuilder(getName());
            sb.append("<");

            boolean first = true;
            for(JavaClass generic : getGenerics()) {
                if(first) first = false;
                else sb.append(", ");
                sb.append(generic.getTypeName());
            }

            sb.append(">");

            return sb.toString();
        }

        return "";
    }

    default @NotNull String getTypeName() {
        if(isArray())
            return getName() + "[]";
        if(hasGenerics()) {
            StringBuilder sb = new StringBuilder(getName());
            sb.append("<");

            boolean first = true;
            for(JavaClass generic : getGenerics()) {
                if(first) first = false;
                else sb.append(", ");
                sb.append(generic.getTypeName());
            }

            sb.append(">");

            return sb.toString();
        }

        return getName();
    }

    /**
     * ordered array of generics this class has.
     */
    default @NotNull JavaClass @NotNull [] getGenerics() {
        return new JavaClass[0];
    }

    /**
     * Whether this class has a specific generic class. For example {@code List<String>}.
     */
    default boolean hasGenerics() {
        return false;
    }

    /**
     * Whether this represents a primitive type. (int, boolean, ...)
     */
    default boolean isPrimitive() {
        return false;
    }

    /**
     * Whether this represents an array type. (int, boolean, ...)
     */
    default boolean isArray() {
        return false;
    }

    /**
     *
     * @return the {@link JavaImport} required to use this {@link JavaClass} in code.
     */
    default @Nullable List<JavaImport> getRequiredImports() {
        if(isPrimitive())
            return null;

        List<JavaImport> imports = new ArrayList<>();

        imports.add(new JavaImport(getPackage(), getName(), null));

        for (JavaClass generic : getGenerics()) {
            if(generic.getRequiredImports() != null)
                imports.addAll(generic.getRequiredImports());
        }

        return imports;
    }

    default @NotNull JavaClass withGenerics(@NotNull JavaClass @NotNull ... generics) {
        if(hasGenerics())
            throw new IllegalStateException("This class already has generics set.");

        JavaClass old = this;

        return new JavaClass() {
            @Override
            public @NotNull JavaPackage getPackage() {
                return old.getPackage();
            }

            @Override
            public @NotNull String getName() {
                return old.getName();
            }

            @Override
            public boolean hasGenerics() {
                return true;
            }

            @Override
            public @NotNull JavaClass @NotNull [] getGenerics() {
                return generics;
            }

            @Override
            public boolean isArray() {
                return old.isArray();
            }

            @Override
            public boolean isPrimitive() {
                return old.isPrimitive();
            }
        };
    }

    default @Nullable Class<?> tryResolveActualClass() {
        try {
            return Class.forName(getPackage().getPackageString() + "." + getName());
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    default @NotNull JavaClass asArray() {
        JavaClass old = this;
        return new JavaClass() {
            @Override
            public @NotNull JavaPackage getPackage() {
                return old.getPackage();
            }

            @Override
            public @NotNull String getName() {
                return old.getName();
            }

            @Override
            public boolean hasGenerics() {
                return old.hasGenerics();
            }

            @Override
            public @NotNull JavaClass @NotNull [] getGenerics() {
                return old.getGenerics();
            }

            @Override
            public boolean isArray() {
                return true;
            }

            @Override
            public boolean isPrimitive() {
                return old.isPrimitive();
            }
        };
    }

}
