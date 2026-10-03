/*
 * Copyright (c) 2026 Linus Andera
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

package de.linusdev.lutils.id3v2.framedata;

import de.linusdev.lutils.image.Image;
import de.linusdev.lutils.image.ImageIO;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public record AttachedPicture(
        int encoding,
        String mimeType,
        int pictureType,
        String description,
        byte[] imageData
) {

    public @NotNull Id3v2PictureType getPictureType() {
        return Id3v2PictureType.from(pictureType);
    }

    public static Charset encodingToCharset(int encoding) {
        return switch (encoding) {
            case 0 -> StandardCharsets.ISO_8859_1;
            case 1 -> StandardCharsets.UTF_16;
            case 2 -> StandardCharsets.UTF_16BE;
            case 3 -> StandardCharsets.UTF_8;
            default -> throw new IllegalArgumentException();
        };
    }

    public Image getImage() {
        try {
            return ImageIO.read(mimeType, new ByteArrayInputStream(imageData));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read image", e);
        }
    }
}