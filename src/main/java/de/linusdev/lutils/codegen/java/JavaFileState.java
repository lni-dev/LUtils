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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JavaFileState {

    private final @NotNull JavaPackage currentPackage;

    private final @NotNull List<JavaImport> imports = new ArrayList<>();


    public JavaFileState(
            @NotNull JavaPackage currentPackage
    ) {
        this.currentPackage = currentPackage;
    }

    public boolean addImport(@Nullable JavaImport toAdd) {
        if(toAdd == null)
            return false;

        // Classes in the same package don't have to be imported
        if(currentPackage.equals(toAdd.getPackage()))
            return false;

        // Check if This import is already present
        for(JavaImport jm : imports) {
            if(jm.contains(toAdd))
                return false;
        }

        return imports.add(toAdd);
    }

    @SuppressWarnings("UnusedReturnValue")
    public boolean addImport(@Nullable Collection<JavaImport> toAdd) {
        if(toAdd == null)
            return false;

        boolean added = false;

        for (JavaImport javaImport : toAdd) {
            added |= addImport(javaImport);
        }

        return added;
    }

    public @NotNull List<JavaImport> getImports() {
        return imports;
    }

}
