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

import de.linusdev.lutils.other.UnknownConstantException;
import org.jetbrains.annotations.NotNull;

import static de.linusdev.lutils.id.IdentifierTypesRegistry.registeredTypes;

/**
 * Instances of this interface define a type of an {@link Identifier}. Each type must be
 * registered using a {@link IdentifierTypesProviderService}.
 */
public interface IdentifierType {

    /**
     * Get the {@link IdentifierType} for given type {@code name}. Searches through all
     * {@link IdentifierTypesRegistry#registeredTypes registered types}.
     */
    static @NotNull IdentifierType ofName(@NotNull String name) {
        var type = registeredTypes.get(name);
        if(type == null)
            throw new UnknownConstantException(name);
        return type;
    }

    /**
     * The name of the type. Must comply to the rules defined in {@link Identifier#type()}.
     */
    @NotNull String getName();

    /**
     * Creates a new {@link Identifier} with this type, given {@code namespace} and given {@code id}.
     */
    default @NotNull Identifier of(@NotNull String namespace, @NotNull String id) {
        return new IdentifierImpl(this, namespace, id);
    }

    /**
     * Same as {@link Identifier#ofStringEnforceType(String, IdentifierType)} with this type.
     */
    @SuppressWarnings("unused")
    default @NotNull Identifier ofString(@NotNull String identifier) {
        return Identifier.ofStringEnforceType(identifier, this);
    }

    /**
     * Same as {@link Identifier#ofStringEnforceType(String, IdentifierType)} with this type and calls
     * {@link Identifier#assertValidIdentifier(String)} before.
     */
    @SuppressWarnings("unused")
    default @NotNull Identifier ofStringChecked(@NotNull String identifier) {
        Identifier.assertValidIdentifier(identifier);
        return Identifier.ofStringEnforceType(identifier, this);
    }
}
