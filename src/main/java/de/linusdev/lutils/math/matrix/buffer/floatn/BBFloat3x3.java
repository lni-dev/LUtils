/*
 * Copyright (c) 2024-2026 Linus Andera
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

package de.linusdev.lutils.math.matrix.buffer.floatn;

import de.linusdev.lutils.math.matrix.abstracts.floatn.Float3x3;
import de.linusdev.lutils.nat.abi.ABI;
import de.linusdev.lutils.nat.struct.abstracts.StructureStaticVariables;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BBFloat3x3 extends BBFloatMxN implements Float3x3 {

    public static final @NotNull BBMatrixGenerator GENERATOR = new BBMatrixGenerator(WIDTH, HEIGHT, ELEMENT_NATIVE_TYPE);

    /**
     * @see StructureStaticVariables#newUnallocated()
     */
    public static BBFloat3x3 newUnallocated() {
        return new BBFloat3x3(null, false);
    }

    /**
     * @see StructureStaticVariables#newAllocatable(ABI, int[], Class[])  
     */
    public static BBFloat3x3 newAllocatable(@Nullable ABI abi) {
        return new BBFloat3x3(abi, true);
    }

    protected BBFloat3x3(@Nullable ABI abi, boolean genInfo) {
        super(GENERATOR, abi, genInfo);
    }
}
