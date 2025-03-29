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

package de.linusdev.lutils.other;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ArgUtilsTest {

    @Test
    void requireGreater() {
        assertThrows(IllegalArgumentException.class, () -> ArgUtils.requireGreater(0, 0, "test"));
        assertDoesNotThrow(() -> ArgUtils.requireGreater(1, 0, "test"));

        assertThrows(IllegalArgumentException.class, () -> ArgUtils.requireGreater(0.0, 0.0, 1.0, "test"));
        assertDoesNotThrow(() -> ArgUtils.requireGreater(1, 0, 0.9, "test"));
    }

    @Test
    void requireGreaterOrEqual() {
        assertThrows(IllegalArgumentException.class, () -> ArgUtils.requireGreaterOrEqual(-1, 0, "test"));
        assertDoesNotThrow(() -> ArgUtils.requireGreaterOrEqual(0, 0, "test"));
    }

    @Test
    void requireLess() {
        assertThrows(IllegalArgumentException.class, () -> ArgUtils.requireLess(0, 0, "test"));
        assertDoesNotThrow(() -> ArgUtils.requireLess(-1, 0, "test"));    }

    @Test
    void requireLessOrEqual() {
        assertThrows(IllegalArgumentException.class, () -> ArgUtils.requireLessOrEqual(1, 0, "test"));
        assertDoesNotThrow(() -> ArgUtils.requireLessOrEqual(0, 0, "test"));
    }
}