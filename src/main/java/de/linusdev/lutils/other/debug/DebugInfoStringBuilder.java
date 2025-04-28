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

package de.linusdev.lutils.other.debug;

import de.linusdev.lutils.other.str.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.RecordComponent;
import java.util.Map;
import java.util.Objects;

public class DebugInfoStringBuilder {

    private final @NotNull StringBuilder sb;
    private final @NotNull Object self;
    private final int maxDepth;

    public DebugInfoStringBuilder(@NotNull Object self, @NotNull String title, int maxDepth) {
        this.sb = new StringBuilder(title).append(": ");
        this.self = self;
        this.maxDepth = maxDepth;

        if(maxDepth <= 0)
            sb.append("[ Too deep, skipping... ]");
    }

    public DebugInfoStringBuilder addInformation(@NotNull String info) {
        if(maxDepth <= 0) return this;
        sb.append("\n * ").append(StringUtils.indent(info,  "   ", false));
        return this;
    }

    public DebugInfoStringBuilder addInformation(@NotNull String name, @Nullable Object value) {
        if(maxDepth <= 0) return this;
        name = name.replace('\n', ' ');
        sb.append("\n * ").append(name).append(": ").append(StringUtils.indent(Debuggable.getDebugInfoString(value, maxDepth-1),  "   ", false));
        return this;
    }

    public DebugInfoStringBuilder addList(@NotNull String name, @NotNull Iterable<?> items) {
        if(maxDepth <= 0) return this;
        sb.append("\n * ").append(name).append(": ");

        for (Object item : items) {
            sb.append("\n   - ").append(StringUtils.indent(Debuggable.getDebugInfoString(item, maxDepth-1),  "     ", false));
        }

        return this;
    }

    public DebugInfoStringBuilder addMap(@NotNull String name, @NotNull Map<?, ?> items) {
        if(maxDepth <= 0) return this;
        sb.append("\n * ").append(name).append(": ");

        for (var entry : items.entrySet()) {
            name = Objects.toString(entry.getValue()).replace('\n', ' ');
            sb.append("\n   - ").append(name).append(": ").append(StringUtils.indent(Debuggable.getDebugInfoString(entry.getValue(), maxDepth-1),  "     ", false));
        }

        return this;
    }

    public DebugInfoStringBuilder addRecordComponents() {
        if(maxDepth <= 0) return this;
        if(!self.getClass().isRecord())
            throw new IllegalArgumentException("Object is not a record!");

        try {
            String val;
            for (RecordComponent recordComponent : self.getClass().getRecordComponents()) {
                val = StringUtils.indent(
                        Debuggable.getDebugInfoString(recordComponent.getAccessor().invoke(self), maxDepth-1),
                        "   ", false
                );

                sb.append("\n * ").append(recordComponent.getName()).append(": ").append(val);
            }

        } catch (IllegalAccessException | InvocationTargetException e) {
           sb.append("\n *** Cannot read record data ***");
        }

        return this;
    }

    public @NotNull String build() {
        return sb.toString();
    }

}
