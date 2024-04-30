package de.linusdev.lutils.codegen;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public interface PartGenerator<SG> {
    void write(@NotNull Appendable writer, @NotNull GeneratorState<SG> codeState) throws IOException;

    default @NotNull String writeToString(@NotNull GeneratorState<SG> codeState) {
        StringBuilder sb = new StringBuilder();
        try {
            write(sb, codeState);
        } catch (IOException ignored) {}
        return sb.toString();
    }
}
