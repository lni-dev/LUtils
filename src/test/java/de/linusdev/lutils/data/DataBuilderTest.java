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

package de.linusdev.lutils.data;

import de.linusdev.lutils.optional.OptionalValue;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DataBuilderTest {

    @Test
    void orderedKnownSize() {
        DataBuilder data = DataBuilder.orderedKnownSize(10);

        data.add("key0", "value0");
        data.add("key1", "value1");
        data.addIfNotNull("key2", null);
        data.addIfNotNull("key3", "notNull");
        data.addIfOptionalExists("key4", OptionalValue.of());
        data.addIfOptionalExists("key5", OptionalValue.of("exists"));

        assertEquals("value0", data.get("key0"));
        assertEquals("value1", data.get("key1"));
        assertEquals("notNull", data.get("key3"));
        assertEquals("exists", data.get("key5"));
        assertEquals(4, data.size());

        assertEquals("""
                {
                \t"key0": "value0",
                \t"key1": "value1",
                \t"key3": "notNull",
                \t"key5": "exists"
                }""", data.toJsonString()
        );
    }

    @Test
    void orderedDynamicSize() {
        DataBuilder data = DataBuilder.orderedDynamicSize();

        data.add("key0", "value0");
        data.add("key1", "value1");
        data.addIfNotNull("key2", null);
        data.addIfNotNull("key3", "notNull");
        data.addIfOptionalExists("key4", OptionalValue.of());
        data.addIfOptionalExists("key5", OptionalValue.of("exists"));

        assertEquals("value0", data.get("key0"));
        assertEquals("value1", data.get("key1"));
        assertEquals("notNull", data.get("key3"));
        assertEquals("exists", data.get("key5"));
        assertEquals(4, data.size());

        assertEquals("""
                {
                \t"key0": "value0",
                \t"key1": "value1",
                \t"key3": "notNull",
                \t"key5": "exists"
                }""", data.toJsonString()
        );
    }

    @Test
    void unorderedMapBacked() {
        DataBuilder data = DataBuilder.unorderedMapBacked(10);

        data.add("key0", "value0");
        data.add("key1", "value1");
        data.addIfNotNull("key2", null);
        data.addIfNotNull("key3", "notNull");
        data.addIfOptionalExists("key4", OptionalValue.of());
        data.addIfOptionalExists("key5", OptionalValue.of("exists"));

        assertEquals("value0", data.get("key0"));
        assertEquals("value1", data.get("key1"));
        assertEquals("notNull", data.get("key3"));
        assertEquals("exists", data.get("key5"));
        assertEquals(4, data.size());
    }

    @Test
    void ofMap() {
        DataBuilder data = DataBuilder.ofMap(new HashMap<>(Map.of("key0", "value0")));
        data.add("key1", "value1");
        data.addIfNotNull("key2", null);
        data.addIfNotNull("key3", "notNull");
        data.addIfOptionalExists("key4", OptionalValue.of());
        data.addIfOptionalExists("key5", OptionalValue.of("exists"));

        assertEquals("value0", data.get("key0"));
        assertEquals("value1", data.get("key1"));
        assertEquals("notNull", data.get("key3"));
        assertEquals("exists", data.get("key5"));
        assertEquals(4, data.size());
    }

    @Test
    void empty() {
        DataBuilder data = DataBuilder.empty();
        assertThrows(UnsupportedOperationException.class, () -> data.add("key0", "value0"));
        assertEquals(0, data.size());
        assertTrue(data.isEmpty());
        assertNull(data.get("key0"));

        assertEquals("{\n}", data.toJsonString());
    }

    @Test
    void wrap() {
        DataBuilder dataWrapper = DataBuilder.wrap(List.of("a", "b", "c"));

        assertEquals("""
                [
                \t"a",
                \t"b",
                \t"c"
                ]""", dataWrapper.toJsonString());
    }
}