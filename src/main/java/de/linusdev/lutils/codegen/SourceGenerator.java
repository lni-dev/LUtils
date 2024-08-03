package de.linusdev.lutils.codegen;

import de.linusdev.lutils.codegen.java.JavaFileGenerator;
import de.linusdev.lutils.codegen.java.JavaPackage;
import de.linusdev.lutils.codegen.java.JavaSourceGeneratorHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class SourceGenerator {

    public static class Test {

    }

    private final @NotNull JavaSourceGeneratorHelper sg = JavaSourceGeneratorHelper.getDefault();
    private final @NotNull Path sourceFolder;

    private @NotNull JavaPackage javaBasePackage = new JavaPackage(new String[]{});
    private final @NotNull List<JavaFileGenerator> javaFiles = new ArrayList<>();

    public SourceGenerator(@NotNull Path sourceFolder) {
        this.sourceFolder = sourceFolder;
    }

    public void setJavaBasePackage(@NotNull String javaBasePackage) {
        this.javaBasePackage = new JavaPackage(javaBasePackage);
    }

    public @NotNull JavaPackage getJavaBasePackage() {
        return javaBasePackage;
    }

    public @NotNull JavaFileGenerator addJavaFile(
            String... javaPackage
    ) {
        var file = new JavaFileGenerator(javaBasePackage.extend(javaPackage), sg);
        javaFiles.add(file);
        return file;
    }

    public @NotNull JavaSourceGeneratorHelper getSg() {
        return sg;
    }

    public @Nullable JavaFileGenerator getJavaFile(
            @NotNull String javaPackage,
            @NotNull String name
    ) {
        for (JavaFileGenerator javaFile : javaFiles) {
            if(javaFile.getName().equals(name) && javaFile.getPackage().getPackageString().equals(javaPackage))
                return javaFile;
        }

        return null;
    }

    public @NotNull JavaFileGenerator addJavaFile(
            @Nullable String javaPackage
    ) {
        if(javaPackage == null)
            return addJavaFile();

        var file = new JavaFileGenerator(javaBasePackage.extend(javaPackage), sg);
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
