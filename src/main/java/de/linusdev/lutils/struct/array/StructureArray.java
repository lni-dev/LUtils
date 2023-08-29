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

package de.linusdev.lutils.struct.array;

import de.linusdev.lutils.struct.abstracts.Structure;
import de.linusdev.lutils.struct.annos.FixedLength;
import de.linusdev.lutils.struct.annos.StructureSettings;
import de.linusdev.lutils.struct.generator.Language;
import de.linusdev.lutils.struct.generator.StaticGenerator;
import de.linusdev.lutils.struct.info.StructureInfo;
import de.linusdev.lutils.struct.mod.ModTrackingStructure;
import de.linusdev.lutils.struct.utils.SSMUtils;
import de.linusdev.lutils.struct.utils.Utils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@StructureSettings(requiresCalculateInfoMethod = true, requiresFixedLengthAnnotation = true)
public class StructureArray<T extends Structure> extends ModTrackingStructure implements NativeArray<T> {

    @SuppressWarnings("unused")
    public static final @NotNull ArrayStaticGenerator GENERATOR = new ArrayStaticGenerator();


    private final @NotNull StructureInfo elementInfo;
    private final @NotNull ArrayStructureInfo info;
    private final @NotNull ElementCreator<T> creator;

    private final Structure @NotNull [] items;
    private final int size;

    public StructureArray(
            boolean allocateBuffer,
            boolean trackModifications,
            @NotNull Class<T> elementClass,
            int size,
            @NotNull ElementCreator<T> creator
    ) {
        super(trackModifications);
        this.elementInfo = SSMUtils.getInfo(elementClass, null, null, null);
        this.size = size;
        this.items = new Structure[size];
        this.creator = creator;
        this.info = GENERATOR.calculateInfo(elementInfo, elementClass, size);

        if(allocateBuffer)
            allocate();
    }

    public void set(int index, @NotNull T struct) {
        items[index] = struct;
        callUseBufferOf(struct, this.mostParentStructure, this.offset + (elementInfo.getRequiredSize() * index));
    }

    @Override
    protected void useBuffer(@NotNull Structure mostParentStructure, int offset) {
        super.useBuffer(mostParentStructure, offset);
    }

    @Override
    public int length() {
        return size;
    }

    /**
     * Similar to {@link #get(int)}, but if the item at {@code index} is still {@code null},
     * a new item will be {@link ElementCreator#create() created}.
     * @see #get(int)
     */
    @SuppressWarnings("unchecked")
    public @NotNull T getOrCreate(int index) {
        if(items[index] == null) {
            Structure item = (items[index] = creator.create());
            callUseBufferOf(item, this.mostParentStructure, this.offset + (elementInfo.getRequiredSize() * index));
            return (T) item;
        }

        return (T) items[index];
    }

    @SuppressWarnings("unchecked")
    @Override
    public T get(int index) {
        return (T) items[index];
    }

    @NotNull
    @Override
    public ArrayStructureInfo getInfo() {
        return info;
    }

    @FunctionalInterface
    public interface ElementCreator<T extends Structure> {
        @NotNull T create();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("length=").append(length()).append("\n");
        sb.append("items={\n");

        int index = 0;
        for (Structure item : items) {
            sb.append(Utils.indent(index++ + ": " + item, "    ")).append(",\n");
        }

        sb.append("}");

        return toString("StructureArray<" + info.elementClass.getSimpleName() + ">", sb.toString());
    }

    public static class ArrayStaticGenerator implements StaticGenerator {
        @Override
        public @NotNull ArrayStructureInfo calculateInfo(@NotNull Class<?> selfClazz, @Nullable FixedLength fixedLength) {
            assert fixedLength != null; //required per annotation
            StructureInfo elementInfo = SSMUtils.getInfo(fixedLength.elementTypes()[0], null, null, null);
            return calculateInfo(elementInfo, fixedLength.elementTypes()[0], fixedLength.value()[0]);
        }

        public @NotNull ArrayStructureInfo calculateInfo(
                @NotNull StructureInfo elementInfo,
                @NotNull Class<?> elementClass,
                int length
        ) {
            return new ArrayStructureInfo(
                    elementInfo.getAlignment(), false,
                    elementInfo.getRequiredSize() * length,
                    elementClass,
                    length
            );
        }

        @Override
        public @NotNull String getStructTypeName(@NotNull Language language, @NotNull Class<?> selfClazz, @NotNull StructureInfo info) {
            ArrayStructureInfo arrayInfo = (ArrayStructureInfo) info;
            StructureInfo elementInfo = SSMUtils.getInfo(arrayInfo.elementClass, null, null, null);
            StaticGenerator elementGenerator = SSMUtils.getGenerator(arrayInfo.elementClass, null);
            return elementGenerator.getStructTypeName(language, arrayInfo.elementClass, elementInfo) + "[]";
        }

        @Override
        public @NotNull String getStructVarDef(@NotNull Language language, @NotNull Class<?> selfClazz, @NotNull StructureInfo info, @NotNull String varName) {
            ArrayStructureInfo arrayInfo = (ArrayStructureInfo) info;
            StructureInfo elementInfo = SSMUtils.getInfo(arrayInfo.elementClass, null, null, null);
            StaticGenerator elementGenerator = SSMUtils.getGenerator(arrayInfo.elementClass, null);

            return elementGenerator.getStructTypeName(language, arrayInfo.elementClass, elementInfo) + " " + varName
                    + "[" + arrayInfo.length + "]" + language.lineEnding;
        }
    }
}
