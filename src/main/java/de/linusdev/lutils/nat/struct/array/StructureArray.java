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

package de.linusdev.lutils.nat.struct.array;

import de.linusdev.lutils.nat.abi.OverwriteChildABI;
import de.linusdev.lutils.nat.array.NativeArray;
import de.linusdev.lutils.nat.struct.abstracts.StructureStaticVariables;
import de.linusdev.lutils.nat.struct.annos.*;
import de.linusdev.lutils.nat.struct.generator.Language;
import de.linusdev.lutils.nat.struct.generator.StaticGenerator;
import de.linusdev.lutils.nat.struct.generator.StructCodeGenerator;
import de.linusdev.lutils.nat.struct.abstracts.Structure;
import de.linusdev.lutils.nat.abi.ABI;
import de.linusdev.lutils.nat.struct.info.StructureInfo;
import de.linusdev.lutils.nat.struct.mod.ModTrackingStructure;
import de.linusdev.lutils.nat.struct.utils.BufferUtils;
import de.linusdev.lutils.nat.struct.utils.SSMUtils;
import de.linusdev.lutils.nat.struct.utils.Utils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;
import java.util.Iterator;

@StructureSettings(
        requiresCalculateInfoMethod = true,
        customLengthOption = RequirementType.REQUIRED,
        customElementTypesOption = RequirementType.REQUIRED,
        customLayoutOption = RequirementType.OPTIONAL
)
public class StructureArray<T extends Structure> extends ModTrackingStructure implements NativeArray<T> {

    @SuppressWarnings("unused")
    public static final @NotNull StaticGenerator GENERATOR = new StaticGenerator() {

        @Override
        public @NotNull StructureInfo calculateInfo(
                @NotNull Class<?> selfClazz,
                @Nullable StructValue structValue,
                @NotNull StructValue @NotNull [] elementsStructValue,
                @NotNull ABI abi,
                @Nullable OverwriteChildABI overwriteChildAbi
        ) {
            assert structValue != null; // ensured through the @StructureSettings annotation

            StructureInfo elementInfo = SSMUtils.getInfo(
                    structValue.elementType()[0],
                    elementsStructValue.length == 0 ? null : elementsStructValue[0],
                    null,
                    abi,
                    overwriteChildAbi,
                    null,
                    null
            );

            StructureInfo info = abi.calculateArrayLayout(
                    false,
                    elementInfo,
                    structValue.length()[0],
                    -1
            );

            return new ArrayStructureInfo(
                    info.getAlignment(),
                    info.isCompressed(),
                    info.getRequiredSize(),
                    structValue.elementType()[0],
                    elementInfo,
                    structValue.length()[0]
            );
        }

        @Override
        public @NotNull StructCodeGenerator codeGenerator() {
            return new StructCodeGenerator() {
                @Override
                public @NotNull String getStructTypeName(@NotNull Language language, @NotNull Class<?> selfClazz, @NotNull StructureInfo info) {
                    ArrayStructureInfo arrayInfo = (ArrayStructureInfo) info;
                    StructureInfo elementInfo = arrayInfo.elementInfo;
                    StaticGenerator elementGenerator = SSMUtils.getGenerator(arrayInfo.elementClass, null);
                    //noinspection DataFlowIssue
                    return elementGenerator.codeGenerator().getStructTypeName(language, arrayInfo.elementClass, elementInfo) + "[]";
                }

                @Override
                public @NotNull String getStructVarDef(@NotNull Language language, @NotNull Class<?> selfClazz, @NotNull StructureInfo info, @NotNull String varName) {
                    ArrayStructureInfo arrayInfo = (ArrayStructureInfo) info;
                    StructureInfo elementInfo = arrayInfo.elementInfo;
                    StaticGenerator elementGenerator = SSMUtils.getGenerator(arrayInfo.elementClass, null);

                    //noinspection DataFlowIssue
                    return elementGenerator.codeGenerator().getStructTypeName(language, arrayInfo.elementClass, elementInfo) + " " + varName
                            + "[" + arrayInfo.length + "]" + language.lineEnding;
                }
            };
        }
    };

    /**
     * Creates an unallocated {@link StructureArray}, whose {@link #info} must be supplied when {@link #useBuffer(Structure, int, StructureInfo)}
     * is called. A call to {@link #allocate()} is not supported.
     * @param trackModifications see {@link #trackModifications}
     * @param creator see {@link #creator}
     * @return unallocated {@link StructureArray} as described above.
     * @param <T> element type
     * @see StructureStaticVariables#newUnallocated()
     */
    @SuppressWarnings("JavadocReference")
    public static <T extends Structure> @NotNull StructureArray<T> newUnallocated(
            boolean trackModifications,
            @NotNull ElementCreator<T> creator
    ) {
        return new StructureArray<>(trackModifications, creator);
    }

    /**
     * Creates an allocated {@link StructureArray}.
     * @param trackModifications see {@link #trackModifications}
     * @param structValue {@link StructValue} annotation supplying required information as described by {@link StructureArray}. See {@link SVWrapper}.
     * @param elementStructValue {@link StructValue} annotation supplying required information for each element. See {@link SVWrapper}.
     * @param creator  see {@link #creator}
     * @return allocated {@link StructureArray} as described above.
     * @param <T> element type
     * @see StructureStaticVariables#newAllocated(StructValue) 
     */
    public static <T extends Structure> @NotNull StructureArray<T> newAllocated(
            boolean trackModifications,
            @NotNull StructValue structValue,
            @Nullable StructValue elementStructValue,
            @NotNull ElementCreator<T> creator
    ) {
        StructureArray<T> struct = new StructureArray<>(
                trackModifications,
                structValue,
                elementStructValue,
                creator
        );
        struct.allocate();
        return struct;
    }

    /**
     * Creates an allocated {@link StructureArray}. Calls {@link #newAllocated(boolean, StructValue, StructValue, ElementCreator)}
     * with {@code trackModifications = false} and {@code elementStructValue = null}
     * @param creator  see {@link #creator}
     * @return allocated {@link StructureArray} as described above.
     * @param <T> element type
     * @see StructureStaticVariables#newAllocated(StructValue)
     */
    public static <T extends Structure> @NotNull StructureArray<T> newAllocated(
            int length,
            Class<T> elementClass,
            @NotNull ElementCreator<T> creator
    ) {
        return newAllocated(
                false,
                SVWrapper.of(length, elementClass),
                null,
                creator
        );
    }

    /**
     * Creates an allocatable {@link StructureArray}.
     * It can be allocated using {@link #allocate()} or {@link #claimBuffer(ByteBuffer)}
     * @param trackModifications see {@link #trackModifications}
     * @param structValue {@link StructValue} annotation supplying required information as described by {@link StructureArray}. See {@link SVWrapper}.
     * @param elementStructValue {@link StructValue} annotation supplying required information for each element. See {@link SVWrapper}.
     * @param creator  see {@link #creator}
     * @return allocatable {@link StructureArray} as described above.
     * @param <T> element type
     * @see StructureStaticVariables#newAllocatable(StructValue) 
     */
    public static <T extends Structure> @NotNull StructureArray<T> newAllocatable(
            boolean trackModifications,
            @NotNull StructValue structValue,
            @Nullable StructValue elementStructValue,
            @NotNull ElementCreator<T> creator
    ) {
        return new StructureArray<>(
                trackModifications,
                structValue,
                elementStructValue,
                creator
        );
    }

    /**
     * requires {@link BufferUtils#setByteBufferFromPointerMethod(BufferUtils.ByteBufferFromPointerMethod)} to be set.
     * @param trackModifications see {@link #trackModifications}
     * @param elementClazz clazz of array elements
     * @param length array length
     * @param pointer pointer to the native array
     * @param creator see {@link #creator}
     * @return {@link StructureArray} backed by given pointer
     * @param <T> array element type
     */
    @SuppressWarnings("unused") // Cannot be tested without native library
    public static <T extends Structure> @NotNull StructureArray<T> ofPointer(
            boolean trackModifications,
            @NotNull Class<?> elementClazz,
            int length,
            long pointer,
            @NotNull ElementCreator<T> creator
    ) {
        StructureArray<T> sArray = StructureArray.newAllocatable(
                trackModifications,
                SVWrapper.of(length, elementClazz),
                null,
                creator
        );

        sArray.claimBuffer(BufferUtils.getByteBufferFromPointer(pointer, sArray.getRequiredSize()));

        return sArray;
    }

    private final @NotNull ElementCreator<T> creator;

    private StructureInfo elementInfo;
    private Structure [] items;
    private int size;

    /**
     * @see #newUnallocated(boolean, ElementCreator) 
     */
    protected StructureArray(
            boolean trackModifications,
            @NotNull ElementCreator<T> creator
    ) {
        super(trackModifications);
        this.creator = creator;
    }

    /**
     * Creates an allocatable struct-array
     * @see #newAllocated(boolean, StructValue, StructValue, ElementCreator)
     * @see #newAllocatable(boolean, StructValue, StructValue, ElementCreator) 
     */
    protected StructureArray(
            boolean trackModifications,
            @NotNull StructValue structValue,
            @Nullable StructValue elementStructValue,
            @NotNull ElementCreator<T> creator
    ) {
        super(trackModifications);
        setInfo(SSMUtils.getInfo(
                this.getClass(),
                structValue,
                elementStructValue == null ? null : new ElementsStructValueWrapper(new StructValue[]{elementStructValue}),
                null,
                null,
                null,
                GENERATOR
        ));
        this.elementInfo = getInfo().elementInfo;
        this.size = getInfo().length;
        this.items = new Structure[size];
        this.creator = creator;
    }

    public void set(int index, @NotNull T struct) {
        items[index] = struct;
        callUseBufferOf(
                struct,
                this.mostParentStructure,
                this.offset + (elementInfo.getRequiredSize() * index),
                elementInfo
        );
    }

    @Override
    protected void useBuffer(
            @NotNull Structure mostParentStructure,
            int offset,
            @NotNull StructureInfo info
    ) {
        super.useBuffer(mostParentStructure, offset, info);
        ArrayStructureInfo aInfo = getInfo();

        this.elementInfo = aInfo.elementInfo;
        this.size = aInfo.length;
        this.items = new Structure[size];

    }

    @Override
    public @NotNull ArrayStructureInfo getInfo() {
        return (ArrayStructureInfo) super.getInfo();
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
            callUseBufferOf(
                    item,
                    this.mostParentStructure,
                    this.offset + (elementInfo.getRequiredSize() * index),
                    elementInfo
            );
            return (T) item;
        }

        return (T) items[index];
    }

    @SuppressWarnings("unchecked")
    @Override
    public T get(int index) {
        return (T) items[index];
    }

    @Override
    public @NotNull Iterator<T> iterator() {
        return new Iterator<>() {
            int index = 0;

            @Override
            public boolean hasNext() {
                return index < length();
            }

            @Override
            public T next() {
                return getOrCreate(index++);
            }
        };
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

        return toString("StructureArray<" + getInfo().elementClass.getSimpleName() + ">", sb.toString());
    }
}
