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

import de.linusdev.lutils.pack.Resources;
import de.linusdev.lutils.pack.errors.PackValidationException;
import de.linusdev.lutils.pack.item.Resource;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Builder class for a collection of {@link ValidationError ValidationErrors}. If no errors have been added
 * to this validation result builder, the result can be considered successful.
 */
public class ValidationResultBuilder {

    protected final @NotNull Resources resources;
    protected final @NotNull List<ValidationError> errors;

    public ValidationResultBuilder(@NotNull Resources resources) {
        this.resources = resources;
        this.errors = new ArrayList<>(0);
    }

    /**
     * Add an {@link ValidationError}.
     * @param message see {@link ValidationError}
     * @param source see {@link ValidationError}
     * @return this
     */
    public @NotNull ValidationResultBuilder error(@NotNull String message, @NotNull Resource source) {
        errors.add(new ValidationError(message, source));
        return this;
    }

    /**
     * Get a new {@link ResourceBoundValidationResultBuilder} which will add errors with given {@code resource} as error-source
     * to this builder.
     * @param resource the error-source for all errors that will be added to the returned resource bound validation result builder.
     * @return new {@link ResourceBoundValidationResultBuilder} as described above.
     */
    public @NotNull ResourceBoundValidationResultBuilder withFixedErrorSource(@NotNull Resource resource) {
        return new ResourceBoundValidationResultBuilder(this, resource);
    }

    /**
     * Whether this builder contains at least one {@link ValidationError}.
     */
    public boolean hasError() {
        return !errors.isEmpty();
    }

    /**
     * Throw a {@link PackValidationException} if {@link #hasError()} returns {@code true}.
     * @throws PackValidationException as described above.
     */
    public void throwOnError() throws PackValidationException {
        if(hasError()) {
            throw new PackValidationException(errors);
        }
    }
}
