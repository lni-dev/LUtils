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

package de.linusdev.lutils.math.vector.buffer.byten;

import de.linusdev.lutils.math.vector.Vector;
import de.linusdev.lutils.math.vector.abstracts.byten.ByteN;
import de.linusdev.lutils.math.vector.buffer.BBVector;
import de.linusdev.lutils.nat.abi.ABI;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class BBByteN extends BBVector implements ByteN {

    public BBByteN(@Nullable ABI abi, @NotNull BBVectorGenerator generator, boolean genInfo) {
        super(generator, abi, genInfo);
    }

    @Override
    public String toString() {
        return toString(
                ELEMENT_TYPE_NAME + getMemberCount(),
                Vector.toString(this, ELEMENT_TYPE_NAME, BBByteN::get)
        );
    }

    @Override
    public byte get(int index) {
        return nativeMem.getByte(posInBuf(index));
    }

    @Override
    public void put(int index, byte value) {
        nativeMem.setByte(posInBuf(index), value);
    }

}
