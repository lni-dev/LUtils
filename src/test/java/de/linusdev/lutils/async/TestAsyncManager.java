/*
 * Copyright (c) 2023 Linus Andera all rights reserved
 */

package de.linusdev.lutils.async;

import de.linusdev.lutils.async.completeable.CompletableTask;
import de.linusdev.lutils.async.exception.NonBlockingThreadException;
import de.linusdev.lutils.async.executable.ExecutableFuture;
import de.linusdev.lutils.async.executable.ExecutableTask;
import de.linusdev.lutils.async.executable.ExecutableTaskBase;
import de.linusdev.lutils.async.manager.AsyncManager;
import de.linusdev.lutils.async.manager.AsyncQueue;
import de.linusdev.lutils.async.queue.QResponse;
import de.linusdev.lutils.async.queue.QueueableFuture;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;

public class TestAsyncManager implements AsyncManager, AsyncQueue<QResponse> {
    @Override
    public void checkThread() throws NonBlockingThreadException {

    }

    @Override
    public void onExceptionInListener(@NotNull Future<?, ?> future, @Nullable Task<?, ?> task, @NotNull Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public void queue(@NotNull QueueableFuture<?, QResponse> future) {
        try {
            future.executeHere();
            future.getTask().execute();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void test() throws InterruptedException {
        TestAsyncManager manager = new TestAsyncManager();

        CompletableTask<String, Nothing> t = new CompletableTask<>(manager);
        var future = t.consumeAndQueue(stringNothingFuture -> {
            stringNothingFuture.then((result, secondary) -> System.out.println(result));
        });


        new Thread(() -> {

            try {
                Thread.sleep(3000);
                System.out.println("complete");
                future.complete("Hallo", Nothing.INSTANCE, null);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }).start();

        String res = future.getResult();
        System.out.println("Result: " + res);
    }

    @Test
    public void test2() {
        TestAsyncManager manager = new TestAsyncManager();

        ExecutableTask<String, Nothing> task = new ExecutableTaskBase<>(manager) {
            @Override
            protected void launch(@NotNull ExecutableFuture<String, Nothing, ExecutableTaskBase<String, Nothing>> future) {
                new Thread(() -> {
                    try {
                        future.executeHere();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }).start();
            }

            @Override
            public @NotNull ComputationResult<String, Nothing> execute() throws InterruptedException {
                Thread.sleep(5000);
                return new ComputationResult<>("Woooow", Nothing.INSTANCE, null);
            }
        };

        task.queue((result, secondary) -> System.out.println(result));
    }
}
