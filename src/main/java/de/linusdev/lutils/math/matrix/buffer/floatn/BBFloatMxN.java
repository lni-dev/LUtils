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

package de.linusdev.lutils.math.matrix.buffer.floatn;

import de.linusdev.lutils.math.matrix.Matrix;
import de.linusdev.lutils.math.matrix.abstracts.floatn.FloatMxN;
import de.linusdev.lutils.math.matrix.buffer.BBMatrixInfo;
import de.linusdev.lutils.struct.abstracts.Structure;
import de.linusdev.lutils.struct.generator.Language;
import de.linusdev.lutils.struct.generator.StaticGenerator;
import de.linusdev.lutils.struct.info.StructureInfo;
import org.jetbrains.annotations.NotNull;

import java.nio.FloatBuffer;

public abstract class BBFloatMxN extends Structure implements FloatMxN {

    @SuppressWarnings("unused")
    public static final @NotNull StaticGenerator GENERATOR = new StaticGenerator() {
        @Override
        public @NotNull String getStructTypeName(@NotNull Language language, @NotNull Class<?> selfClazz, @NotNull StructureInfo info) {
            BBMatrixInfo bbInfo = (BBMatrixInfo) info;
            return bbInfo.getElementTypeName() + bbInfo.getHeight() + "x" + bbInfo.getWidth();
        }

        @Override
        public @NotNull String getStructVarDef(
                @NotNull Language language,
                @NotNull Class<?> selfClazz,
                @NotNull StructureInfo info,
                @NotNull String varName
        ) {
            BBMatrixInfo bbInfo = (BBMatrixInfo) info;
            return bbInfo.getElementTypeName() + " " + varName + "[" + (bbInfo.getHeight() * bbInfo.getWidth()) + "]"
                    + language.lineEnding;
        }
    };

    protected FloatBuffer buf;

    public BBFloatMxN(boolean allocateBuffer) {
        if(allocateBuffer)
            allocate();
    }

    @Override
    public void useBuffer(@NotNull Structure mostParentStructure, int offset) {
        super.useBuffer(mostParentStructure, offset);
        buf = byteBuf.asFloatBuffer();
    }

    @Override
    public float get(int y, int x) {
        return buf.get(positionToIndex(y, x));
    }

    @Override
    public void put(int y, int x, float value) {
        buf.put(positionToIndex(y, x), value);
    }

    @Override
    public float get(int index) {
        return buf.get(index);
    }

    @Override
    public void put(int index, float value) {
        buf.put(index, value);
    }

    @Override
    public @NotNull Structure getStructure() {
        return this;
    }

    @Override
    public boolean isArrayBacked() {
        return false;
    }

    @Override
    public boolean isBufferBacked() {
        return true;
    }

    @Override
    public String toString() {
        return toString(
                ELEMENT_TYPE_NAME + getWidth() + "x" + getHeight(),
                Matrix.toString(this, ELEMENT_TYPE_NAME, BBFloatMxN::get)
        );
    }
}
