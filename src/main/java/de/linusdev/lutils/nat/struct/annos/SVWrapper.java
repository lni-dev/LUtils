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

package de.linusdev.lutils.nat.struct.annos;

import de.linusdev.lutils.nat.struct.abstracts.Structure;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;

@SuppressWarnings({"ClassExplicitlyAnnotation", "unused"})
public record SVWrapper(
        int @NotNull [] length,
        @NotNull Class<?> @NotNull [] elementType,
        @NotNull Class<?> overwriteStructureLayout
) implements StructValue {

    /**
     * @param length                   {@link StructValue#length()}
     * @param elementType              {@link StructValue#elementType()}
     * @param overwriteStructureLayout {@link StructValue#overwriteStructureLayout()}
     * @see StructValue
     */
    public static @NotNull SVWrapper of(
            int length,
            @NotNull Class<?> elementType,
            @Nullable Class<?> overwriteStructureLayout
    ) {
        return new SVWrapper(new int[]{length}, new Class[]{elementType}, overwriteStructureLayout);
    }

    /**
     * @param length      {@link StructValue#length()}
     * @param elementType {@link StructValue#elementType()}
     * @see StructValue
     */
    public static @NotNull SVWrapper of(
            int length,
            @NotNull Class<?> elementType
    ) {
        return new SVWrapper(new int[]{length}, new Class[]{elementType}, null);
    }

    /**
     * @param length {@link StructValue#length()}
     * @see StructValue
     */
    public static @NotNull SVWrapper length(
            int length
    ) {
        return new SVWrapper(new int[]{length}, null, null);
    }

    /**
     * @param width  {@link StructValue#length()}
     * @param height {@link StructValue#length()}
     * @see StructValue
     */
    public static @NotNull SVWrapper imageSize(
            int width,
            int height
    ) {
        return new SVWrapper(new int[]{width, height}, null, null);
    }

    /**
     * @param elementType {@link StructValue#elementType()}
     * @see StructValue
     */
    public static @NotNull SVWrapper elementType(
            @NotNull Class<?> elementType
    ) {
        return new SVWrapper(null, new Class[]{elementType}, null);
    }

    /**
     * @param overwriteStructureLayout {@link StructValue#overwriteStructureLayout()}
     * @see StructValue
     */
    public static @NotNull SVWrapper overwriteLayout(@Nullable Class<?> overwriteStructureLayout) {
        return new SVWrapper(null, null, overwriteStructureLayout);
    }

    public SVWrapper(
            int @Nullable [] length,
            Class<?> @Nullable [] elementType,
            @Nullable Class<?> overwriteStructureLayout
    ) {
        this.length = length == null ? new int[]{-1} : length;
        this.elementType = elementType == null ? new Class[]{Structure.class} : elementType;
        this.overwriteStructureLayout = overwriteStructureLayout == null ? Structure.class : overwriteStructureLayout;
    }

    @Override
    public int value() {
        return -1;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return StructValue.class;
    }
}
