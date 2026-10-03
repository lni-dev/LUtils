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

package de.linusdev.lutils.id3v2.tagrestrictions;

public record TagRestrictions(
        int rawByte,
        TagSizeRestriction tagSizeRestriction,
        TextEncodingRestriction textEncodingRestriction,
        TextFieldSizeRestriction textFieldSizeRestriction,
        ImageEncodingRestriction imageEncodingRestriction,
        ImageSizeRestriction imageSizeRestriction
) {

    public static TagRestrictions fromByte(int b) {
        return new TagRestrictions(
                b,
                TagSizeRestriction.from((b >> 6) & 0b11),
                TextEncodingRestriction.from((b >> 4) & 0b11),
                TextFieldSizeRestriction.from((b >> 2) & 0b11),
                ImageEncodingRestriction.from((b >> 1) & 0b1),
                ImageSizeRestriction.from(b & 0b1)
        );
    }
}