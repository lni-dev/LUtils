/*
 * Copyright (c) 2023-2024 Linus Andera
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

package de.linusdev.lutils.math.vector.buffer.floatn;


import de.linusdev.lutils.math.vector.abstracts.floatn.Float2;
import de.linusdev.lutils.nat.struct.abstracts.StructureStaticVariables;
import de.linusdev.lutils.nat.struct.annos.StructValue;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class BBFloat2 extends BBFloatN implements Float2 {

    public static final BBVectorGenerator GENERATOR = new BBVectorGenerator(MEMBER_COUNT, ELEMENT_NATIVE_TYPE);

    /**
     * @see StructureStaticVariables#newUnallocated()
     */
    public static BBFloat2 newUnallocated() {
        return new BBFloat2(false, null);
    }

    /**
     * @see StructureStaticVariables#newAllocatable(StructValue) 
     */
    public static BBFloat2 newAllocatable(@Nullable StructValue structValue) {
        return new BBFloat2(true, structValue);
    }

    /**
     * @see StructureStaticVariables#newAllocated(StructValue) 
     */
    public static BBFloat2 newAllocated(@Nullable StructValue structValue) {
        BBFloat2 ret = newAllocatable(structValue);
        ret.allocate();
        return ret;
    }


    protected BBFloat2(boolean generateInfo, @Nullable StructValue structValue) {
        super(GENERATOR, generateInfo, structValue);
    }
}
