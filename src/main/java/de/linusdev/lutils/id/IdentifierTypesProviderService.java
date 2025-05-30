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

package de.linusdev.lutils.id;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Classes implementing this interface can provide new {@link IdentifierType identifier types}.
 * <br><br>
 * Implementing classes must have a default constructor and their fully qualified class name  must be added to the
 * service resource-file located in:
 * <pre>{@code META-INF/services/de.linusdev.lutils.id.IdentifierTypesProviderService}</pre>
 */
public interface IdentifierTypesProviderService {

    /**
     * The {@link IdentifierType identifier types} this service provides.
     */
    @NotNull List<@NotNull IdentifierType> provide();

}
