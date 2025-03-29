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

package de.linusdev.lutils.other.str;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConstructableStringTest {

    @Test
    void construct() {
        ConstructableString str = new ConstructableString(
                new Part[]{
                        Part.constant("Hello "),
                        Part.placeholder("name"),
                        Part.constant(", how are you?")
                }
        );


        assertEquals("Hello World, how are you?", str.construct(Map.of("name", "World")));
    }
}