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

package de.linusdev.lutils.nat.struct.abstracts;

import de.linusdev.lutils.math.vector.buffer.intn.BBInt1;
import de.linusdev.lutils.math.vector.buffer.intn.BBInt4;
import de.linusdev.lutils.nat.struct.annos.StructValue;
import de.linusdev.lutils.nat.struct.array.StructureArray;
import org.jetbrains.annotations.NotNull;

public class TestStruct extends ComplexStructure {

    public final @StructValue @NotNull BBInt4 testIntVector = BBInt4.newUnallocated();
    public final @StructValue @NotNull BBInt1 testInt = BBInt1.newUnallocated();

    @StructValue(length = 1000, elementType = BBInt4.class)
    public final @NotNull StructureArray<BBInt4> array = StructureArray.newUnallocated(false, BBInt4::newUnallocated);

    public TestStruct(boolean trackModifications) {
        super(trackModifications);
    }
}