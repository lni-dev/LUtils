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

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class JavaPackage {

    public static @NotNull JavaPackage ofClass(@NotNull Class<?> clazz) {
        if(clazz.isArray())
            return ofClass(clazz.getComponentType());

        if(clazz.isPrimitive())
            throw new UnsupportedOperationException("Primitive types have no package.");

        return new JavaPackage(clazz.getPackageName());
    }

    private final @NotNull String @NotNull [] jPackage;

    public JavaPackage(@NotNull String @NotNull [] jPackage) {
        this.jPackage = jPackage;
    }

    public JavaPackage(@NotNull String jPackage) {
        this.jPackage = jPackage.split("\\.");
    }

    public @NotNull JavaPackage extend(@NotNull String @NotNull ... jPackage) {
        String[] newJPackage = new String[this.jPackage.length + jPackage.length];
        System.arraycopy(this.jPackage, 0, newJPackage, 0, this.jPackage.length);
        System.arraycopy(jPackage, 0, newJPackage, this.jPackage.length, jPackage.length);
        return new JavaPackage(newJPackage);
    }

    public @NotNull JavaPackage extend(@NotNull String jPackage) {
        JavaPackage extending = new JavaPackage(jPackage);
        return extend(extending.getArray());
    }

    public @NotNull String @NotNull [] getArray() {
        return jPackage;
    }

    public @NotNull String getPackageString() {
        StringBuilder sb = new StringBuilder();

        sb.append(jPackage[0]);

        for (int i = 1; i < jPackage.length; i++)
            sb.append(".").append(jPackage[i]);

        return sb.toString();
    }

    @Override
    public String toString() {
        return getPackageString();
    }

    public @NotNull Path getPath() {
        return Paths.get(jPackage[0], Arrays.copyOfRange(jPackage, 1, jPackage.length));
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;

        if(!(obj instanceof JavaPackage))
            return false;

        JavaPackage other = (JavaPackage) obj;

        return Arrays.equals(jPackage, other.jPackage);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(jPackage);
    }
}
