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

import de.linusdev.lutils.math.vector.buffer.longn.BBLong1;
import de.linusdev.lutils.nat.abi.ABI;
import de.linusdev.lutils.nat.struct.abstracts.StructureStaticVariables;
import org.jetbrains.annotations.Nullable;

/**
 * This class can store a 64 bit pointer.
 */
@SuppressWarnings("unused")
public class BBPointer64 extends BBLong1 implements Pointer64{
    /**
     * @see StructureStaticVariables#newUnallocated()
     */
    public static BBPointer64 newUnallocated()  {
        return new BBPointer64(null);
    }

    /**
     * @see StructureStaticVariables#newAllocatable(ABI, int[], Class[])  
     */
    public static BBPointer64 newAllocatable(@Nullable ABI abi)  {
        return new BBPointer64(abi);
    }

    protected BBPointer64(@Nullable ABI abi) {
        super(abi);
    }

    @Override
    public long get() {
        return super.get();
    }

    @Override
    public void set(long f) {
        super.set(f);
    }
}
