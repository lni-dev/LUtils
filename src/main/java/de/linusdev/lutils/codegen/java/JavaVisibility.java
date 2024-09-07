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

import org.jetbrains.annotations.NotNull;

public enum JavaVisibility {
    PUBLIC {
        @Override
        public @NotNull String getName(@NotNull JavaSourceGeneratorHelper sg) {
            return sg.javaPublic();
        }
    },
    PRIVATE {
        @Override
        public @NotNull String getName(@NotNull JavaSourceGeneratorHelper sg) {
            return sg.javaPrivate();
        }
    },
    PROTECTED {
        @Override
        public @NotNull String getName(@NotNull JavaSourceGeneratorHelper sg) {
            return sg.javaProtected();
        }
    },
    PACKAGE_PRIVATE {
        @Override
        public @NotNull String getName(@NotNull JavaSourceGeneratorHelper sg) {
            return sg.javaPackagePrivate();
        }
    },
    ;

    public abstract @NotNull String getName(@NotNull JavaSourceGeneratorHelper sg);
}
