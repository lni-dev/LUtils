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

package de.linusdev.lutils.math.matrix.buffer;

import de.linusdev.lutils.math.matrix.Matrix;
import de.linusdev.lutils.math.matrix.MatrixMemoryLayout;
import de.linusdev.lutils.math.vector.buffer.BBVectorInfo;
import de.linusdev.lutils.nat.NativeType;
import de.linusdev.lutils.nat.abi.ABI;
import de.linusdev.lutils.nat.struct.abstracts.Structure;
import de.linusdev.lutils.nat.struct.annos.RequirementType;
import de.linusdev.lutils.nat.struct.generator.Language;
import de.linusdev.lutils.nat.struct.generator.SimpleStaticGenerator;
import de.linusdev.lutils.nat.struct.generator.StructCodeGenerator;
import de.linusdev.lutils.nat.struct.info.ArrayInfo;
import de.linusdev.lutils.nat.struct.info.StructureInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class BBMatrix extends Structure implements Matrix {

    protected final @NotNull BBMatrixGenerator generator;
    protected ArrayInfo.ArrayPositionFunction positions;
    protected @NotNull MatrixMemoryLayout memoryLayout = MatrixMemoryLayout.ROW_MAJOR;

    protected BBMatrix(@NotNull BBMatrixGenerator generator, @Nullable ABI abi, boolean genInfo) {
        super(abi);
        this.generator = generator;
        if(genInfo)
            setInfo(getInfoOrFail());
    }

    /**
     * position of given {@code x} and {@code y} in {@link #nativeMem}
     */
    protected long posInBuf(int y, int x) {
        return positions.position(positionToIndex(y, x));
    }

    /**
     * position of given {@code index} in {@link #nativeMem}
     */
    protected long posInBuf(int index) {
        return positions.position(index);
    }

    @Override
    protected @Nullable StructureInfo generateInfo() {
        return generator.calculateInfo(this.getClass(), abi, null, null);
    }

    @Override
    public @NotNull BBMatrixGenerator getGenerator() {
        return generator;
    }

    @Override
    protected void onSetInfo(@NotNull StructureInfo info) {
        super.onSetInfo(info);

        positions = ((BBVectorInfo) info).getPositions();
    }

    @Override
    public @NotNull BBMatrixInfo getInfo() {
        return (BBMatrixInfo) super.getInfo();
    }

    @Override
    public int getWidth() {
        return getInfo().getWidth();
    }

    @Override
    public int getHeight() {
        return getInfo().getHeight();
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
    public @NotNull MatrixMemoryLayout getMemoryLayout() {
        return memoryLayout;
    }

    @Override
    public void setMemoryLayout(@NotNull MatrixMemoryLayout memoryLayout) {
        this.memoryLayout = memoryLayout;
    }

    public static class BBMatrixGenerator extends SimpleStaticGenerator {

        private final int width;
        private final int height;
        private final @NotNull NativeType type;

        public BBMatrixGenerator(int width, int height, @NotNull NativeType type) {
            super(RequirementType.NOT_SUPPORTED, RequirementType.NOT_SUPPORTED);
            this.width = width;
            this.height = height;
            this.type = type;
        }

        @Override
        public @NotNull StructureInfo calculateInfoChecked(@NotNull Class<?> selfClazz, @NotNull ABI abi, int[] length, @NotNull Class<?>[] elementTypes) {
            return BBMatrixInfo.create(abi, width, height, type);
        }

        @Override
        public @Nullable StructCodeGenerator codeGenerator() {
            return new StructCodeGenerator() {
                @Override
                public @NotNull String getStructTypeName(@NotNull Language language, @NotNull Class<?> selfClazz, @NotNull StructureInfo info) {
                    BBMatrixInfo bbInfo = (BBMatrixInfo) info;
                    return language.getNativeTypeName(bbInfo.getType()) + bbInfo.getHeight() + "x" + bbInfo.getWidth();
                }

                @Override
                public @NotNull String getStructVarDef(
                        @NotNull Language language,
                        @NotNull Class<?> selfClazz,
                        @NotNull StructureInfo info,
                        @NotNull String varName
                ) {
                    BBMatrixInfo bbInfo = (BBMatrixInfo) info;
                    return language.getNativeTypeName(bbInfo.getType()) + " " + varName + "[" + (bbInfo.getHeight() * bbInfo.getWidth()) + "]"
                            + language.lineEnding;
                }
            };
        }
    }
}
