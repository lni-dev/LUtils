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

package de.linusdev.lutils.image.buffer;

import de.linusdev.lutils.image.Image;
import de.linusdev.lutils.image.ImageSize;
import de.linusdev.lutils.image.PixelFormat;
import de.linusdev.lutils.nat.abi.ABI;
import de.linusdev.lutils.nat.struct.abstracts.Structure;
import de.linusdev.lutils.nat.struct.abstracts.StructureStaticVariables;
import de.linusdev.lutils.nat.struct.annos.RequirementType;
import de.linusdev.lutils.nat.struct.generator.SimpleStaticGenerator;
import de.linusdev.lutils.nat.struct.generator.StaticGenerator;
import de.linusdev.lutils.nat.struct.info.StructureInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteOrder;

/**
 * Buffer backed int32 image. Stores pixel data row major in given {@link #pixelFormat} format
 * in little endian byte order.
 */
@SuppressWarnings("IntegerMultiplicationImplicitCastToLong")
public class BBInt32Image extends Structure implements Image {

    public final static @NotNull StaticGenerator GENERATOR = new SimpleStaticGenerator(
            RequirementType.REQUIRED, RequirementType.NOT_SUPPORTED
    ) {
        @Override
        public @NotNull StructureInfo calculateInfoChecked(@NotNull Class<?> selfClazz, @NotNull ABI abi, int[] length, @NotNull Class<?>[] elementTypes) {
            return new BBImageInfo(abi, 4, false, length[0], length[1], 4, 0, 0);
        }
    };

    /**
     * @see StructureStaticVariables#newUnallocated()
     * @see #pixelFormat
     */
    public static @NotNull BBInt32Image newUnallocated(@NotNull PixelFormat<Integer> pixelFormat) {
        return new BBInt32Image(pixelFormat);
    }

    /**
     * @see StructureStaticVariables#newAllocatable(ABI, int[], Class[]) 
     * @see #pixelFormat
     */
    public static @NotNull BBInt32Image newAllocatable(@Nullable ABI abi, @NotNull PixelFormat<Integer> pixelFormat, int width, int height) {
        return new BBInt32Image(abi, pixelFormat, width, height);
    }

    /**
     * @see StructureStaticVariables#newAllocatable(ABI, int[], Class[]) 
     * @see #pixelFormat
     */
    public static @NotNull BBInt32Image newAllocatable(@Nullable ABI abi, @NotNull PixelFormat<Integer> pixelFormat, @NotNull ImageSize size) {
        return new BBInt32Image(abi, pixelFormat, size.getWidth(), size.getHeight());
    }

    private int width = 0;
    private int height = 0;
    private int pixelSize = 0;

    /**
     * {@link PixelFormat} to store the pixels in.
     */
    private final @NotNull PixelFormat<Integer> pixelFormat;

    protected BBInt32Image(@NotNull PixelFormat<Integer> pixelFormat) {
        super(null);
        this.pixelFormat = pixelFormat;
    }

    protected BBInt32Image(@Nullable ABI abi, @NotNull PixelFormat<Integer> pixelFormat, int width, int height) {
        super(abi);
        this.pixelFormat = pixelFormat;
        setInfo(GENERATOR.calculateInfo(this.getClass(), abi, new int[]{width, height}, null));
    }

    @Override
    protected void useBuffer(@NotNull Structure mostParentStructure, long offset, @NotNull StructureInfo info) {
        super.useBuffer(mostParentStructure, offset, info);
        assert nativeMem.byteOrder() == ByteOrder.LITTLE_ENDIAN; // Byte order for this class is always little endian
    }

    @Override
    protected @Nullable StaticGenerator getGenerator() {
        return GENERATOR;
    }

    @Override
    public @NotNull BBImageInfo getInfo() {
        return (BBImageInfo) super.getInfo();
    }

    @Override
    protected void onSetInfo(@NotNull StructureInfo info) {
        super.onSetInfo(info);
        this.width = getInfo().getWidth();
        this.height = getInfo().getHeight();
        this.pixelSize = getInfo().getPixelSize();
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
    public int getPixelAsRGBA(int x, int y) {
        assert x < width;
        assert y < height;
        return pixelFormat.toR8G8B8A8_SRGB(fixEndianness(nativeMem.getInt(( y * width + x) * pixelSize)));
    }

    public int fixEndianness(int rgba) {
        return Integer.reverseBytes(rgba);
    }

    @Override
    public void setPixelAsRGBA(int x, int y, int rgba) {
        assert x < width;
        assert y < height;
        nativeMem.setInt((y * getWidth() + x) * pixelSize, fixEndianness(pixelFormat.from(PixelFormat.R8G8B8A8_SRGB, rgba)));
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }
}
