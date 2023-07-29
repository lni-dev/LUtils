/*
 * Copyright (c) 2023 Linus Andera all rights reserved
 */

package de.linusdev.lutils.async;

import de.linusdev.lutils.async.completeable.CompletableFuture;
import de.linusdev.lutils.async.completeable.CompletableTask;
import de.linusdev.lutils.async.error.ThrowableAsyncError;
import de.linusdev.lutils.async.exception.NonBlockingThreadException;
import de.linusdev.lutils.async.executable.ExecutableFuture;
import de.linusdev.lutils.async.executable.ExecutableTask;
import de.linusdev.lutils.async.executable.ExecutableTaskBase;
import de.linusdev.lutils.async.manager.AsyncManager;
import de.linusdev.lutils.async.manager.AsyncQueue;
import de.linusdev.lutils.async.queue.Queueable;
import de.linusdev.lutils.async.queue.QueueableFuture;
import de.linusdev.lutils.async.queue.QueueableBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;

public class TestAsyncManager implements AsyncManager, AsyncQueue<Nothing> {
    @Override
    public void checkThread() throws NonBlockingThreadException {

    }

    @Override
    public void onExceptionInListener(@NotNull Future<?, ?> future, @Nullable Task<?, ?> task, @NotNull Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public void queue(@NotNull QueueableFuture<?, Nothing> future) {
        try {
            System.out.println("queue");
            future.executeHere();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void completableFuture() throws InterruptedException {
        TestAsyncManager asyncManager = new TestAsyncManager();
        var future = CompletableFuture.<String, Nothing>create(asyncManager, false);

        new Thread(() -> {
            try {
                Thread.sleep(3000);
                //complete the future in a different Thread
                future.complete("Hello", Nothing.INSTANCE, null);
            } catch (Throwable e) {
                future.complete(null, Nothing.INSTANCE,
                        new ThrowableAsyncError(e));
            }
        }).start();

        //End User:
        Future<String, Nothing> returnedFuture = future;
        String res = returnedFuture.getResult();
        System.out.println("Result: " + res);
    }

    @Test
    public void completableTask() throws InterruptedException {
        TestAsyncManager asyncManager = new TestAsyncManager();
        CompletableTask<String, Nothing> task = new CompletableTask<>(asyncManager) {
            @Override
            public void start(
                    @NotNull CompletableFuture<String, Nothing, CompletableTask<String, Nothing>> future
            ) {
                new Thread(() -> {
                    try {
                        Thread.sleep(3000);
                        //complete the future in a different Thread
                        future.complete("Hello", Nothing.INSTANCE, null);
                    } catch (Throwable e) {
                        future.complete(null, Nothing.INSTANCE,
                                new ThrowableAsyncError(e));
                    }
                }).start();
            }
        };

        Task<String, Nothing> taskToReturn = task;

        //End User:
        Future<String, Nothing> f = taskToReturn.queue();
        String res = f.getResult();
        System.out.println("Result: " + res);
    }

    @Test
    public void executableTask() throws InterruptedException {
        TestAsyncManager manager = new TestAsyncManager();

        ExecutableTask<String, Nothing> task = new ExecutableTaskBase<>(manager) {
            @Override
            protected void launch(@NotNull ExecutableFuture<String, Nothing, ExecutableTaskBase<String, Nothing>> future) {
                new Thread(() -> {
                    try {
                        future.executeHere();
                    } catch (InterruptedException e) {
                       e.printStackTrace();
                    }
                }).start();
            }

            @Override
            public @NotNull ComputationResult<String, Nothing> execute() throws InterruptedException {
                Thread.sleep(5000);
                return new ComputationResult<>("Woooow", Nothing.INSTANCE, null);
            }
        };

        task.queue((result, secondary) -> System.out.println(result)).get();
    }

    @Test
    public void queueable() throws InterruptedException {
        TestAsyncManager manager = new TestAsyncManager();


        QueueableBase<String, Nothing> queueable = new QueueableBase<>(manager) {
            @Override
            public @NotNull ComputationResult<String, Nothing> execute() throws InterruptedException {
                //Execute task
                Thread.sleep(3000);
                return new ComputationResult<>("test", Nothing.INSTANCE, null);
            }
        };


        Queueable<String, Nothing> qableToReturn = queueable;

        //End User:
        String result = qableToReturn.queue().getResult();
        System.out.println("Result: " + result);
    }
}
