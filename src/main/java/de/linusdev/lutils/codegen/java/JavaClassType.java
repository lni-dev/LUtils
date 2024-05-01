package de.linusdev.lutils.codegen.java;

import org.jetbrains.annotations.NotNull;

public enum JavaClassType {
    ENUM {
        @Override
        public @NotNull String getName(@NotNull JavaSourceGeneratorHelper sg) {
            return sg.javaEnumName();
        }
    },
    CLASS {
        @Override
        public @NotNull String getName(@NotNull JavaSourceGeneratorHelper sg) {
            return sg.javaClassName();
        }
    },
    RECORD {
        @Override
        public @NotNull String getName(@NotNull JavaSourceGeneratorHelper sg) {
            return sg.javaRecordName();
        }
    },
    INTERFACE {
        @Override
        public @NotNull String getName(@NotNull JavaSourceGeneratorHelper sg) {
            return sg.javaInterfaceName();
        }
    },
    ;

    public abstract @NotNull String getName(@NotNull JavaSourceGeneratorHelper sg);
}
