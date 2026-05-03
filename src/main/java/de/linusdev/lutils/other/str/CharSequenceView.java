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

package de.linusdev.lutils.other.str;

import org.jetbrains.annotations.NotNull;

public class CharSequenceView implements CharSequence {

    private final @NotNull CharSequence original;
    private final int start;
    private final int length;

    public CharSequenceView(@NotNull CharSequence original, int start, int length) {
        this.original = original;
        this.start = start;
        this.length = length;
    }

    public CharSequenceView(@NotNull CharSequence original, int start) {
        this.original = original;
        this.start = start;
        this.length = original.length() - start;
    }

    @Override
    public int length() {
        return length;
    }

    @Override
    public char charAt(int index) {
        return original.charAt(start + index);
    }

    @Override
    public @NotNull CharSequence subSequence(int start, int end) {
        return new CharSequenceView(original, this.start + start, end - start);
    }

    @Override
    public @NotNull String toString() {
        return original.subSequence(start, start+length).toString();
    }
}
