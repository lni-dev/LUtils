/*
 * Copyright (c) 2026 Linus Andera
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

package de.linusdev.lutils.nat.memory.allocator;

import de.linusdev.lutils.nat.memory.Allocators;
import de.linusdev.lutils.nat.memory.MemoryIdType;
import de.linusdev.lutils.other.log.TestLogger;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MallocAllocatorTest {


    @Test
    void test() throws InterruptedException {
        var mem = Allocators.MALLOC_ALLOCATOR.allocOwned(20, MemoryIdType.TYPE.of("lutils", "malloc-test-1"));

        System.out.print("Do stuff");
        for (int i = 0; i < 50; i++) {
            mem.setInt(4, i);
            Thread.sleep(10);
        }
        System.out.println();

        mem.setInt(4, 9);
        mem = Allocators.MALLOC_ALLOCATOR.allocOwned(20, MemoryIdType.TYPE.of("lutils", "malloc-test-2"));

        System.gc();
        System.gc();
        Thread.sleep(1000);

        assertTrue(TestLogger.lastErrorMsg.contains("and the id 'native-memory:lutils:malloc-test-1' was never closed, but its representing object is about to be garbage collected."));

        mem.setInt(4, 9);

    }
}