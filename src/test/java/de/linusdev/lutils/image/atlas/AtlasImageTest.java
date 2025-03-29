/*
 * Copyright (c) 2025 Linus Andera
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

package de.linusdev.lutils.image.atlas;

import de.linusdev.lutils.color.Color;
import de.linusdev.lutils.color.Colors;
import de.linusdev.lutils.color.creator.ColorCreator;
import de.linusdev.lutils.image.Image;
import de.linusdev.lutils.image.png.reader.PNGReader;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Random;

class AtlasImageTest {

    @Test
    public void test() throws IOException {
        AtlasBuilder<String> builder = new AtlasBuilder<>(10);

        ColorCreator creator = Colors.rainbow(20);
        var it = creator.iterator();

        Random random = new Random(123);



        builder.add(ImageRef.ofImage(PNGReader.readFromResource("de/linusdev/lutils/image/buffer/grass_side-v1.png"), "grass"));
        //System.out.println("Result: \n" + Image.printable(builder.build(Image::create)));

        for (int i = 0; i < 20; i++) {
            int width = random.nextInt(2, 5);
            int height = random.nextInt(2, 5);
            Color color = it.next();

            Image image = Image.fill(Image.create(width*width, height*height), color);
            System.out.println("adding: \n" + Image.printable(image));

            builder.add(ImageRef.ofImage(image, "image " + i));

            //System.out.println("Result: \n" + Image.printable(builder.build(Image::create)));
        }

        Atlas<Image, String> result = builder.build(Image::create);
        System.out.println("Result: \n" + Image.printable(result));

        for (AtlasImage<String> img : result) {
            System.out.println("ID: " + img.getId());
            System.out.println("isRotated: " + img.isRotated());
            System.out.println("UVs: (" + img.getUvXStart() + ", " + img.getUvYStart() + ") (" + img.getUvXEnd() + ", " + img.getUvYEnd() + ")");
            System.out.println("Result: \n" + Image.printable(img));
        }
    }

}