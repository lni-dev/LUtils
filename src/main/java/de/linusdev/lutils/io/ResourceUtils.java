/*
 * Copyright (c) 2024 Linus Andera
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
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class ResourceUtils {

    /**
     * Get {@link StreamURLConnection} as described below.
     * @param relClazz class used to get the resource.
     * @param path Either absolut (starting with {@code /}) or relative to the package of given {@code relClazz}
     *             (not starting {@code /}).
     * @return {@link StreamURLConnection} to resource at given {@code path}.
     * @throws Error if resource with given {@code path} does not exist.
     */
    public static @NotNull StreamURLConnection getURLConnectionOfResource(
            @NotNull Class<?> relClazz,
            @NotNull String path
    ) {
        URL resource =  relClazz.getResource(path);

        if (resource == null) {
            throw new Error("Resource '" + path + "' does not exist. Remember paths starting with \"/\" are absolute." +
                    " Paths not starting with \"/\" are relative to the package of relClazz.");
        }

        return new StreamURLConnection(path, resource);
    }

    public static class StreamURLConnection {
        private final @NotNull String path;
        private final @NotNull URL resource;

        private @Nullable URLConnection currentOpenConnection;

        public StreamURLConnection(@NotNull String path, @NotNull URL resource) {
            this.path = path;
            this.resource = resource;
        }

        private @NotNull URLConnection getURLConnection(boolean canCache) {
            if(currentOpenConnection != null) {
                if(canCache) return currentOpenConnection;
                else {
                    URLConnection copy = currentOpenConnection;
                    currentOpenConnection = null;
                    return copy;
                }
            }

            try {
                currentOpenConnection = resource.openConnection();
                return getURLConnection(canCache);
            } catch (IOException e) {
                throw new Error("Resource '" + path + "' cannot be read.", e);
            }

        }

        public long getContentLength() {
            return getURLConnection(true).getContentLengthLong();
        }

        public @NotNull InputStream openInputStream() throws IOException {
            return getURLConnection(false).getInputStream();
        }
    }

}
