package de.linusdev.lutils.codegen;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public interface FileGenerator {
    void write(@NotNull Appendable writer) throws IOException;

    default @NotNull String writeToString() {
        StringBuilder sb = new StringBuilder();
        try {
            write(sb);
        } catch (IOException ignored) {}
        return sb.toString();
    }
}
