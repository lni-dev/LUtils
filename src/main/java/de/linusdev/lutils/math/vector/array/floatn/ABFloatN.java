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

package de.linusdev.lutils.math.vector.array.floatn;

import de.linusdev.lutils.math.vector.Vector;
import de.linusdev.lutils.math.vector.abstracts.floatn.FloatN;

public abstract class ABFloatN implements FloatN {

    protected final float[] array;

    protected final int arrayStartIndex;
    protected final int memberCount;

    public ABFloatN(float[] array, int arrayStartIndex, int memberCount) {
        this.array = array;
        this.arrayStartIndex = arrayStartIndex;
        this.memberCount = memberCount;
    }

    public ABFloatN(int memberCount) {
        this.array = new float[memberCount];
        this.arrayStartIndex = 0;
        this.memberCount = memberCount;
    }

    @Override
    public boolean isArrayBacked() {
        return true;
    }

    @Override
    public boolean isBufferBacked() {
        return false;
    }

    @Override
    public boolean isView() {
        return false;
    }

    @Override
    public float get(int index) {
        return array[arrayStartIndex + index];
    }

    @Override
    public void put(int index, float value) {
        array[arrayStartIndex + index] = value;
    }

    @Override
    public String toString() {
        return Vector.toString(this, ELEMENT_TYPE_NAME, ABFloatN::get);
    }
}
