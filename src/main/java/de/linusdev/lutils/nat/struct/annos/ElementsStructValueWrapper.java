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

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;

@SuppressWarnings("ClassExplicitlyAnnotation")
public class ElementsStructValueWrapper implements ElementsStructValue {

    private final @NotNull StructValue[] values;

    public ElementsStructValueWrapper(@NotNull StructValue[] values) {
        this.values = values;
    }

    @Override
    public StructValue[] value() {
        return values;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return ElementsStructValue.class;
    }
}
