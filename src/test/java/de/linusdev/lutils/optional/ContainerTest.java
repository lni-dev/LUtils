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

package de.linusdev.lutils.optional;

import de.linusdev.lutils.optional.impl.BasicContainer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContainerTest {

    @Test
    void requireNotNull() {

        Container<String> con = new BasicContainer<>(null, true, "test");
        BasicContainer<String> con1 = new BasicContainer<>(null, true, null);

        assertTrue(con.exists());
        assertFalse(con.isNull());
        assertDoesNotThrow(() -> con.requireNotNull());
        assertDoesNotThrow(() -> con.requireExists());

        assertTrue(con1.exists());
        assertTrue(con1.isNull());
        assertThrows(NullPointerException.class, con1::requireNotNull);
        assertDoesNotThrow(() -> con1.requireExists());

    }
}