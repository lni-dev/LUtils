/*
 * Copyright (c) 2024 Linus Andera
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
import de.linusdev.lutils.nat.abi.ABI;
import de.linusdev.lutils.nat.abi.OverwriteChildABI;
import de.linusdev.lutils.nat.struct.abstracts.Structure;
import de.linusdev.lutils.nat.struct.annos.RequirementType;
import de.linusdev.lutils.nat.struct.annos.StructValue;
import de.linusdev.lutils.nat.struct.annos.StructureSettings;
import de.linusdev.lutils.nat.struct.generator.StaticGenerator;
import de.linusdev.lutils.nat.struct.info.StructureInfo;
import de.linusdev.lutils.nat.struct.utils.SSMUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@StructureSettings(
        requiresCalculateInfoMethod = true,
        customLengthOption = RequirementType.REQUIRED
)
public class BufferBackedImage extends Structure implements Image {

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

    private int width = 0;
    private int height = 0;
    private int pixelSize = 0;

    public BufferBackedImage(
            @Nullable StructValue structValue,
            boolean generateInfo
    ) {
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
        return byteBuf.getInt((y * width + x) * pixelSize);
    }

    @Override
    public void setPixelAsRGBA(int x, int y, int rgba) {
        assert x < width;
        assert y < height;
        byteBuf.putInt((y * getWidth() + x) * pixelSize, rgba);
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }
}
