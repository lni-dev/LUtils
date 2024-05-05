package de.linusdev.lutils.codegen.java;

import de.linusdev.lutils.codegen.FileGenerator;
import de.linusdev.lutils.codegen.GeneratorState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public class JavaFileGenerator extends JavaClassGenerator implements FileGenerator, JavaClass {

    public JavaFileGenerator(
            @NotNull JavaPackage jPackage,
            @Nullable JavaSourceGeneratorHelper sourceGenerator) {
        super(
                new JavaFileState(jPackage),
                sourceGenerator == null ? JavaSourceGeneratorHelper.getDefault() : sourceGenerator,
                jPackage,
                null);
    }


    @Override
    public void write(@NotNull Appendable writer) throws IOException {
        GeneratorState<JavaSourceGeneratorHelper> state = new GeneratorState<>(sg.javaIndent(), sg);

        // Write Package
        writer
                .append(sg.javaPackageExpression(jPackage))
                .append(sg.javaLineBreak())
                .append(sg.javaLineBreak());

        // Write imports
        for(JavaImport jImport : ft.getImports())
            writer.append(sg.javaImportExpression(jImport)).append(sg.javaLineBreak());

        writer.append(sg.javaLineBreak());
        write(writer, state);

    }

    @Override
    public @NotNull JavaPackage getPackage() {
        return jPackage;
    }

    @Override
    public @NotNull String getName() {
        if(name == null)
            throw new IllegalStateException("Class name must not be null.");
        return name;
    }
}
