/*
 * Copyright (c) 2025 Linus Andera
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

package de.linusdev.lutils.pack.errors;

import de.linusdev.lutils.data.json.Json;
import de.linusdev.lutils.other.str.StringUtils;
import de.linusdev.lutils.pack.AbstractPack;
import org.jetbrains.annotations.NotNull;

public class PackContentException extends PackException {

    public PackContentException(@NotNull AbstractPack pack, @NotNull Throwable cause) {
        super(pack, "Error while reading content of pack '" + pack +  "'.", cause);
    }

    public PackContentException(@NotNull AbstractPack pack, @NotNull String file, @NotNull Throwable cause) {
        super(pack, "Error while reading/parsing file '" + file + "' from pack '" + pack +  "'.", cause);
    }

    public PackContentException(@NotNull AbstractPack pack, @NotNull String file, @NotNull Json itemInJson, @NotNull Throwable cause) {
        super(pack,
                "Error while parsing item '"
                        + itemInJson.grab("id").orDefaultIfNull("unknown id").get()
                        + "' from file '" + file + "' from pack '" + pack + "'. Item Json:\n"
                        + StringUtils.indent(itemInJson.toString(), "        ", true)
                , cause
        );
    }
}
