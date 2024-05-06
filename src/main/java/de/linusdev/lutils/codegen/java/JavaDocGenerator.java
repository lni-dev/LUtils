package de.linusdev.lutils.codegen.java;

import de.linusdev.lutils.codegen.GeneratorState;
import de.linusdev.lutils.codegen.PartGenerator;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class JavaDocGenerator implements PartGenerator<JavaSourceGeneratorHelper> {

    private final StringBuilder text = new StringBuilder();

    public void addText(@NotNull String text) {
        this.text.append(text);
    }

    public void addTextNl(@NotNull String text) {
        this.text.append(text).append("\n");
    }

    public void addAtText(@NotNull String at, @NotNull String text) {
        this.text.append("\n").append("@").append(at).append(" ").append(text).append("\n");
    }


    @Override
    public void write(@NotNull Appendable writer, @NotNull GeneratorState<JavaSourceGeneratorHelper> state) throws IOException {

        writer.append(state.getSg().javaLineBreak());
        writer.append(state.getIndent()).append("/**").append(state.getSg().javaLineBreak());

        String fin = text.toString();
        String[] lines = fin.split("\n");
        for (String line : lines) {
            writer
                    .append(state.getIndent())
                    .append(" * ")
                    .append(line)
                    .append(state.getSg().javaLineBreak());
        }

        writer.append(state.getIndent()).append(" */");
        writer.append(state.getSg().javaLineBreak());

    }
}
