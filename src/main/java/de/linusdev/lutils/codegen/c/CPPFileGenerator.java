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
