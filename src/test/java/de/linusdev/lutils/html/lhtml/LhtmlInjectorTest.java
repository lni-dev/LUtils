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

package de.linusdev.lutils.html.lhtml;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class LhtmlInjectorTest {

    @Test
    void getConstructableStringOfValue() {

        assertNull(LhtmlInjector.getConstructableStringOfValue(null));
        assertNull(LhtmlInjector.getConstructableStringOfValue(""));
        assertNull(LhtmlInjector.getConstructableStringOfValue("test"));

        assertEquals("Hello World!", LhtmlInjector.getConstructableStringOfValue("Hello ${name}!").construct(Map.of("name", "World")));
        assertEquals("Hello World!", LhtmlInjector.getConstructableStringOfValue("Hello ${n1Az_-}!").construct(Map.of("n1Az_-", "World")));
        assertEquals("Hello World, wow!",
                LhtmlInjector.getConstructableStringOfValue("Hello ${n1Az_-}, ${test}!")
                        .construct(Map.of("n1Az_-", "World", "test", "wow")));

        assertEquals("Hello World, wow",
                LhtmlInjector.getConstructableStringOfValue("Hello ${n1Az_-}, ${test}")
                        .construct(Map.of("n1Az_-", "World", "test", "wow")));

        assertEquals("World, wow",
                LhtmlInjector.getConstructableStringOfValue("${n1Az_-}, ${test}")
                        .construct(Map.of("n1Az_-", "World", "test", "wow")));

    }
}