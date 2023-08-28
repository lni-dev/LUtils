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
import de.linusdev.lutils.struct.abstracts.Structure;
import org.jetbrains.annotations.NotNull;

import java.nio.IntBuffer;

public abstract class BBIntN extends BBVector implements IntN {

    protected IntBuffer buf;

    public BBIntN( boolean allocateBuffer) {
        if(allocateBuffer)
            allocate();
    }

    @Override
    public void useBuffer(@NotNull Structure mostParentStructure, int offset) {
        super.useBuffer(mostParentStructure, offset);
        buf = byteBuf.asIntBuffer();
    }

    @Override
    public int get(int index) {
        return buf.get(index);
    }

    @Override
    public void put(int index, int value) {
        buf.put(index, value);
    }

    @Override
    public String toString() {
        return Vector.toString(this, ELEMENT_TYPE_NAME, BBIntN::get);
    }
}

