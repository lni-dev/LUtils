/*
 * Copyright (c) 2023-2025 Linus Andera
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

package de.linusdev.lutils.ansi.sgr;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SGRTest {


    @Test
    public void test() {
        SGR sgr = new SGR(SGRParameters.FOREGROUND_RED);

        System.out.println(sgr.construct() + "Some text in red." + SGR.reset());
    }

    @Test
    public void someTests() {
        assertEquals("\033[0m", SGR.reset());
        assertThrows(IllegalArgumentException.class, () -> new SGR(SGRParameters.BACKGROUND_COLOR_24_BIT, "1", "2"));
        assertEquals("\033[48;2;20;30;40m", new SGR(SGRParameters.BACKGROUND_COLOR_24_BIT, "2", "20", "30", "40").construct());
        assertEquals("\033[48,2,20,30,40m", new SGR(",", SGRParameters.BACKGROUND_COLOR_24_BIT, "2", "20", "30", "40").construct());
        assertEquals("\033[48;2;20;30;40m", new SGR(SGRParameters.BACKGROUND_COLOR_24_BIT, "20", "30", "40").construct());

        assertEquals("\033[48;2;20;30;40m", new SGR().add(SGRParameters.BACKGROUND_COLOR_24_BIT, "20", "30", "40").construct());
    }
}