/*
 * Copyright (c) 2023 Linus Andera
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

package de.linusdev.lutils.http_WIP.body;

import org.jetbrains.annotations.NotNull;

import java.io.InputStream;

public interface BodySupplier {

    /**
     * the length of the body or -1 if the length is unknown.
     * @return length of this body or -1
     */
    int length();

    /**
     * {@link InputStream} containing the body. The returned stream must be closed by the method caller.
     * @return {@link InputStream}
     */
    @NotNull InputStream stream();

}
