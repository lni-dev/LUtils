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

import de.linusdev.lutils.bitfield.IntBitfield;
import de.linusdev.lutils.bitfield.IntBitfieldImpl;
import de.linusdev.lutils.id3v2.tagrestrictions.TagRestrictions;
import de.linusdev.lutils.io.InputStreamUtils;
import de.linusdev.lutils.io.file.type.FileType;
import de.linusdev.lutils.io.file.type.StandardFileTypes;
import de.linusdev.lutils.other.ByteUtils;
import de.linusdev.lutils.other.parser.ParseException;
import de.linusdev.lutils.version.ReleaseType;
import de.linusdev.lutils.version.Version;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;

public class Id3v2Parser {

    final static int HEADER_SIZE = 10;
    final static byte[] EXPECTED_MAGIC = new byte[] {
            'I', 'D', '3'
    };
    final static int SIZE_VARIABLE_SIZE = 4;

    public Id3v2Parser() {

    }

    public Id3v2Data parse(@NotNull InputStream in, @NotNull FileType fileType) throws IOException, ParseException {
        if(FileType.equals(StandardFileTypes.MP3, fileType))
            return parse(in);
        else if(FileType.equals(StandardFileTypes.WAV, fileType))
    }

    public Id3v2Data parseWave(@NotNull InputStream in) {
        
    }

    public Id3v2Data parse(@NotNull InputStream in) throws IOException, ParseException {
        byte[] header = new byte[HEADER_SIZE];

        if(!InputStreamUtils.readUntilArrayIsFull(in, header))
            throw new ParseException("Unable to read ID3 header: File too short");

        int pos = 0;
        for (; pos < EXPECTED_MAGIC.length; pos++) {
            if(header[pos] != EXPECTED_MAGIC[pos])
                throw new ParseException(
                        "data starts with '"
                        + HexFormat.of().formatHex(header, 0, 3)
                        + "' but expected is '" + HexFormat.of().formatHex(EXPECTED_MAGIC) + "'."
                );
        }

        int minor = Byte.toUnsignedInt(header[pos++]);
        int patch = Byte.toUnsignedInt(header[pos++]);
        int flags = Byte.toUnsignedInt(header[pos++]);
        int size = syncSafeToInt(header, pos);

        Id3v2Header pHeader = new Id3v2Header(Version.of(ReleaseType.RELEASE, 2, minor, patch), flags, size);
        Id3v2ExtendedHeader extendedHeader = null;

        if(pHeader.getFlagsAsBitfield().isSet(Id3v2HeaderFlags.EXTENDED_HEADER)) {
            extendedHeader = parseExtendedHeader(in);
        }

        return new Id3v2Data(pHeader, extendedHeader, parseFrames(in, size - (extendedHeader == null ? 0 :extendedHeader.actualSize())));
    }

    private static Id3v2ExtendedHeader parseExtendedHeader(@NotNull InputStream in) throws ParseException, IOException {
        // Parse extended header
        byte[] extendedHeader = new byte[SIZE_VARIABLE_SIZE];
        if(!InputStreamUtils.readUntilArrayIsFull(in, extendedHeader))
            throw new ParseException("Unable to read ID3 **extended** header size: File too short");

        int extendedHeaderSize = syncSafeToInt(extendedHeader, 0);
        extendedHeader = new byte[extendedHeaderSize];
        if(!InputStreamUtils.readUntilArrayIsFull(in, extendedHeader))
            throw new ParseException("Unable to read ID3 **extended** header: File too short");

        int pos = 0;
        int extendedHeaderFlags = (Byte.toUnsignedInt(extendedHeader[pos++] ) << 8) | Byte.toUnsignedInt(extendedHeader[pos++]);
        IntBitfield<Id3v2ExtendedHeaderFlags> f = new IntBitfieldImpl<>(extendedHeaderFlags);

        int crc32 = 0;
        TagRestrictions tagRestrictions = null;
        boolean unknownCrc = true;
        boolean failedToReadCrc = false;
        boolean failedToReadTagRestrictions = false;

        if(f.isSet(Id3v2ExtendedHeaderFlags.CRC_DATA_PRESENT)) {
            int crcLength = Byte.toUnsignedInt(extendedHeader[pos++]);

            if(pos + crcLength > extendedHeader.length) {
                // data is bad. Don't parse crc
                failedToReadCrc = true;
            } if(crcLength == 4) {
                unknownCrc = false;
                crc32 = ByteUtils.constructInt(extendedHeader[pos++], extendedHeader[pos++], extendedHeader[pos++], extendedHeader[pos++]);
            } else {
                pos += crcLength;
            }
        }

        if(f.isSet(Id3v2ExtendedHeaderFlags.TAG_RESTRICTIONS)) {
            if(pos >= extendedHeader.length) {
                failedToReadTagRestrictions = true;
            } else {
                tagRestrictions = TagRestrictions.fromByte(extendedHeader[pos]);
            }
        }

        return new Id3v2ExtendedHeader(
                extendedHeaderSize,
                extendedHeaderFlags,
                crc32, unknownCrc, failedToReadCrc,
                tagRestrictions,
                failedToReadTagRestrictions
        );
    }

    private static List<Id3v2Frame> parseFrames(InputStream in, final int remainingSize) throws IOException, ParseException {
        byte[] frameHeader = new byte[10];
        int readCount = 0;
        List<Id3v2Frame> frames = new ArrayList<>();

        while(readCount < remainingSize) {
            if(!InputStreamUtils.readUntilArrayIsFull(in, frameHeader))
                throw new ParseException("Unable to read ID3 frame: File too short");
            readCount += 10;

            if(frameHeader[0] == 0x0) {
                // we found padding
                break;
            }

            String frameId = new String(frameHeader, 0, 4);
            int size = syncSafeToInt(frameHeader, 4);
            int flags = (Byte.toUnsignedInt(frameHeader[8]) << 8) | Byte.toUnsignedInt(frameHeader[9]);
            byte[] data = new byte[size];
            if(!InputStreamUtils.readUntilArrayIsFull(in, data))
                throw new ParseException("Unable to read ID3 frame '" + frameId + "' data: File too short");

            frames.add(new Id3v2Frame(Id3v2FrameTypes.fromId(frameId), flags, data));
        }

        return frames;
    }

    private static int syncSafeToInt(byte[] b,  int offset) {
        return (b[offset] & 0x7F) << 21 |
                (b[offset + 1] & 0x7F) << 14 |
                (b[offset + 2] & 0x7F) << 7  |
                (b[offset + 3] & 0x7F);
    }

}
