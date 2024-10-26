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

package de.linusdev.lutils.html.lhtml;

import de.linusdev.lutils.html.parser.Registry;
import org.jetbrains.annotations.NotNull;

public class Lhtml {

    /**
     * {@link Registry.Builder} required when parsing any {@link LhtmlElement}.
     */
    public static @NotNull Registry.Builder getRegistry() {
        Registry.Builder builder = Registry.getDefault();
        builder.putElement(LhtmlHead.TYPE);

        return builder;
    }

}
