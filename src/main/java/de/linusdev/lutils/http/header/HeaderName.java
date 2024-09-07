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

package de.linusdev.lutils.http.header;

import de.linusdev.lutils.http.header.value.HeaderValue;
import org.jetbrains.annotations.NotNull;

public interface HeaderName {

    /**
     * @return name of this header
     */
    String getName();

    default Header with(String value) {
        return new HeaderImpl(getName(), value);
    }

    default Header with(@NotNull HeaderValue value) {
        return new HeaderImpl(getName(), value.asString());
    }

}
