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

package de.linusdev.lutils.pack.validation;

import de.linusdev.lutils.pack.resource.Resource;
import org.jetbrains.annotations.NotNull;

/**
 * A resource related validation error.
 * @param message the error message.
 * @param source the resource that caused the error. For example if resource x is dependent resource y,
 *               but resource y is not present, the error source will be resource x.
 */
public record ValidationError(
        @NotNull String message,
        @NotNull Resource source
) {
}
