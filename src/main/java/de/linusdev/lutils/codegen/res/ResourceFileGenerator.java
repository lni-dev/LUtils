/*
 * Copyright (c) 2025 Linus Andera
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

package de.linusdev.lutils.codegen.res;

import de.linusdev.lutils.codegen.FileGenerator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ResourceFileGenerator implements FileGenerator {

    private @NotNull String path = "";
    private @Nullable String name;
    private @Nullable String fileEnding;

    private final @NotNull List<String> content = new ArrayList<>();

    public void addContent(@NotNull String content) {
        this.content.add(content);
    }

    public void setName(@Nullable String name) {
        this.name = name;
    }

    public void setPath(@NotNull String path) {
        this.path = path;
    }

    public void setFileEnding(@Nullable String fileEnding) {
        this.fileEnding = fileEnding;
    }

    public @Nullable String getName() {
        return name;
    }

    public @NotNull String getPath() {
        return path;
    }

    public @Nullable String getFileEnding() {
        return fileEnding;
    }

    @Override
    public void write(@NotNull Appendable writer) throws IOException {
        for (String content : this.content) {
            writer.append(content);
        }
    }
}
