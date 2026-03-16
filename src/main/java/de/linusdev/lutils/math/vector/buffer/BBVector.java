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

package de.linusdev.lutils.math.vector.buffer;

import de.linusdev.lutils.math.vector.Vector;
import de.linusdev.lutils.nat.NativeType;
import de.linusdev.lutils.nat.abi.ABI;
import de.linusdev.lutils.nat.struct.abstracts.Structure;
import de.linusdev.lutils.nat.struct.annos.RequirementType;
import de.linusdev.lutils.nat.struct.generator.SimpleStaticGenerator;
import de.linusdev.lutils.nat.struct.generator.StructCodeGenerator;
import de.linusdev.lutils.nat.struct.info.ArrayInfo;
import de.linusdev.lutils.nat.struct.info.StructureInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class BBVector extends Structure implements Vector {

    protected final @NotNull BBVectorGenerator generator;
    protected ArrayInfo.ArrayPositionFunction positions;

    public BBVector(@NotNull BBVectorGenerator generator, @Nullable ABI abi) {
        super(abi);
        this.generator = generator;
    }

    /**
     * position of given {@code index} in {@link #nativeMem}
     */
    protected int posInBuf(int index) {
        return positions.position(index);
    }

    @Override
    protected @Nullable StructureInfo generateInfo() {
        return generator.calculateInfo(this.getClass(), abi, null, null);
    }

    @Override
    protected void onSetInfo(@NotNull StructureInfo info) {
        super.onSetInfo(info);

        positions = ((BBVectorInfo) info).getPositions();
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
    public @NotNull Structure getStructure() {
        return this;
    }

    @Override
    public boolean isView() {
        return false;
    }

    public static class BBVectorGenerator extends SimpleStaticGenerator {

        private final int elementCount;
        private final @NotNull NativeType type;

        public BBVectorGenerator(int elementCount, @NotNull NativeType type) {
            super(RequirementType.NOT_SUPPORTED, RequirementType.NOT_SUPPORTED);
            this.elementCount = elementCount;
            this.type = type;
        }


        @Override
        public @NotNull StructureInfo calculateInfoChecked(@NotNull Class<?> selfClazz, @NotNull ABI abi, int[] length, @NotNull Class<?>[] elementTypes) {
            return BBVectorInfo.create(abi, elementCount, type);
        }

        @Override
        public @NotNull StructCodeGenerator codeGenerator() {
            return (language, selfClazz, info) -> {
                BBVectorInfo bbInfo = (BBVectorInfo) info;

                return language.getNativeTypeName(bbInfo.getType()) + bbInfo.getLength();
            };
        }
    }

}
