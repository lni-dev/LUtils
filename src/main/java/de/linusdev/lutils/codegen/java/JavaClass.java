package de.linusdev.lutils.codegen.java;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface JavaClass {

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
                return super.equals(obj);
            }
        };
    }

    static boolean equals(@NotNull JavaClass that, @NotNull JavaClass other) {
        if(!that.getPackage().equals(other.getPackage()))
            return false;

        if(!that.getName().equals(other.getName()))
            return false;

        return that.isPrimitive() == other.isPrimitive() && that.isArray() == other.isArray();
    }

    static int hashcode(@NotNull JavaClass that) {
        int result = that.getPackage().hashCode();
        result = 31 * result + that.getName().hashCode();
        result = 31 * result + Boolean.hashCode(that.isPrimitive());
        result = 31 * result + Boolean.hashCode(that.isArray());
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
    default @Nullable JavaImport getRequiredImport() {
        if(isPrimitive())
            return null;

        return new JavaImport(getPackage(), getName(), null);
    }

}
