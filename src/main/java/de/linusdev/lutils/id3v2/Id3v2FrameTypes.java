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

import de.linusdev.lutils.id3v2.framedata.AttachedPicture;
import de.linusdev.lutils.id3v2.framedata.UserDefinedText;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public final class Id3v2FrameTypes {

    private Id3v2FrameTypes() {}

    // Text information frames
    public static final Id3v2FrameType<String> TITLE = new TextFrameType("Title", "TIT2");
    public static final Id3v2FrameType<String> SUBTITLE = new TextFrameType("Subtitle", "TIT3");
    public static final Id3v2FrameType<String> ARTIST = new TextFrameType("Artist", "TPE1");
    public static final Id3v2FrameType<String> ALBUM_ARTIST = new TextFrameType("Album Artist", "TPE2");
    public static final Id3v2FrameType<String> CONDUCTOR = new TextFrameType("Conductor", "TPE3");
    public static final Id3v2FrameType<String> REMIXER = new TextFrameType("Remixer", "TPE4");
    public static final Id3v2FrameType<String> ALBUM = new TextFrameType("Album", "TALB");
    public static final Id3v2FrameType<String> TRACK = new TextFrameType("Track Number", "TRCK");
    public static final Id3v2FrameType<String> DISC = new TextFrameType("Disc Number", "TPOS");
    public static final Id3v2FrameType<String> YEAR = new TextFrameType("Recording Time", "TDRC");
    public static final Id3v2FrameType<String> RELEASE_TIME = new TextFrameType("Release Time", "TDRL");
    public static final Id3v2FrameType<String> GENRE = new TextFrameType("Genre", "TCON");
    public static final Id3v2FrameType<String> COMPOSER = new TextFrameType("Composer", "TCOM");
    public static final Id3v2FrameType<String> LYRICIST = new TextFrameType("Lyricist", "TEXT");
    public static final Id3v2FrameType<String> LANGUAGE = new TextFrameType("Language", "TLAN");
    public static final Id3v2FrameType<String> COPYRIGHT = new TextFrameType("Copyright", "TCOP");
    public static final Id3v2FrameType<String> ENCODER = new TextFrameType("Encoder", "TENC");
    public static final Id3v2FrameType<String> BPM = new TextFrameType("Beats Per Minute", "TBPM");
    public static final Id3v2FrameType<String> ISRC = new TextFrameType("ISRC", "TSRC");
    public static final Id3v2FrameType<String> MEDIA_TYPE = new TextFrameType("Media Type", "TMED");
    public static final Id3v2FrameType<String> MOOD = new TextFrameType("Mood", "TMOO");
    public static final Id3v2FrameType<String> GROUPING = new TextFrameType("Grouping", "TIT1");
    public static final Id3v2FrameType<String> INITIAL_KEY = new TextFrameType("Initial Key", "TKEY");
    public static final Id3v2FrameType<String> FILE_OWNER = new TextFrameType("File Owner", "TOWN");
    public static final Id3v2FrameType<String> INTERNET_RADIO_NAME = new TextFrameType("Internet Radio Name", "TRSN");
    public static final Id3v2FrameType<String> INTERNET_RADIO_OWNER = new TextFrameType("Internet Radio Owner", "TRSO");
    public static final Id3v2FrameType<String> ALBUM_ARTIST_SORT_ORDER = new TextFrameType("Album Artist Sort Order", "TSO2");
    public static final Id3v2FrameType<String> PERFORMER_SORT_ORDER = new TextFrameType("Performer Sort Order", "TSOP");
    public static final Id3v2FrameType<String> ORIGINAL_RELEASE_TIME = new TextFrameType("Original Release Time", "TDOR");
    public static final Id3v2FrameType<String> PUBLISHER = new TextFrameType("Publisher", "TPUB");
    public static final Id3v2FrameType<String> ENCODING_SETTINGS = new TextFrameType("Encoding Settings", "TSSE");

    // URL frames
    public static final Id3v2FrameType<String> OFFICIAL_ARTIST_URL = new TextFrameType("Official Artist URL", "WOAR");
    public static final Id3v2FrameType<String> OFFICIAL_AUDIO_URL = new TextFrameType("Official Audio URL", "WOAF");
    public static final Id3v2FrameType<String> OFFICIAL_FILE_URL = new TextFrameType("Official File URL", "WOAS");
    public static final Id3v2FrameType<String> COPYRIGHT_URL = new TextFrameType("Copyright URL", "WCOP");
    public static final Id3v2FrameType<String> PUBLISHER_URL = new TextFrameType("Publisher URL", "WPUB");

    // Binary / structured
    public static final Id3v2FrameType<AttachedPicture> ALBUM_ART = new AttachedPictureFrameType("Album Art", "APIC");
    public static final Id3v2FrameType<byte[]> COMMENT = new BinaryFrameType("Comment", "COMM");
    public static final Id3v2FrameType<byte[]> LYRICS = new BinaryFrameType("Unsynchronised Lyrics", "USLT");
    public static final Id3v2FrameType<UserDefinedText> USER_TEXT = new UserDefinedTextFrameType("User Defined Text", "TXXX");
    public static final Id3v2FrameType<byte[]> USER_URL = new BinaryFrameType("User Defined URL", "WXXX");
    public static final Id3v2FrameType<byte[]> PRIVATE = new BinaryFrameType("Private", "PRIV");
    public static final Id3v2FrameType<byte[]> POPULARIMETER = new BinaryFrameType("Popularimeter", "POPM");
    public static final Id3v2FrameType<byte[]> CHAPTER = new BinaryFrameType("Chapter", "CHAP");
    public static final Id3v2FrameType<byte[]> TABLE_OF_CONTENTS = new BinaryFrameType("Table Of Contents", "CTOC");
    public static final Id3v2FrameType<byte[]> SYNCED_LYRICS = new BinaryFrameType("Synchronised Lyrics", "SYLT");
    public static final Id3v2FrameType<byte[]> TERMS_OF_USE = new BinaryFrameType("Terms Of Use", "USER");
    public static final Id3v2FrameType<byte[]> UNIQUE_FILE_ID = new BinaryFrameType("Unique File Identifier", "UFID");
    public static final Id3v2FrameType<byte[]> GENERAL_OBJECT = new BinaryFrameType("General Encapsulated Object", "GEOB");

    public static final Id3v2FrameType<?>[] ALL = new Id3v2FrameType<?>[] {
            GROUPING, TITLE, SUBTITLE, ARTIST, ALBUM_ARTIST, CONDUCTOR, REMIXER, ALBUM, TRACK, DISC, YEAR, RELEASE_TIME,
            ORIGINAL_RELEASE_TIME, GENRE, COMPOSER, LYRICIST, LANGUAGE, COPYRIGHT, ENCODER, BPM, ISRC, MEDIA_TYPE, MOOD,
            INITIAL_KEY, FILE_OWNER, INTERNET_RADIO_NAME, INTERNET_RADIO_OWNER, PUBLISHER, ALBUM_ARTIST_SORT_ORDER,
            PERFORMER_SORT_ORDER, ENCODING_SETTINGS, OFFICIAL_ARTIST_URL, OFFICIAL_AUDIO_URL, OFFICIAL_FILE_URL,
            COPYRIGHT_URL, PUBLISHER_URL, ALBUM_ART, COMMENT, LYRICS, USER_TEXT, USER_URL, PRIVATE, POPULARIMETER,
            CHAPTER, TABLE_OF_CONTENTS, SYNCED_LYRICS, TERMS_OF_USE, UNIQUE_FILE_ID, GENERAL_OBJECT
    };

    public static @NotNull Id3v2FrameType<?> fromId(@NotNull String id) {
        for (Id3v2FrameType<?> type : ALL)
            if(type.id().equalsIgnoreCase(id))
                return type;

        return new BinaryFrameType(null, id);
    }

    private record TextFrameType(@Nullable String name, @NotNull String id) implements Id3v2FrameType<String> {

        @SuppressWarnings("DuplicateBranchesInSwitch")
        @Override
        public @NotNull String decode(byte @NotNull [] data) {
            if (data.length == 0) return "";

            int encoding = data[0] & 0xFF;

            int end = data.length - 1;
            for (;end >= 0; end--) {
                if(data[end] != 0)
                    break;
            }
            end++;

            return switch (encoding) {
                case 0  -> new String(data, 1, end - 1, StandardCharsets.ISO_8859_1);
                case 1  -> new String(data, 1, end - 1,  StandardCharsets.UTF_16);
                case 2  -> new String(data, 1, end - 1,  StandardCharsets.UTF_16BE);
                case 3  -> new String(data, 1, end - 1,  StandardCharsets.UTF_8);
                default -> new String(data, 1, end - 1,  StandardCharsets.UTF_8);
            };
        }
    }

    private record BinaryFrameType(@Nullable String name, String id) implements Id3v2FrameType<byte[]> {
        @Override
        public byte[] decode(byte @NotNull [] data) {
            return data;
        }
    }

    private record UserDefinedTextFrameType(@Nullable String name, @NotNull String id) implements Id3v2FrameType<UserDefinedText> {

        @Override
        public UserDefinedText decode(byte @NotNull [] data) {
            int encoding = Byte.toUnsignedInt(data[0]);

            Charset charset = UserDefinedText.encodingToCharset(encoding);
            int separatorLength = encoding == 0 || encoding == 3 ? 1 : 2;

            int split = 1;
            for (; split <= data.length - separatorLength; split += separatorLength) {
                if (data[split] == 0x00) {
                    if(separatorLength == 2) {
                        if(data[split + 1] == 0x00) break;
                    } else {
                        break;
                    }
                }
            }

            int zeroCount = 0;
            for (int i = data.length - 1; i >= 0; i--) {
                if(data[i] == 0) {
                    zeroCount++;
                } else {
                    break;
                }
            }


            String description = new String(data, 1, split - 1, charset);
            String value = new String(data, split + separatorLength, data.length - split - separatorLength - zeroCount, charset);

            return new UserDefinedText(
                    encoding,
                    description,
                    value
            );
        }
    }

    private record AttachedPictureFrameType(@Nullable String name, @NotNull String id) implements Id3v2FrameType<AttachedPicture> {

        @Override
        public AttachedPicture decode(byte @NotNull [] data) {
            int pos = 0;

            int encoding = Byte.toUnsignedInt(data[pos++]);
            Charset charset = AttachedPicture.encodingToCharset(encoding);

            int end = 1;
            for (; end <= data.length - 1; end += 1)
                if (data[end] == 0x00)
                   break;

            String mimeType = new String(data, pos, end - pos, StandardCharsets.ISO_8859_1);

            pos = end + 1;
            int pictureType = Byte.toUnsignedInt(data[pos++]);

            int separatorLength = encoding == 0 || encoding == 3 ? 1 : 2;
            end = pos;
            for (; end <= data.length - separatorLength; end += separatorLength) {
                if (data[end] == 0x00) {
                    if(separatorLength == 2) {
                        if(data[end + 1] == 0x00) break;
                    } else {
                        break;
                    }
                }
            }

            String description = new String(data, pos, end - pos, charset);
            pos = end + (encoding == 1 || encoding == 2 ? 2 : 1);

            byte[] imageData = new byte[data.length - pos];
            System.arraycopy(data, pos, imageData, 0, imageData.length);

            return new AttachedPicture(encoding, mimeType, pictureType, description, imageData);
        }
    }
}