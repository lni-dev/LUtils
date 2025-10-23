/*
 * Copyright (c) 2024-2025 Linus Andera
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.linusdev.lutils.codegen;

import de.linusdev.lutils.codegen.c.CPPFileGenerator;
import de.linusdev.lutils.codegen.java.JavaFileGenerator;
import de.linusdev.lutils.codegen.java.JavaPackage;
import de.linusdev.lutils.codegen.java.JavaSourceGeneratorHelper;
import de.linusdev.lutils.codegen.res.ResourceFileGenerator;
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
    private final @NotNull List<CPPFileGenerator> cppFiles = new ArrayList<>();
    private final @NotNull List<ResourceFileGenerator> resFiles = new ArrayList<>();

    public SourceGenerator(@NotNull Path sourceFolder) {
        this.sourceFolder = sourceFolder;
    }

    public void setJavaBasePackage(@NotNull String javaBasePackage) {
        this.javaBasePackage = new JavaPackage(javaBasePackage);
    }

    public @NotNull JavaPackage getJavaBasePackage() {
        return javaBasePackage;
    }

    public @NotNull CPPFileGenerator addCFile() {
        var file = new CPPFileGenerator();
        cppFiles.add(file);
        return file;
    }

    public @NotNull ResourceFileGenerator addResourceFile(@NotNull String path, @NotNull String name, @NotNull String fileEnding) {
        var file = new ResourceFileGenerator();
        file.setName(name);
        file.setFileEnding(fileEnding);
        file.setPath(path);
        resFiles.add(file);
        return file;
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

    public @NotNull Path getCppSourcePath() {
        return sourceFolder.resolve("cpp");
    }

    public @NotNull Path getResourcePath() {
        return sourceFolder.resolve("resources");
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
        
        if(!cppFiles.isEmpty()) {
            Path cppSourcePath = getCppSourcePath();
            Files.createDirectories(cppSourcePath);

            for (CPPFileGenerator file : cppFiles) {
                Path cppFilePackagePath = cppSourcePath.resolve(file.getPath());
                Path cppFilePath = cppFilePackagePath.resolve(file.getName() + "." + file.getType().getFileEnding());

                Files.createDirectories(cppFilePackagePath);
                try(Writer writer = Files.newBufferedWriter(
                        cppFilePath,
                        StandardCharsets.UTF_8,
                        StandardOpenOption.TRUNCATE_EXISTING,
                        StandardOpenOption.CREATE
                )) {
                    file.write(writer);
                }
            }


        }

        if(!resFiles.isEmpty()) {
            Path resPath = getResourcePath();
            Files.createDirectories(resPath);

            for (ResourceFileGenerator file : resFiles) {
                Path resFilePackagePath = resPath.resolve(file.getPath());
                Path resFilePath = resFilePackagePath.resolve(file.getName() + "." + file.getFileEnding());

                Files.createDirectories(resFilePackagePath);
                try(Writer writer = Files.newBufferedWriter(
                        resFilePath,
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
