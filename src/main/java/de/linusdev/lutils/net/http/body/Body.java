/*
 * Copyright (c) 2023-2025 Linus Andera
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

package de.linusdev.lutils.net.http.body;

import de.linusdev.lutils.net.http.header.HeaderMap;
import de.linusdev.lutils.net.http.header.HeaderNames;
import de.linusdev.lutils.net.http.header.contenttype.ContentType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;

public interface Body {

    /**
     * @return the content-type of this body
     */
    @Nullable ContentType contentType();

    /**
     * the length of the body or -1 if the length is unknown.
     * @return length of this body or -1
     */
    long length();

    default long definitiveLength(){
        if(length() != -1) return length();

        // read the stream to get the length
        try(InputStream stream = stream()) {

            byte[] buffer = new byte[2048];
            long actualLength = 0;
            int len;
            while((len = stream.read(buffer)) != -1)
                actualLength += len;

            return actualLength;

        } catch (IOException e) {
            throw new IllegalStateException("Cannot calculate length of this body.", e);
        }
    }

    /**
     * Adds the for this body required headers to given {@link HeaderMap}.
     * @param headers map to add the headers to
     */
    default void adjustHeaders(@NotNull HeaderMap headers) {
        ContentType contentType = contentType();
        if(contentType != null)
            headers.put(contentType.asHeader());

        headers.put(HeaderNames.CONTENT_LENGTH.with("" + definitiveLength()));
    }

    /**
     * Removes the for this body required headers from given {@link HeaderMap}.
     * @param headers map to remove the headers from
     */
    default void removeHeaders(@NotNull HeaderMap headers) {
        ContentType contentType = contentType();
        if(contentType != null && headers.containsValue(contentType.asHeader()))
            headers.remove(HeaderNames.CONTENT_TYPE);

        if(headers.containsValue(HeaderNames.CONTENT_LENGTH.with("" + length())))
            headers.remove(HeaderNames.CONTENT_LENGTH);
    }

    /**
     * {@link InputStream} containing the body. The returned stream must be closed by the method caller.
     * If this method is called multiple times, it must supply a new stream each time.
     * @return {@link InputStream}
     */
    @NotNull InputStream stream() throws IOException;

}
