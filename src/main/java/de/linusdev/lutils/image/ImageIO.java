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

package de.linusdev.lutils.image;

import de.linusdev.lutils.image.java.JavaBackedImage;
import de.linusdev.lutils.io.ResourceUtils;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageIO {

    public static @NotNull Image read(@NotNull InputStream in) throws IOException {

        return new JavaBackedImage(javax.imageio.ImageIO.read(in));
    }

    public static @NotNull Image read(@NotNull String mimeType, @NotNull InputStream in) throws IOException {
        ImageReader reader = javax.imageio.ImageIO.getImageReadersByMIMEType(mimeType).next();
        ImageInputStream iin = javax.imageio.ImageIO.createImageInputStream(in);
        reader.setInput(iin);
        return new JavaBackedImage(reader.read(0));
    }

    public static @NotNull Image readFromResource(@NotNull String name) throws IOException {
        try (InputStream in = ResourceUtils.getURLConnectionOfResource(name).openInputStream()) {
            return read(in);
        }
    }

}
