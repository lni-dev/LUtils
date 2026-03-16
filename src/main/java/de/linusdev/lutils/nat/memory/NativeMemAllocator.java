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

package de.linusdev.lutils.nat.memory;

import de.linusdev.lutils.nat.struct.abstracts.Structure;
import org.jetbrains.annotations.NotNull;

public interface NativeMemAllocator {

    @NotNull NativeMemBuffer allocate(long size);

    default @NotNull NativeMemBuffer allocate(@NotNull Structure structure) {
        NativeMemBuffer buffer = allocate(structure.getRequiredSize());
        structure.claimMemory(buffer);
        return buffer;
    }

}
