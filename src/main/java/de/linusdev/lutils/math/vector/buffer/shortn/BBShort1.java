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

package de.linusdev.lutils.math.vector.buffer.shortn;

import de.linusdev.lutils.math.vector.abstracts.shortn.Short1;
import de.linusdev.lutils.nat.struct.abstracts.StructureStaticVariables;
import de.linusdev.lutils.nat.struct.annos.StructValue;
import org.jetbrains.annotations.Nullable;

public class BBShort1 extends BBShortN implements Short1 {

    public static final BBVectorGenerator GENERATOR = new BBVectorGenerator(MEMBER_COUNT, ELEMENT_NATIVE_TYPE);

    /**
     * @see StructureStaticVariables#newUnallocated()
     */
    public static BBShort1 newUnallocated() {
        return new BBShort1(false, null);
    }

    /**
     * @see StructureStaticVariables#newAllocatable(StructValue) 
     */
    public static BBShort1 newAllocatable(@Nullable StructValue structValue) {
        return new BBShort1(true, structValue);
    }

    /**
     * @see StructureStaticVariables#newAllocated(StructValue) 
     */
    public static BBShort1 newAllocated(@Nullable StructValue structValue) {
        BBShort1 ret = newAllocatable(structValue);
        ret.allocate();
        return ret;
    }


    protected BBShort1(boolean generateInfo, @Nullable StructValue structValue) {
        super(GENERATOR, generateInfo, structValue);
    }
}
