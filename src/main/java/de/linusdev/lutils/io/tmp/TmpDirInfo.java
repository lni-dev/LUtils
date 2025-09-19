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

package de.linusdev.lutils.io.tmp;

import de.linusdev.lutils.data.Data;
import de.linusdev.lutils.data.DataBuilder;
import de.linusdev.lutils.data.Datable;
import de.linusdev.lutils.data.json.Json;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public record TmpDirInfo(
        long created,
        boolean delete,
        long deleteAfter
) implements Datable {

    public static @NotNull TmpDirInfo ofJson(@NotNull Json json) {
        long created = json.getNumberOrDefault("created", System.currentTimeMillis()).longValue();
        boolean delete = json.getAsOrDefault("delete", false);
        long deleteAfter = json.getNumberOrDefault("deleteAfter", TimeUnit.DAYS.toMillis(7)).longValue();

        return new TmpDirInfo(created, delete, deleteAfter);
    }

    @Override
    public @NotNull Data getData() {
        return DataBuilder.orderedKnownSize(2)
                .add("created", created)
                .add("delete", delete)
                .add("deleteAfter", deleteAfter);
    }
}
