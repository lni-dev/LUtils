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

import de.linusdev.lutils.interfaces.Simplifiable;
import org.intellij.lang.annotations.RegExp;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Pattern;

/**
 * This class represents an identifier which identifies something. Every identifier has
 * a {@link #type()}, {@link #namespace()} and the actual {@link #id()}. The combination of {@link #type()},
 * {@link #namespace()} and {@link #id()} must <b>uniquely</b> identify something.
 * @see #type()
 * @see #namespace()
 * @see #id()
 */
public interface Identifier extends Identifiable, Simplifiable {

    @NotNull String ANY_NAMESPACE = "*";

    @RegExp @NotNull String TYPE_REGEX = "[a-z0-9-_]+";
    @NotNull Pattern TYPE_REGEX_PATTERN = Pattern.compile("^" + TYPE_REGEX + "$");
    @RegExp @NotNull String NAMESPACE_REGEX = "(" + TYPE_REGEX + "|\\*)";
    @RegExp @NotNull String ID_REGEX = "[a-z0-9-_/]+";
    @RegExp @NotNull String IDENTIFIER_REGEX = TYPE_REGEX + ":" + NAMESPACE_REGEX + ":" + ID_REGEX;
    @NotNull Pattern IDENTIFIER_REGEX_PATTERN = Pattern.compile("^" + IDENTIFIER_REGEX + "$");

    /**
     * Creates a new {@link Identifier} of given string. Does not check if given {@code identifier} is
     * {@link #isValidIdentifier(String) valid} if assertions are disabled. If you require a validation check use
     * {@link #ofStringChecked(String)} instead.
     * @param identifier the valid identifier as string.
     * @return created {@link Identifier}.
     */
    @Contract("null -> null; !null -> !null")
    static @Nullable Identifier ofString(@Nullable String identifier) {
        if(identifier == null) return null;
        assert assertValidIdentifier(identifier);
        String[] parts = identifier.split(":");
        return IdentifierType.ofName(parts[0]).of(parts[1], parts[2]);
    }

    /**
     * Creates a new {@link Identifier} of given string. Does not check if given {@code identifier} is
     * {@link #isValidIdentifier(String) valid} if assertions are disabled. If you require a validation check use
     * {@link #ofStringChecked(String)} instead. Additionally, if the type of given {@code identifier} is not given
     * {@code expectedType}, a {@link IllegalArgumentException} is thrown.
     * @param identifier the valid identifier as string.
     * @return created {@link Identifier}.
     */
    @Contract("null, _ -> null; !null, _ -> !null")
    static @Nullable Identifier ofStringEnforceType(@Nullable String identifier, @NotNull IdentifierType expectedType) {
        Identifier ret = ofString(identifier);
        if(ret != null && ret.type() != expectedType)
            throw new IllegalArgumentException("Identifier '" + identifier + "' is of type '" + ret.type().getName()
                    + "', but type '" + expectedType.getName() + "' was expected.");
        return ret;
    }

    /**
     * Checks if given {@code possibleIdentifier} is {@link #isValidIdentifier(String) valid} and creates a new
     * {@link Identifier} of given string if it is valid.
     * @param possibleIdentifier the possible identifier as string.
     * @return created {@link Identifier}.
     * @throws IllegalArgumentException if given {@code possibleIdentifier} is not a
     * {@link #isValidIdentifier(String) valid} identifier.
     */
    @Contract("null -> null; !null -> !null")
    static @Nullable Identifier ofStringChecked(@Nullable String possibleIdentifier) {
        if(possibleIdentifier == null) return null;
        assertValidIdentifier(possibleIdentifier);
        String[] parts = possibleIdentifier.split(":");
        return IdentifierType.ofName(parts[0]).of(parts[1], parts[2]);
    }

    /**
     * @throws IllegalArgumentException if given {@code possibleIdentifier} is not a
     * {@link #isValidIdentifier(String) valid} identifier.
     */
    static boolean assertValidIdentifier(@NotNull String possibleIdentifier) {
        if(isValidIdentifier(possibleIdentifier))
            return true;
        throw new IllegalArgumentException("Illegal identifier '" + possibleIdentifier + "'.");
    }

    /**
     * Checks if given {@code possibleIdentifier} is a valid regex using {@link #IDENTIFIER_REGEX_PATTERN}.
     * @return {@code true} if given {@code possibleIdentifier} is a valid identifier, {@code false} otherwise.
     */
    static boolean isValidIdentifier(@NotNull String possibleIdentifier) {
        return IDENTIFIER_REGEX_PATTERN.matcher(possibleIdentifier).find();
    }

    /**
     * Checks if given {@code possibleIdentifier} has a {@link #type()}, {@link #namespace()} and {@link #id()}. Does <b>not</b> check if
     * {@link #type()}, {@link #namespace()} and {@link #id()} are valid.
     * @return {@code true} if given {@code possibleIdentifier} is complete as described above, {@code false} otherwise.
     */
    static boolean isCompleteIdentifier(@NotNull String possibleIdentifier) {
        if(possibleIdentifier.charAt(0) == ':' || possibleIdentifier.charAt(possibleIdentifier.length()-1) == ':')
            return false;
        String[] parts = possibleIdentifier.split(":");
        return parts.length == 3 && !parts[1].isEmpty();
    }

    /**
     * Compares if the two {@link Identifier identifiers} match. Two identifiers match,
     * if {@link #id()} is the same and {@link #namespace()} is the same or one of the identifier's
     * namespace is {@value #ANY_NAMESPACE}.
     */
    static boolean equals(@NotNull Identifiable that, @Nullable Object other) {
        if(that == other) return true;
        if(!(other instanceof Identifiable otherId)) return false;
        Identifier thatRId = that.getIdentifier();
        Identifier otherRId = otherId.getIdentifier();
        if(!otherRId.id().equals(thatRId.id())) return false;
        if(thatRId.isAnyNamespace() || otherRId.isAnyNamespace()) return true;
        return otherRId.namespace().equals(thatRId.namespace());
    }

    /**
     * Hashcode of given {@link Identifier}. Only {@link #id()} and {@link #namespace()}
     * are used to create the hashcode.
     */
    static int hashCode(@NotNull Identifier that) {
        return 31 * that.id().hashCode() + that.namespace().hashCode();
    }

    /**
     * Returns the identifier as string in the following format: {@code type:namespace:id}
     */
    static @NotNull String toString(@NotNull Identifier that) {
        return that.type().getName() + ":" + that.namespace() + ":" + that.id();
    }

    /**
     * Returns the identifier as string in the following format: {@code namespace:id}
     */
    static @NotNull String toStringNoType(@NotNull Identifier that) {
        return that.namespace() + ":" + that.id();
    }

    /* ================================================================================================= *\
    |                                                                                                     |
    |                                             Normal Methods                                          |
    |                                                                                                     |
    \* ================================================================================================= */

    /**
     * The type of an identifier specifies what kind of object it identifies.
     * <br><br>
     * Only small letters, numbers, hyphens and underscores are allowed (Regex: {@value #TYPE_REGEX}). Underscores
     * should only be used if necessary and hyphens should be preferred.
     */
    @NotNull IdentifierType type();

    /**
     * The namespace of the identifier specifies who created the identifier. For example, it could be the name of an
     * application, person or class.
     * <br><br>
     * The special #namespace {@value #ANY_NAMESPACE} is reserved and represents an identifier where any
     * namespace matches.
     * <br><br>
     * Only small letters, numbers, hyphens and underscores are allowed (Regex: {@value #NAMESPACE_REGEX}). Underscores
     * should only be used if necessary and hyphens should be preferred.
     */
    @NotNull String namespace();

    /**
     * The id is the actual unique id.
     * <br><br>
     * Only small letters, numbers, hyphens, slashes and underscores are allowed (Regex: {@value #ID_REGEX}). Underscores
     * and hyphens should only be used if necessary and underscores should be preferred.
     */
    @NotNull String id();

    /**
     * @return {@code true} if {@link #namespace()} is {@link #ANY_NAMESPACE}. Otherwise, {@code false}.
     */
    default boolean isAnyNamespace() {
        return namespace().equals(ANY_NAMESPACE);
    }

    @Override
    default Object simplify() {
        return toString(this);
    }

    default @NotNull String getAsString() {
        return toString(this);
    }

    /* ================================================================================================= *\
    |                                                                                                     |
    |                                          Identifiable Methods                                       |
    |                                                                                                     |
    \* ================================================================================================= */

    @Override
    default @NotNull Identifier getIdentifier() {
        return this;
    }

    @Override
    @NotNull
    default String getIdentifierAsString() {
        return getAsString();
    }
}
