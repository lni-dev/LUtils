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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IdentifierTest {

    public static class TestType implements IdentifierType {

        private static final TestType INSTANCE = new TestType();

        @Override
        public @NotNull String getName() {
            return "test-type";
        }
    }

    public static class TestType2 implements IdentifierType {

        private static final TestType INSTANCE = new TestType();

        @Override
        public @NotNull String getName() {
            return "test-type2";
        }
    }

    @BeforeAll
    static void addTypes() {
        IdentifierTypesRegistry.registerType(TestType.INSTANCE);
    }

    @Test
    void ofString() {
        Identifier id = Identifier.ofString("test-type:ns:some-id");

        assertEquals(TestType.INSTANCE, id.type());
        assertEquals("ns", id.namespace());
        assertEquals("some-id", id.id());
        assertEquals("test-type:ns:some-id", id.toString());
        assertEquals("test-type:ns:some-id", id.getAsString());
        assertEquals("test-type:ns:some-id", Identifier.toString(id));
        assertEquals("ns:some-id", Identifier.toStringNoType(id));
        assertFalse(id.isAnyNamespace());

        id = Identifier.ofString("test-type:*:some-id");

        assertEquals(TestType.INSTANCE, id.type());
        assertEquals("*", id.namespace());
        assertEquals("some-id", id.id());
        assertEquals("test-type:*:some-id", id.toString());
        assertEquals("test-type:*:some-id", id.getAsString());
        assertEquals("test-type:*:some-id", Identifier.toString(id));
        assertEquals("*:some-id", Identifier.toStringNoType(id));
        assertTrue(id.isAnyNamespace());

        assertTrue(Identifier.isCompleteIdentifier("test-type:*:some-id"));
        assertTrue(Identifier.isCompleteIdentifier("test-type:ns:some-id"));
        assertFalse(Identifier.isCompleteIdentifier("dgeg43gt43t:"));
        assertFalse(Identifier.isCompleteIdentifier("::dgeg43gt43t:"));
        assertFalse(Identifier.isCompleteIdentifier("a::a"));

        assertDoesNotThrow(() -> Identifier.ofStringChecked("test-type:*:some-id"));
        assertDoesNotThrow(() -> Identifier.ofStringChecked("test-type:ns:some-id"));
        assertThrows(IllegalArgumentException.class, () -> Identifier.ofStringChecked("test-type*:some-id"));
        assertThrows(IllegalArgumentException.class, () -> Identifier.ofStringEnforceType("test-type:*:some-id", TestType2.INSTANCE));
        assertDoesNotThrow(() -> Identifier.ofStringEnforceType("test-type:*:some-id", TestType.INSTANCE));
    }

}