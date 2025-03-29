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

package de.linusdev.lutils.io;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;

public class InputStreamUtils {

    /**
     * Read given input stream until the array is filled.
     * @param in input stream to read from
     * @param buffer array to fill
     * @return {@code true} if it was possible to fill the array completely, {@code false} if the stream's {@link InputStream#read()}
     * returned -1 before the buffer could be filled.
     * @throws IOException while reading
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean readUntilArrayIsFull(@NotNull InputStream in, byte @NotNull [] buffer) throws IOException {
        int current;
        int count = 0;
        //noinspection StatementWithEmptyBody
        while((current = in.read(buffer, count, buffer.length - count)) != -1 && (count += current) < buffer.length) {}

        return count == buffer.length;
    }

}
