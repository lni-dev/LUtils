package de.linusdev.lutils.codegen.java;

import de.linusdev.lutils.codegen.GeneratorState;
import de.linusdev.lutils.codegen.PartGenerator;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JavaBlockContents implements PartGenerator<JavaSourceGeneratorHelper> {

    protected final @NotNull JavaFileState ft;
    protected final @NotNull List<JavaExpression> expressions = new ArrayList<>();

    public JavaBlockContents(
            @NotNull JavaFileState ft
    ) {
        this.ft = ft;
    }

    public void addExpression(@NotNull JavaExpression expression) {
        expressions.add(expression);
    }

    @Override
    public void write(@NotNull Appendable writer, @NotNull GeneratorState<JavaSourceGeneratorHelper> codeState) throws IOException {
        codeState.increaseIndent();
        for (JavaExpression expression : expressions) {
            expression.write(writer, codeState);
            writer.append(codeState.getSg().javaExpressionEnd());
            writer.append(codeState.getSg().javaLineBreak());
        }
        codeState.decreaseIndent();
    }
}
