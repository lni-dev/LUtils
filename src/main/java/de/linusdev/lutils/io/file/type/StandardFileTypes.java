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

package de.linusdev.lutils.io.file.type;

import de.linusdev.lutils.net.http.header.contenttype.ContentType;
import org.jetbrains.annotations.NotNull;

public enum StandardFileTypes implements FileType {

    MP3(
            "MPEG-2 Audio Layer III",
            new ContentType[]{
                    ContentType.of("audio", "mpeg"),
                    ContentType.of("audio", "MPA"),
                    ContentType.of("audio", "mpa-robust")
            },
            "mp3", "mpga", "bit"
    ),

    WAV(
            "Waveform Audio File",
            new ContentType[]{
                    ContentType.of("audio", "vnd.wave"),
                    ContentType.of("audio", "wav"),
                    ContentType.of("audio", "wave"),
                    ContentType.of("audio", "x-wav")
            },
            "wav", "wave"
    ),

    ;

    private final @NotNull String name;
    private final @NotNull ContentType @NotNull [] associatedContentTypes;
    private final @NotNull String @NotNull [] fileEndings;

    StandardFileTypes(
            @NotNull String name,
            @NotNull ContentType @NotNull [] associatedContentTypes,
            @NotNull String @NotNull... fileEndings
    ) {
        this.name = name;
        this.associatedContentTypes = associatedContentTypes;
        this.fileEndings = fileEndings;
    }

    @Override
    public @NotNull String fullName() {
        return name;
    }

    @Override
    public @NotNull String @NotNull [] fileEndings() {
        return fileEndings;
    }

    @Override
    public @NotNull ContentType @NotNull [] associatedContentTypes() {
        return associatedContentTypes;
    }
}
