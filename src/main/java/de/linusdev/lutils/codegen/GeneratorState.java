package de.linusdev.lutils.codegen;

import org.jetbrains.annotations.NotNull;

public class GeneratorState<SG> {

    private final @NotNull String indentString;
    private final @NotNull SG sg;

    private int indent = 0;

    public GeneratorState(@NotNull String indentString, @NotNull SG sg) {
        this.indentString = indentString;
        this.sg = sg;
    }

    public String getIndent() {
        return indentString.repeat(indent);
    }

    public void increaseIndent() {
        indent++;
    }

    public void decreaseIndent() {
        indent = Math.max(0, indent-1);
    }

    public @NotNull SG getSg() {
        return sg;
    }
}
