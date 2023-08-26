package de.linusdev.lutils.struct.generator;

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
        public @NotNull String getPrimitiveElementTypeName(@NotNull Class<?> primitiveClass) {
            if(primitiveClass.equals(Integer.class)) return "int";
            else if(primitiveClass.equals(Float.class)) return "float";
            else if(primitiveClass.equals(Double.class)) return "double";
            else if(primitiveClass.equals(Short.class)) return "short";
            else if(primitiveClass.equals(Long.class)) return "long";
            else if(primitiveClass.equals(Byte.class) || primitiveClass.equals(Character.class)) return "char";
            else throw new IllegalArgumentException("primitive type " + primitiveClass.getSimpleName() + "is not supported");
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

    public abstract @NotNull String getPrimitiveElementTypeName(@NotNull Class<?> primitiveClass);
}
