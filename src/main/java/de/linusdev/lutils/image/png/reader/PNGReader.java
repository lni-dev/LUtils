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

package de.linusdev.lutils.image.png.reader;

import de.linusdev.lutils.image.Image;
import de.linusdev.lutils.image.java.JavaBackedImage;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream;

public class PNGReader {

    public static @NotNull Image read(@NotNull InputStream in) throws IOException {
        return new JavaBackedImage(ImageIO.read(in));
    }

    public static @NotNull Image readFromResource(@NotNull String name) throws IOException {
        try (InputStream in = PNGReader.class.getClassLoader().getResourceAsStream(name)) {

            if(in == null)
                throw new IllegalArgumentException("Cannot find resource '" + name + "'.");

            return read(in);
        }
    }

}
