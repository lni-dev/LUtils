package de.linusdev.lutils.codegen;

import de.linusdev.lutils.codegen.java.JavaFileGenerator;
import de.linusdev.lutils.codegen.java.JavaPackage;
import de.linusdev.lutils.codegen.java.JavaSourceGeneratorHelper;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class SourceGenerator {

    private final @NotNull JavaSourceGeneratorHelper sg = JavaSourceGeneratorHelper.getDefault();
    private final @NotNull Path sourceFolder;
    private final @NotNull List<JavaFileGenerator> javaFiles;

    public SourceGenerator(@NotNull Path sourceFolder) {
        this.sourceFolder = sourceFolder;
        this.javaFiles = new ArrayList<>();
    }

    public @NotNull JavaFileGenerator addJavaFile(
            String... javaPackage
    ) {
        var file = new JavaFileGenerator(new JavaPackage(javaPackage), sg);
        javaFiles.add(file);
        return file;
    }

    public @NotNull JavaFileGenerator addJavaFile(
            String javaPackage
    ) {
        var file = new JavaFileGenerator(new JavaPackage(javaPackage), sg);
        javaFiles.add(file);
        return file;
    }

    public @NotNull Path getJavaSourcePath() {
        return sourceFolder.resolve(sg.javaSourcePath());
    }

    public void write() throws IOException {
        if(!javaFiles.isEmpty()) {
            Path javaSourcePath = getJavaSourcePath();
            Files.createDirectories(javaSourcePath);

            for (JavaFileGenerator file : javaFiles) {
                Path javaFilePackagePath = javaSourcePath.resolve(file.getPackage().getPath());
                Path javaFilePath = javaFilePackagePath.resolve(file.getName() + ".java");

                Files.createDirectories(javaFilePackagePath);
                try(Writer writer = Files.newBufferedWriter(
                        javaFilePath,
                        StandardCharsets.UTF_8,
                        StandardOpenOption.TRUNCATE_EXISTING,
                        StandardOpenOption.CREATE
                )) {
                    file.write(writer);
                }
            }

        }
    }
}
