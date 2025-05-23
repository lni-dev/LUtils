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

package de.linusdev.lutils.thread.pool;

import de.linusdev.lutils.async.Future;
import de.linusdev.lutils.async.Task;
import de.linusdev.lutils.async.exception.NonBlockingThreadException;
import de.linusdev.lutils.async.manager.AsyncManager;
import de.linusdev.lutils.nat.memory.stack.StackFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ThreadWithStackPoolTest {

    @Test
    void execute() throws InterruptedException {

        ThreadWithStackPool pool = new ThreadWithStackPool(1, 100, new AsyncManager() {
            @Override
            public void checkThread() throws NonBlockingThreadException {

            }

            @Override
            public void onExceptionInListener(@NotNull Future<?, ?> future, @Nullable Task<?, ?> task, @NotNull Throwable throwable) {
                throwable.printStackTrace();
            }
        }, Thread::new, StackFactory.DEFAULT);

        var fut0 = pool.execute(stack -> {
            try(var a = stack.popPoint()) {
                a.pushInt();
            }
            Thread.sleep(2000);
            return 0;
        });

        var fut1 = pool.execute(stack -> {
            Thread.sleep(2000);
            return 1;
        });

        var fut2 = pool.execute(stack -> {
            Thread.sleep(2000);
            return 2;
        });

        assertEquals(3, pool.getCurrentThreadCount());
        Thread.sleep(2500);
        assertEquals(1, pool.getCurrentThreadCount());

        assertFalse(pool.isClosed());
        pool.close();

        assertEquals(0, fut0.getResult());
        assertEquals(1, fut1.getResult());
        assertEquals(2, fut2.getResult());

        assertTrue(pool.isClosed());
    }
}