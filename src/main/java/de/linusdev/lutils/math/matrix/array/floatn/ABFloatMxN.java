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

package de.linusdev.lutils.math.matrix.array.floatn;

import de.linusdev.lutils.math.matrix.Matrix;
import de.linusdev.lutils.math.matrix.MatrixMemoryLayout;
import de.linusdev.lutils.math.matrix.abstracts.floatn.FloatMxN;
import org.jetbrains.annotations.NotNull;

public abstract class ABFloatMxN implements FloatMxN {

    protected final float[] array;
    protected @NotNull MatrixMemoryLayout memoryLayout = MatrixMemoryLayout.ROW_MAJOR;

    public ABFloatMxN() {
        this.array = new float[getWidth() * getHeight()];
    }

    @Override
    public float get(int y, int x) {
        return array[positionToIndex(y, x)];
    }

    @Override
    public void put(int y, int x, float value) {
        array[positionToIndex(y, x)] = value;
    }

    @Override
    public float get(int index) {
        return array[index];
    }

    @Override
    public void put(int index, float value) {
        array[index] = value;
    }

    @Override
    public boolean isArrayBacked() {
        return true;
    }

    @Override
    public float @NotNull [] getArray() {
        return array;
    }

    @Override
    public @NotNull MatrixMemoryLayout getMemoryLayout() {
        return memoryLayout;
    }

    @Override
    public void setMemoryLayout(@NotNull MatrixMemoryLayout memoryLayout) {
        this.memoryLayout = memoryLayout;
    }

    @Override
    public boolean isBufferBacked() {
        return false;
    }

    @Override
    public String toString() {
        return Matrix.toString(this, "ABFloat", ABFloatMxN::get);
    }
}
