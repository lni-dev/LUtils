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

package de.linusdev.lutils.math.vector.buffer.byten;

import de.linusdev.lutils.math.vector.UnsignedVector;
import de.linusdev.lutils.nat.struct.abstracts.StructureStaticVariables;
import de.linusdev.lutils.nat.struct.annos.StructValue;
import org.jetbrains.annotations.Nullable;

public class BBUByte1 extends BBByte1 implements UnsignedVector {

    /**
     * @see StructureStaticVariables#newUnallocated()
     */
    public static BBUByte1 newUnallocated() {
        return new BBUByte1(false, null);
    }

    /**
     * @see StructureStaticVariables#newAllocatable(StructValue) 
     */
    public static BBUByte1 newAllocatable(@Nullable StructValue structValue) {
        return new BBUByte1(true, structValue);
    }

    /**
     * @see StructureStaticVariables#newAllocated(StructValue) 
     */
    public static BBUByte1 newAllocated(@Nullable StructValue structValue) {
        BBUByte1 ret = newAllocatable(structValue);
        ret.allocate();
        return ret;
    }

    protected BBUByte1(boolean generateInfo, @Nullable StructValue structValue) {
        super(generateInfo, structValue);
    }

}
