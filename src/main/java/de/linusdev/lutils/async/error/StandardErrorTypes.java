/*
 * Copyright (c) 2022-2025 Linus Andera
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

package de.linusdev.lutils.async.error;

import de.linusdev.lutils.interfaces.Simplifiable;
import org.jetbrains.annotations.NotNull;

public enum StandardErrorTypes implements ErrorType, Simplifiable {

    THROWABLE,
    FILE_ALREADY_EXISTS,
    ;

    @Override
    public String simplify() {
        return this.toString();
    }

    @Override
    public @NotNull String getName() {
        return this.toString();
    }
}
