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

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.ServiceLoader;

class IdentifierTypesRegistry {

    /**
     * All currently registered types. New types must be registered using a {@link IdentifierTypesProviderService}.
     */
    static final @NotNull HashMap<String, IdentifierType> registeredTypes = new HashMap<>();

    static {
        ServiceLoader<IdentifierTypesProviderService> providers = ServiceLoader.load(IdentifierTypesProviderService.class);

        for (IdentifierTypesProviderService provider : providers) {
            provider.provide().forEach(IdentifierTypesRegistry::registerType);
        }
    }

    @ApiStatus.Internal
    static void registerType(@NotNull IdentifierType type) {

        if(!Identifier.TYPE_REGEX_PATTERN.matcher(type.getName()).find()) {
            throw new IllegalArgumentException("Given identifier type '" + type.getName() + "' has an invalid type name." +
                    " Type name must match regex '" + Identifier.TYPE_REGEX + "'.");
        }

        synchronized (registeredTypes) {
            if(registeredTypes.get(type.getName()) != null) {
                throw new IllegalStateException("A type with the name '" + type.getName() + "' is already registered!");
            }
            registeredTypes.put(type.getName(), type);
        }
    }

}
