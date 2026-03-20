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

package de.linusdev.lutils.nat.pointer;

import de.linusdev.lutils.nat.NativeParsable;
import de.linusdev.lutils.nat.abi.ABI;
import de.linusdev.lutils.nat.struct.abstracts.StructureStaticVariables;
import org.jetbrains.annotations.Nullable;

public class BBTypedPointer64<T extends NativeParsable> extends BBPointer64 implements TypedPointer64<T> {

    /**
     * @see StructureStaticVariables#newUnallocated()
     */
    public static <T extends NativeParsable> BBTypedPointer64<T> newUnallocated1()  {
        return new BBTypedPointer64<>(null, false);
    }

    /**
     * @see StructureStaticVariables#newAllocatable(ABI, int[], Class[])  
     */
    public static <T extends NativeParsable> BBTypedPointer64<T> newAllocatable1(@Nullable ABI abi)  {
        return new BBTypedPointer64<>(abi, true);
    }

    protected BBTypedPointer64(@Nullable ABI abi, boolean genInfo) {
        super(abi, genInfo);
    }

}
