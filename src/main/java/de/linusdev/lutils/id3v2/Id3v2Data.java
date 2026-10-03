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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Id3v2Data {

    private final @NotNull Id3v2Header header;
    private final @Nullable Id3v2ExtendedHeader extendedHeader;
    private final @NotNull List<Id3v2Frame> frames;

    public Id3v2Data(@NotNull Id3v2Header header, @Nullable Id3v2ExtendedHeader extendedHeader, @NotNull List<Id3v2Frame> frames) {
        this.header = header;
        this.extendedHeader = extendedHeader;
        this.frames = frames;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Id3v2Frame frame : frames) {
            sb.append("\n - ").append(frame);
        }
        return "header: " + header + "\nextendedHeader: " + extendedHeader + "\nframes:" + sb;
    }

    public @NotNull Id3v2Header getHeader() {
        return header;
    }

    public @Nullable Id3v2ExtendedHeader getExtendedHeader() {
        return extendedHeader;
    }

    public @NotNull List<Id3v2Frame> getFrames() {
        return frames;
    }
}
