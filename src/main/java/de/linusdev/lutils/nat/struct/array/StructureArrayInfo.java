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

package de.linusdev.lutils.nat.struct.array;

import de.linusdev.lutils.nat.struct.info.ArrayInfo;
import de.linusdev.lutils.nat.struct.info.StructureInfo;
import org.jetbrains.annotations.NotNull;

public class StructureArrayInfo extends ArrayInfo {

    protected final @NotNull Class<?> elementClass;
    protected final @NotNull StructureInfo elementInfo;

    public StructureArrayInfo(
            int alignment,
            boolean compressed,
            int size,
            int[] sizes,
            int length,
            int stride,
            @NotNull ArrayInfo.ArrayPositionFunction positions,
            @NotNull Class<?> elementClass,
            @NotNull StructureInfo elementInfo
    ) {
        super(alignment, compressed, size, sizes, length, stride, positions);

        this.elementClass = elementClass;
        this.elementInfo = elementInfo;
    }

    public @NotNull Class<?> getElementClass() {
        return elementClass;
    }

    public @NotNull StructureInfo getElementInfo() {
        return elementInfo;
    }
}
