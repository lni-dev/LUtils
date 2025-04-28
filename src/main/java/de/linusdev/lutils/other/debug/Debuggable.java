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

import de.linusdev.lutils.interfaces.Simplifiable;
import de.linusdev.lutils.other.iterator.IterableReflectionArray;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;

public abstract class Debuggable {

    private static @NotNull String getDebugInfoString(@Nullable Object object, int maxDepth, boolean allowSimplify) {
        if(object == null)
            return "null";
        if(object instanceof Debuggable.InfoString withInfoStr) return withInfoStr.debug_info_string(maxDepth);
        if(object instanceof String str) return str;
        if(object instanceof Number number) return number.toString();
        if(object instanceof Boolean bool) return bool.toString();

        if(object.getClass().isArray()) {
            return new DebugInfoStringBuilder(object, "Array of '" + object.getClass().getComponentType().getSimpleName() + "'", maxDepth)
                    .addInformation("length",  Array.getLength(object))
                    .addList("items", new IterableReflectionArray(object))
                    .build();
        }

        if(object instanceof List<?> list) {
            return new DebugInfoStringBuilder(list, list.getClass().getSimpleName(), maxDepth)
                    .addInformation("size", list.size())
                    .addList("items", list)
                    .build();
        }

        if(object instanceof Map<?,?> map) {
            return new DebugInfoStringBuilder(map, map.getClass().getSimpleName(), maxDepth)
                    .addInformation("size", map.size())
                    .addMap("entries", map)
                    .build();
        }

        if(object instanceof Record record) {
            return new DebugInfoStringBuilder(record, "Record '" + record.getClass().getSimpleName() + "'", maxDepth)
                    .addRecordComponents()
                    .build();
        }

        if(allowSimplify && object instanceof Simplifiable simple) {
            return getDebugInfoString(simple.simplify(), maxDepth, false);
        }

        return object + " [Used toString()]";
    }

    /**
     *
     * @param maxDepth how deep children should show. 0 = no children, 1 = children of given object, 2 = children of children of given object
     */
    static @NotNull String getDebugInfoString(@Nullable Object object, int maxDepth) {
        return getDebugInfoString(object, maxDepth, true);
    }

    interface InfoString {

        /**
         * Generates a human-readable string containing debug information about this object. Should have
         * the following form:
         * <pre>{@code
         * Object Title e.g. Class + Id/Name:
         *  * Some Text or value
         *  * Some more nice
         *    information (multiline)
         *  * A example List
         *    - item 1
         *    - item 2
         * }</pre>
         * @see DebugInfoStringBuilder
         */
        @NotNull String debug_info_string(int maxDepth);

        /**
         * @see #debug_info_string(int)
         */
        @SuppressWarnings("unused")
        default @NotNull String debug_info_string() {
            return debug_info_string(Integer.MAX_VALUE);
        }
    }

}
