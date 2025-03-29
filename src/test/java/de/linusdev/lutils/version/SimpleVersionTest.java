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

package de.linusdev.lutils.version;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SimpleVersionTest {

    @Test
    void test() {
        SimpleVersion v1 = SimpleVersion.of(1, 2, 3);
        assertEquals(1, v1.major());
        assertEquals(2, v1.minor());
        assertEquals(3, v1.patch());

        SimpleVersion v2 = SimpleVersion.of("1.2.3");
        assertEquals(1, v2.major());
        assertEquals(2, v2.minor());
        assertEquals(3, v2.patch());

        assertEquals("1.2.3", v2.getAsUserFriendlyString());
        assertEquals("1.2.3", v1.getAsUserFriendlyString());
    }

}