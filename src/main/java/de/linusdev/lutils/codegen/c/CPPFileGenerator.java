package de.linusdev.lutils.codegen.c;

import de.linusdev.lutils.codegen.FileGenerator;
import de.linusdev.lutils.codegen.GeneratorState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CPPFileGenerator implements FileGenerator {

    private @NotNull String path = "";
    private @Nullable String name;
    private @Nullable CPPFileType type;

    private final @NotNull List<String> includes = new ArrayList<>();
    private final @NotNull List<String> code = new ArrayList<>();

    public CPPFileGenerator() {

    }

    public void addCode(@NotNull String code) {
        this.code.add(code);
    }

    public void setName(@Nullable String name) {
        this.name = name;
    }

    public void setPath(@NotNull String path) {
        this.path = path;
    }

    public void setType(@Nullable CPPFileType type) {
        this.type = type;
    }

    public void addInclude(@NotNull String include) {
        if(includes.contains(include)) return;
        includes.add(include);
    }

    /**
     * path to folder in "src/cpp" containing this file.
     */
    public @NotNull String getPath() {
        return path;
    }

    public @Nullable String getName() {
        return name;
    }

    public @Nullable CPPFileType getType() {
        return type;
    }

    @Override
    public void write(@NotNull Appendable writer) throws IOException {
        GeneratorState<?> state = new GeneratorState<>("    ", new Object());

        // Write includes
        for (String include : includes) {
            writer.append("\n#include ");

            if(include.startsWith("<"))
                writer.append(include);
            else
                writer.append("\"").append(include).append("\"");
        }

        // Write code
        for (String c : code) {
            writer.append(c);
        }

    }
}
