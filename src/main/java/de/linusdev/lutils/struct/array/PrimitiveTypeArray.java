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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.function.IntFunction;
import java.util.function.ObjIntConsumer;

@SuppressWarnings("unused")
@StructureSettings(requiresCalculateInfoMethod = true, requiresFixedLengthAnnotation = true)
public class PrimitiveTypeArray<T> extends Structure implements NativeArray<T> {

    @SuppressWarnings("unused")
    public static final @NotNull PrimitiveArrayStaticGenerator GENERATOR = new PrimitiveArrayStaticGenerator();

    private final @NotNull ArrayStructureInfo info;

    private final int elementSize;
    private final int size;

    private final IntFunction<T> get;
    private final ObjIntConsumer<T> set;

    @SuppressWarnings("unchecked")
    public PrimitiveTypeArray(@NotNull Class<T> type, int size, boolean allocateBuffer) {

        if(type.isPrimitive()) {
            throw new IllegalArgumentException("You should pass the Class<> of the Wrapper not the primitive type. " +
                    "For example 'Integer.class', NOT 'int.class'");
        }

        try {
            Field bytes = type.getDeclaredField("BYTES");
            this.elementSize = bytes.getInt(null);
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException(type.getSimpleName() + " is not a primitive type or not supported!");
        } catch (IllegalAccessException e) {
            throw new Error(e);
        }
        
        this.size = size;

        if(type.equals(Integer.class)) {
            this.get = index -> (T) (Integer) byteBuf.getInt(index);
            this.set = (value, index) -> byteBuf.putInt(index, (int) value);

        } else if(type.equals(Float.class)) {
            this.get = index -> (T) (Float) byteBuf.getFloat(index);
            this.set = (value, index) -> byteBuf.putFloat(index, (float) value);

        } else if(type.equals(Double.class)) {
            this.get = index -> (T) (Double) byteBuf.getDouble(index);
            this.set = (value, index) -> byteBuf.putDouble(index, (double) value);

        } else if(type.equals(Short.class)) {
            this.get = index -> (T) (Short) byteBuf.getShort(index);
            this.set = (value, index) -> byteBuf.putShort(index, (short) value);

        } else if(type.equals(Long.class)) {
            this.get = index -> (T) (Long) byteBuf.getLong(index);
            this.set = (value, index) -> byteBuf.putLong(index, (long) value);

        } else if(type.equals(Byte.class)) {
            this.get = index -> (T) (Byte) byteBuf.get(index);
            this.set = (value, index) -> byteBuf.put(index, (byte) value);

        } else {
            throw new IllegalArgumentException("primitive type " + type.getSimpleName() + "is not supported");

        }

        this.info = GENERATOR.calculateInfo(elementSize, type, size);
        if(allocateBuffer)
            allocate();
    }

    private int toByteIndex(int index) {
        if(index >= size || index < 0)
            throw new IndexOutOfBoundsException(index);
        return index * elementSize;
    }

    public T get(int index) {
        return get.apply(toByteIndex(index));
    }

    public void set(int index, T value) {
        set.accept(value, toByteIndex(index));
    }

    @Override
    public int length() {
        return size;
    }

    public int getInt(int index) {
        return byteBuf.getInt(toByteIndex(index));
    }
    
    public float getFloat(int index) {
        return byteBuf.getFloat(toByteIndex(index));
    }
    
    public double getDouble(int index) {
        return byteBuf.getDouble(toByteIndex(index));
    }

    public long getLong(int index) {
        return byteBuf.getLong(toByteIndex(index));
    }

    public short getShort(int index) {
        return byteBuf.getShort(toByteIndex(index));
    }

    public byte getByte(int index) {
        return byteBuf.get(toByteIndex(index));
    }

    public void setInt(int index, int value) {
        byteBuf.putInt(toByteIndex(index), value);
    }

    public void setFloat(int index, float value) {
        byteBuf.putFloat(toByteIndex(index), value);
    }

    public void setDouble(int index, double value) {
        byteBuf.putDouble(toByteIndex(index), value);
    }

    public void setLong(int index, long value) {
        byteBuf.putLong(toByteIndex(index), value);
    }

    public void setShort(int index, short value) {
        byteBuf.putShort(toByteIndex(index), value);
    }

    public void setByte(int index, byte value) {
        byteBuf.put(toByteIndex(index), value);
    }

    public int size() {
        return size;
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {

            int index = 0;

            @Override
            public boolean hasNext() {
                return index < size;
            }

            @Override
            public T next() {
                return get(index++);
            }
        };
    }

    @NotNull
    @Override
    public StructureInfo getInfo() {
        return info;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("length=").append(length()).append("\n");
        sb.append("items={\n    ");

        for (T item : this) {
            sb.append(item).append(", ");
        }

        sb.append("\n}");

        return toString("StructureArray<" + info.elementClass.getSimpleName() + ">", sb.toString());
    }

    public static class PrimitiveArrayStaticGenerator implements StaticGenerator {
        @Override
        public @NotNull ArrayStructureInfo calculateInfo(@NotNull Class<?> selfClazz, @Nullable FixedLength fixedLength) {
            assert fixedLength != null; //required per annotation

            int elementSize;
            Class<?> type = fixedLength.elementTypes()[0];

            if(type.isPrimitive()) {
                throw new IllegalArgumentException("You should pass the Class<> of the Wrapper not the primitive type. " +
                        "For example 'Integer.class', NOT 'int.class'");
            }

            try {
                Field bytes = type.getDeclaredField("BYTES");
                elementSize = bytes.getInt(null);
            } catch (NoSuchFieldException e) {
                throw new IllegalArgumentException(type.getSimpleName() + " is not a primitive type or not supported!");
            } catch (IllegalAccessException e) {
                throw new Error(e);
            }


            return calculateInfo(elementSize, type, fixedLength.value()[0]);
        }

        public @NotNull ArrayStructureInfo calculateInfo(
                int elementSize,
                @NotNull Class<?> elementClass,
                int length
        ) {
            return new ArrayStructureInfo(
                    elementSize, false,
                    elementSize * length,
                    elementClass,
                    length
            );
        }

        @Override
        public @NotNull String getStructTypeName(@NotNull Language language, @NotNull Class<?> selfClazz, @NotNull StructureInfo info) {
            ArrayStructureInfo arrayInfo = (ArrayStructureInfo) info;
            return language.getPrimitiveElementTypeName(arrayInfo.elementClass) + "[]";
        }

        @Override
        public @NotNull String getStructVarDef(@NotNull Language language, @NotNull Class<?> selfClazz, @NotNull StructureInfo info, @NotNull String varName) {
            ArrayStructureInfo arrayInfo = (ArrayStructureInfo) info;
            return language.getPrimitiveElementTypeName(arrayInfo.elementClass) + " " + varName + "[" +  arrayInfo.length + "]"
                    + language.lineEnding;
        }
    }
}
