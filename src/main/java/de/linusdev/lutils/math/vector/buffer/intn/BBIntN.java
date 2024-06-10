/*
 * Copyright (c) 2023 Linus Andera
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

import de.linusdev.lutils.math.vector.Vector;
import de.linusdev.lutils.math.vector.abstracts.intn.IntN;
import de.linusdev.lutils.math.vector.buffer.BBVector;
import de.linusdev.lutils.nat.struct.abstracts.Structure;
import de.linusdev.lutils.nat.struct.annos.StructValue;
import de.linusdev.lutils.nat.struct.info.StructureInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class BBIntN extends BBVector implements IntN {

    public BBIntN(
            @NotNull BBVectorGenerator generator,
            boolean generateInfo,
            @Nullable StructValue structValue
    ) {
        super(generator, generateInfo, structValue);
    }

    @Override
    public void useBuffer(
            @NotNull Structure mostParentStructure,
            int offset,
            @NotNull StructureInfo info
    ) {
        super.useBuffer(mostParentStructure, offset, info);
    }

    @Override
    public int get(int index) {
        return byteBuf.getInt(index);
    }

    @Override
    public void put(int index, int value) {
        byteBuf.putInt(index, value);
    }

    @Override
    public String toString() {
        return Vector.toString(this, ELEMENT_TYPE_NAME, BBIntN::get);
    }
}

