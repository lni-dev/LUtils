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

package de.linusdev.lutils.image.wip_webp.reader;

import de.linusdev.lutils.image.wip_webp.reader.simple.lossless.SimpleLosslessWebP;
import de.linusdev.lutils.io.InputStreamUtils;
import de.linusdev.lutils.nat.EndianUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;

import static de.linusdev.lutils.image.wip_webp.reader.WebReaderUtils.*;

public class WebPReader {




    public WebPReader() {

    }

    public void read(@NotNull InputStream in) throws WebPReaderException {
        try {
            byte[] headerAndFirstChunkHeader = new byte[WEBP_FILE_HEADER_BYTE_LENGTH + RIFF_CHUNK_HEADER];

            if(!InputStreamUtils.readUntilArrayIsFull(in, headerAndFirstChunkHeader))
                throw new WebPReaderException("File too short.");

            int readingPos = 0;

            if(!checkChunkFourCC(headerAndFirstChunkHeader, 0, RIFF))
                throw new WebPReaderException("First webp header fourCC should be RIFF, but is '"
                        + convertChunkFourCC(headerAndFirstChunkHeader, 0)
                        + "'."
                );
            readingPos += RIFF_FOUR_CC_BYTE_LENGTH; // RIFF

            int fileSize = EndianUtils.littleEndianBytesToInt(headerAndFirstChunkHeader, readingPos);

            readingPos += RIFF_CHUNK_SIZE_BYTE_LENGTH; // fileSize

            if(!checkChunkFourCC(headerAndFirstChunkHeader, readingPos, WEBP))
                throw new WebPReaderException("Second webp header fourCC should be WEBP, but is '"
                        + convertChunkFourCC(headerAndFirstChunkHeader, readingPos)
                        + "'."
                );

            readingPos += RIFF_FOUR_CC_BYTE_LENGTH; // WEBP

            int actualFileSize = fileSize + RIFF_FOUR_CC_BYTE_LENGTH + RIFF_CHUNK_SIZE_BYTE_LENGTH;

            WebPImageType type = WebPImageType.of(headerAndFirstChunkHeader, readingPos);

            readingPos += RIFF_FOUR_CC_BYTE_LENGTH; // VP8_

            if(type == WebPImageType.SIMPLE_LOSSY)
                throw new WebPReaderException("Webp of type simple-lossy is not supported.");

            if(type == WebPImageType.SIMPLE_LOSSLESS) {
                int chunkSize = EndianUtils.littleEndianBytesToInt(headerAndFirstChunkHeader, readingPos);
                // headerAndFirstChunkHeader completely read

                byte[] chunk = new byte[chunkSize];

                if(!InputStreamUtils.readUntilArrayIsFull(in, chunk))
                    throw new WebPReaderException("Webp missing chunk data.");

                BitReader reader = new BitReader(chunk);

                SimpleLosslessWebP.read(reader);

            }

        } catch (IOException e) {
            throw new WebPReaderException(e);
        }

    }


}
