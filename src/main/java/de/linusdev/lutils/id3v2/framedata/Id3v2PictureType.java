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

public enum Id3v2PictureType {

    OTHER(0),
    FILE_ICON_32x32(1),
    OTHER_FILE_ICON(2),
    FRONT_COVER(3),
    BACK_COVER(4),
    LEAFLET_PAGE(5),
    MEDIA(6),
    LEAD_ARTIST(7),
    ARTIST(8),
    CONDUCTOR(9),
    BAND(10),
    COMPOSER(11),
    LYRICIST(12),
    RECORDING_LOCATION(13),
    DURING_RECORDING(14),
    DURING_PERFORMANCE(15),
    MOVIE_SCREEN_CAPTURE(16),
    COLOURED_FISH(17),
    ILLUSTRATION(18),
    BAND_LOGO(19),
    PUBLISHER_LOGO(20),

    ;

    private static final Id3v2PictureType[] ALL = values();

    private final int value;

    Id3v2PictureType(int value) {
        this.value = value;
        assert value == ordinal();
    }

    public int getValue() {
        return value;
    }

    public static Id3v2PictureType from(int value) {
        if(value < 0 || value >= ALL.length)
            return OTHER;
        return ALL[value];
    }
}