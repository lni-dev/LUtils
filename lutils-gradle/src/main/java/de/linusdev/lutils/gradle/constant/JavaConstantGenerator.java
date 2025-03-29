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

package de.linusdev.lutils.gradle.constant;

import de.linusdev.lutils.codegen.SourceGenerator;
import de.linusdev.lutils.version.Version;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.Directory;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.SetProperty;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;

public class JavaConstantGenerator extends DefaultTask {

    @Input
    Property<Path> generatedSourceRoot = getProject().getObjects().property(Path.class)
            .convention(getProject().provider(() ->
                    getProject().getLayout()
                            .getBuildDirectory()
                            .getAsFile().get()
                            .toPath()
                            .resolve("generated/sources/constants/")
            ));

    @Input
    Property<String> basePackage = getProject().getObjects().property(String.class).convention("");

    @Input
    Property<String> clazzName = getProject().getObjects().property(String.class)
            .convention("GeneratedConstants");

    @Internal
    Property<SourceGenerator> sourceGenerator = getProject().getObjects().property(SourceGenerator.class)
            .convention(getProject().provider(() -> {
                        SourceGenerator gen = new SourceGenerator(generatedSourceRoot.get());
                        gen.setJavaBasePackage(basePackage.get());
                        return gen;
                    }
            ));

    @OutputDirectory
    DirectoryProperty generatedJavaSource = getProject().getObjects().directoryProperty()
            .convention(
                    getProject().getLayout().dir(
                            getProject().provider(
                                    () -> sourceGenerator.get().getJavaSourcePath().toFile()
                            )
                    )
            );

    @Input
    SetProperty<Constant> constants = getProject().getObjects().setProperty(Constant.class);

    @TaskAction
    void generate() throws IOException {

        if(constants.get().isEmpty()) return;

        SourceGenerator generator = sourceGenerator.get();

        var clazz = generator.addJavaFile();
        clazz.setName(clazzName.get());

        for (Constant constant : constants.get()) {
            constant.add(clazz);
        }

        generator.write();
    }

    public void add(@NotNull String name, @NotNull Version version) {
        constants.add(new Constant.VersionConst(name, version));
    }

    public void add(@NotNull String name, @NotNull String value) {
        constants.add(new Constant.StringConst(name, value));
    }

    public void add(@NotNull String name, @NotNull Path value) {
        constants.add(new Constant.PathConstant(name, value));
    }

    public String getBasePackage() {
        return basePackage.get();
    }

    public void setBasePackage(String basePackage) {
        this.basePackage.set(basePackage);
    }

    public String getClazzName() {
        return clazzName.get();
    }

    public SourceGenerator getSourceGenerator() {
        return sourceGenerator.get();
    }

    public void setGeneratedSourceRoot(Path generatedSourceRoot) {
        this.generatedSourceRoot.set(generatedSourceRoot);
    }

    public String getGeneratedSourceRoot() {
        return generatedSourceRoot.get().toString();
    }

    public Directory getGeneratedJavaSource() {
        return generatedJavaSource.get();
    }

    public SetProperty<Constant> getConstants() {
        return constants;
    }

    public void setClazzName(String clazzName) {
        this.clazzName.set(clazzName);
    }
}
