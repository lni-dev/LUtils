/*
 * Copyright (c) 2024-2025 Linus Andera
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
import de.linusdev.lutils.nat.abi.OverwriteChildABI;
import de.linusdev.lutils.nat.struct.abstracts.Structure;
import de.linusdev.lutils.nat.struct.abstracts.StructureStaticVariables;
import de.linusdev.lutils.nat.struct.annos.RequirementType;
import de.linusdev.lutils.nat.struct.annos.SVWrapper;
import de.linusdev.lutils.nat.struct.annos.StructValue;
import de.linusdev.lutils.nat.struct.annos.StructureSettings;
import de.linusdev.lutils.nat.struct.generator.StaticGenerator;
import de.linusdev.lutils.nat.struct.info.StructureInfo;
import de.linusdev.lutils.nat.struct.utils.SSMUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteOrder;

/**
 * Buffer backed int32 image. Stores pixel data row major in given {@link #pixelFormat} format
 * in little endian byte order.
 */
@StructureSettings(
        requiresCalculateInfoMethod = true,
        customLengthOption = RequirementType.REQUIRED
)
public class BBInt32Image extends Structure implements Image {

    public final static @NotNull StaticGenerator GENERATOR = new StaticGenerator() {
        @Override
        public @NotNull StructureInfo calculateInfo(
                @NotNull Class<?> selfClazz,
                @Nullable StructValue structValue,
                @NotNull StructValue @NotNull [] elementsStructValue,
                @NotNull ABI abi,
                @NotNull OverwriteChildABI overwriteChildAbi
        ) {
            assert structValue != null;
            int width = structValue.length()[0];
            int height = structValue.length()[1];


            return new BBImageInfo(4, false, width, height, 4, 0, 0);
        }
    };

    /**
     * @see StructureStaticVariables#newUnallocated()
     * @see #pixelFormat
     */
    public static @NotNull BBInt32Image newUnallocated(@NotNull PixelFormat<Integer> pixelFormat) {
        return new BBInt32Image(null, false, pixelFormat);
    }

    /**
     * @see StructureStaticVariables#newAllocatable(StructValue)
     * @see #pixelFormat
     */
    public static @NotNull BBInt32Image newAllocatable(@NotNull StructValue structValue, @NotNull PixelFormat<Integer> pixelFormat) {
        return new BBInt32Image(structValue, true, pixelFormat);
    }

    /**
     * @see StructureStaticVariables#newAllocatable(StructValue)
     * @see #pixelFormat
     */
    public static @NotNull BBInt32Image newAllocatable(@NotNull ImageSize size, @NotNull PixelFormat<Integer> pixelFormat) {
        return new BBInt32Image(SVWrapper.imageSize(size), true, pixelFormat);
    }

    /**
     * @see StructureStaticVariables#newAllocated(StructValue)
     * @see #pixelFormat
     */
    public static @NotNull BBInt32Image newAllocated(@NotNull StructValue structValue, @NotNull PixelFormat<Integer> pixelFormat) {
        return allocate(new BBInt32Image(structValue, true, pixelFormat));
    }

    private int width = 0;
    private int height = 0;
    private int pixelSize = 0;

    /**
     * {@link PixelFormat} to store the pixels in.
     */
    private final @NotNull PixelFormat<Integer> pixelFormat;

    protected BBInt32Image(
            @Nullable StructValue structValue,
            boolean generateInfo,
            @NotNull PixelFormat<Integer> pixelFormat
    ) {
        this.pixelFormat = pixelFormat;
        assert !generateInfo || structValue != null;

        if(generateInfo) {
            setInfo(SSMUtils.getInfo(
                    this.getClass(),
                    structValue,
                    null, null, null, null,
                    GENERATOR
            ));
        }
    }

    @Override
    protected void useBuffer(@NotNull Structure mostParentStructure, int offset, @NotNull StructureInfo info) {
        super.useBuffer(mostParentStructure, offset, info);
        byteBuf.order(ByteOrder.LITTLE_ENDIAN); // Byte order for this class is always little endian
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
        return pixelFormat.toR8G8B8A8_SRGB(fixEndianness(byteBuf.getInt((y * width + x) * pixelSize)));
    }

    public int fixEndianness(int rgba) {
        return Integer.reverseBytes(rgba);
    }

    @Override
    public void setPixelAsRGBA(int x, int y, int rgba) {
        assert x < width;
        assert y < height;
        byteBuf.putInt((y * getWidth() + x) * pixelSize, fixEndianness(pixelFormat.from(PixelFormat.R8G8B8A8_SRGB, rgba)));
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }
}
