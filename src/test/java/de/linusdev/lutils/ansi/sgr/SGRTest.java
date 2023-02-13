/*
 * Copyright (c) 2023 Linus Andera all rights reserved
 */

package de.linusdev.lutils.ansi.sgr;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

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
    }
}