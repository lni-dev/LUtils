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
            switch (type) {
                case INT8:
                    return "char";
                case INT16:
                    return "short";
                case INT32:
                    return "int";
                case INT64:
                    return "long";
                case FLOAT32:
                    return "float";
                case FLOAT64:
                    return "double";
                case POINTER:
                    return "void*";
                default:
                    throw new IllegalArgumentException();
            }
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
