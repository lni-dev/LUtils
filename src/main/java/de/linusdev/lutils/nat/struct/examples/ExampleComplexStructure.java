/*
 * Copyright (c) 2025-2026 Linus Andera
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

package de.linusdev.lutils.nat.struct.examples;

import de.linusdev.lutils.math.vector.buffer.intn.BBInt1;
import de.linusdev.lutils.math.vector.buffer.intn.BBInt4;
import de.linusdev.lutils.nat.abi.ABI;
import de.linusdev.lutils.nat.struct.abstracts.ComplexStructure;
import de.linusdev.lutils.nat.struct.abstracts.StructureStaticVariables;
import de.linusdev.lutils.nat.struct.annos.StructValue;
import de.linusdev.lutils.nat.struct.array.StructureArray;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class ExampleComplexStructure extends ComplexStructure {

    /**
     * @see StructureStaticVariables#newUnallocated() 
     */
    public static @NotNull ExampleComplexStructure newUnallocated() {
        return new ExampleComplexStructure(null);
    }

    /**
     * @see StructureStaticVariables#newAllocatable(ABI, int[], Class[]) 
     */
    public static @NotNull ExampleComplexStructure newAllocatable(@Nullable ABI abi) {
        return new ExampleComplexStructure(abi);
    }

    /**
     * First element: A 32-bit integer.
     */
    @StructValue(0)
    public final  @NotNull BBInt1 someInt = BBInt1.newUnallocated();

    /**
     * Second element: An array of {@link BBInt4} with length {@code 5}.
     */
    @StructValue(value = 1, length = 5, elementType = BBInt4.class)
    public final @NotNull StructureArray<BBInt4> someIntVectorArray = StructureArray.newUnallocated(false, BBInt4::newUnallocated);

    public ExampleComplexStructure(@Nullable ABI abi) {
        super(abi, false);
        init(someInt, someIntVectorArray);
    }
}
