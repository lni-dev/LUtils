/*
 * Copyright (c) 2024 Linus Andera
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
import de.linusdev.lutils.async.Nothing;
import de.linusdev.lutils.async.completeable.CompletableFuture;
import de.linusdev.lutils.async.completeable.CompletableTask;
import de.linusdev.lutils.async.error.ThrowableAsyncError;
import de.linusdev.lutils.async.manager.AsyncManager;
import de.linusdev.lutils.interfaces.TFunction;
import de.linusdev.lutils.llist.LLinkedList;
import de.linusdev.lutils.nat.memory.stack.Stack;
import de.linusdev.lutils.nat.memory.stack.StackFactory;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadFactory;
import java.util.function.Function;

public class ThreadPoolWithStack {

    private final @NotNull ThreadFactory threadFactory;
    private final @NotNull StackFactory stackFactory;

    private final @NotNull AsyncManager asyncManager;
    private final @NotNull LLinkedList<ThreadWithStack> threads;


    public ThreadPoolWithStack(
            @NotNull AsyncManager asyncManager,
            @NotNull ThreadFactory threadFactory,
            @NotNull StackFactory stackFactory
    ) {
        this.asyncManager = asyncManager;
        this.threadFactory = threadFactory;
        this.stackFactory = stackFactory;
        this.threads = new LLinkedList<>();
    }


    public <T> @NotNull Future<T, Nothing> execute(@NotNull TFunction<Stack, T, ?> runnable) {

        CompletableFuture<T, Nothing, CompletableTask<T, Nothing>> fut = CompletableFuture.create(asyncManager, false);

        Function<Stack, Runnable> fun = (stack) -> () -> {
            try {
                fut.complete(runnable.apply(stack), Nothing.INSTANCE, null);
            } catch (Throwable e) {
                fut.complete(null, Nothing.INSTANCE, new ThrowableAsyncError(e));
            }
        };


        for (ThreadWithStack thread : threads) {
            if(thread.setRunnableIfAvailable(fun)) {
                return fut;
            }
        }

        ThreadWithStack thread = new ThreadWithStack(threadFactory, stackFactory);
        if(!thread.setRunnableIfAvailable(fun))
            throw new Error();
        threads.add(thread);
        return fut;
    }



}
