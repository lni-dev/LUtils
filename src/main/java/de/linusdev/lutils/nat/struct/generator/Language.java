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

package de.linusdev.lutils.nat.struct.generator;

import de.linusdev.lutils.nat.NativeType;
import org.jetbrains.annotations.NotNull;

public enum Language {
    OPEN_CL(";") {
        private final int[] suppPaddings = {1, 2, 4, 8, 16};

        @Override
        public @NotNull String getStartStructString(boolean packed, @NotNull String name) {
            return packed ?
                    "typedef struct __attribute__((packed)) {\n"
                    :
                    "typedef struct {\n";
        }

        @Override
        public @NotNull String getEndStructString(boolean packed, @NotNull String name) {
            return "} " + name + ";";
        }

        @Override
        public int addPadding(StringBuilder sb, int padding, int index) {
            switch (padding) {
                case 0:
                    return index;
                case 1: sb.append("byte padding").append(index).append(";\n"); break;
                case 2: sb.append("char padding").append(index).append(";\n"); break;
                case 4: sb.append("int padding").append(index).append(";\n"); break;
                case 8: sb.append("int2 padding").append(index).append(";\n"); break;
                case 16: sb.append("int4 padding").append(index).append(";\n"); break;
                default:
                    for(int i = suppPaddings.length-1; i >= 0; i--) {
                        int pad = suppPaddings[i];
                        if(padding - pad > 0) {
                            padding -= pad;
                            index = addPadding(sb, pad, index);
                            return addPadding(sb, padding, index);
                        }
                    }
            }
            return index + 1;
        }

        @Override
        public @NotNull String getNativeTypeName(@NotNull NativeType type) {
            return switch (type) {
                case INT8 -> "char";
                case INT16 -> "short";
                case INT32 -> "int";
                case INT64 -> "long";
                case FLOAT32 -> "float";
                case FLOAT64 -> "double";
                case POINTER -> "void*";
                default -> throw new IllegalArgumentException();
            };
        }
    },
    ;

    public final @NotNull String lineEnding;

    Language(@NotNull String lineEnding) {
        this.lineEnding = lineEnding;
    }

    public abstract @NotNull String getStartStructString(boolean packed, @NotNull String name);

    public abstract @NotNull String getEndStructString(boolean packed, @NotNull String name);

    public abstract int addPadding(StringBuilder sb, int padding, int index);

    public abstract @NotNull String getNativeTypeName(@NotNull NativeType type);
}
