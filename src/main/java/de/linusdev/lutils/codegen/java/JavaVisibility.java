package de.linusdev.lutils.codegen.java;

import org.jetbrains.annotations.NotNull;

public enum JavaVisibility {
    PUBLIC {
        @Override
        public @NotNull String getName(@NotNull JavaSourceGenerator sg) {
            return sg.javaPublic();
        }
    },
    PRIVATE {
        @Override
        public @NotNull String getName(@NotNull JavaSourceGenerator sg) {
            return sg.javaPrivate();
        }
    },
    PROTECTED {
        @Override
        public @NotNull String getName(@NotNull JavaSourceGenerator sg) {
            return sg.javaProtected();
        }
    },
    PACKAGE_PRIVATE {
        @Override
        public @NotNull String getName(@NotNull JavaSourceGenerator sg) {
            return sg.javaPackagePrivate();
        }
    },
    ;

    public abstract @NotNull String getName(@NotNull JavaSourceGenerator sg);
}
