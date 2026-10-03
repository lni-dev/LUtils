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

package de.linusdev.lutils.id3v2;

import de.linusdev.lutils.image.Image;
import de.linusdev.lutils.image.ScaleType;
import de.linusdev.lutils.io.ResourceUtils;
import de.linusdev.lutils.other.parser.ParseException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static de.linusdev.lutils.id3v2.Id3v2FrameTypes.ALBUM_ART;

class Id3v2DataTest {

    @Test
    void test() throws IOException, ParseException {

        Id3v2Parser parser = new Id3v2Parser();

        InputStream in = ResourceUtils.getInputStream(Id3v2DataTest.class, "jump.wav");


        Id3v2Data data = parser.parse(in);

        System.out.println(data);
        data.getFrames().stream().filter(frame -> frame.type() == ALBUM_ART).forEach(frame -> {
            System.out.println(Image.printable(Image.scale(ALBUM_ART.decode(frame).getImage(), Image.create(32, 32), ScaleType.NEAREST_NEIGHBOR)));
        });
    }
}