package de.linusdev.lutils.codegen.java;

import de.linusdev.lutils.codegen.GeneratorState;
import de.linusdev.lutils.codegen.PartGenerator;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public interface JavaExpression extends PartGenerator<JavaSourceGenerator> {

    static @NotNull JavaExpression ofCode(@NotNull String code) {
        return new JavaExpression() {
            @Override
            public @NotNull String getExprString(@NotNull JavaSourceGenerator sg) {
                return code;
            }
        };
    }

    static @NotNull JavaExpression ofString(@NotNull String string) {
        return new JavaExpression() {
            @Override
            public @NotNull String getExprString(@NotNull JavaSourceGenerator sg) {
                return "\"" + string.replace("\"", "\\\"") + "\"";
            }
        };
    }

    @NotNull String getExprString(@NotNull JavaSourceGenerator sg);

    @Override
    default void write(@NotNull Appendable writer, @NotNull GeneratorState<JavaSourceGenerator> codeState) throws IOException {
        writer.append(codeState.getIndent()).append(getExprString(codeState.getSg()));
    }
}
