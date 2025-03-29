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

package de.linusdev.lutils.math.vector.buffer.intn;

import de.linusdev.lutils.math.vector.UnsignedVector;
import de.linusdev.lutils.nat.struct.abstracts.StructureStaticVariables;
import de.linusdev.lutils.nat.struct.annos.StructValue;
import org.jetbrains.annotations.Nullable;

public class BBUInt2 extends BBInt2 implements UnsignedVector {

    /**
     * @see StructureStaticVariables#newUnallocated()
     */
    public static BBUInt2 newUnallocated() {
        return new BBUInt2(false, null);
    }

    /**
     * @see StructureStaticVariables#newAllocatable(StructValue)
     */
    public static BBUInt2 newAllocatable(@Nullable StructValue structValue) {
        return new BBUInt2(true, structValue);
    }

    /**
     * @see StructureStaticVariables#newAllocated(StructValue)
     */
    public static BBUInt2 newAllocated(@Nullable StructValue structValue) {
        BBUInt2 ret = newAllocatable(structValue);
        ret.allocate();
        return ret;
    }

    protected BBUInt2(boolean generateInfo, @Nullable StructValue structValue) {
        super(generateInfo, structValue);
    }
}
