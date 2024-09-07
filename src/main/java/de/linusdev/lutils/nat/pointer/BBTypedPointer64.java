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

package de.linusdev.lutils.nat.pointer;

import de.linusdev.lutils.nat.NativeParsable;
import de.linusdev.lutils.nat.struct.abstracts.StructureStaticVariables;
import de.linusdev.lutils.nat.struct.annos.StructValue;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class BBTypedPointer64<T extends NativeParsable> extends BBPointer64 implements TypedPointer64<T> {

    /**
     * @see StructureStaticVariables#newUnallocated()
     */
    public static <T extends NativeParsable> BBTypedPointer64<T> newUnallocated1()  {
        return new BBTypedPointer64<>(false, null);
    }

    /**
     * @see StructureStaticVariables#newAllocatable(StructValue) 
     */
    public static <T extends NativeParsable> BBTypedPointer64<T> newAllocatable1(
            @Nullable StructValue structValue
    )  {
        return new BBTypedPointer64<>(true, structValue);
    }

    /**
     * @see StructureStaticVariables#newAllocated(StructValue) 
     */
    public static <T extends NativeParsable> BBTypedPointer64<T> newAllocated1(
            @Nullable StructValue structValue
    )  {
        BBTypedPointer64<T> ret = newAllocatable1(structValue);
        ret.allocate();
        return ret;
    }

    protected BBTypedPointer64(boolean generateInfo, @Nullable StructValue structValue) {
        super(generateInfo, structValue);
    }

}
