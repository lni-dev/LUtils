/*
 * Copyright (c) 2024 Linus Andera
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
        ft.addImport(expression.getRequiredImports());
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
