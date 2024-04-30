package de.linusdev.lutils.codegen;

import de.linusdev.lutils.codegen.java.JavaFileGenerator;
import de.linusdev.lutils.codegen.java.JavaPackage;
import de.linusdev.lutils.codegen.java.JavaSourceGenerator;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class SourceGenerator implements JavaSourceGenerator {

    private final @NotNull Path sourceFolder;
    private final @NotNull List<JavaFileGenerator> javaFiles;

    public SourceGenerator(@NotNull Path sourceFolder) {
        this.sourceFolder = sourceFolder;
        this.javaFiles = new ArrayList<>();
    }

    public @NotNull JavaFileGenerator addJavaFile(
            String[] javaPackage
    ) {
        return new JavaFileGenerator(new JavaPackage(javaPackage), this);
    }
}
