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

import de.linusdev.lutils.other.str.StringUtils;
import de.linusdev.lutils.pack.validation.ValidationError;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PackValidationException extends PackException {

    private static @NotNull String message(@NotNull List<ValidationError> errors) {
        StringBuilder sb = new StringBuilder();
        for (ValidationError error : errors) {
            String item = StringUtils.indent(error.message() + "\nCaused by '" + error.source().getIdentifierAsString() + "' from pack '" + error.source().getSource().name() + "'.", "   ", false);
            sb.append("\n - ").append(item);
        }

        return sb.toString();
    }

    public PackValidationException(@NotNull List<ValidationError> errors) {
        //noinspection OptionalGetWithoutIsPresent
        super(errors.stream().findFirst().get().source().getSource(), "Error while validating packs: " + message(errors), null);
    }
}
