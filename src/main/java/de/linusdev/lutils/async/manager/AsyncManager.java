/*
 * Copyright (c) 2023 Linus Andera all rights reserved
 */

package de.linusdev.lutils.async.manager;

import de.linusdev.lutils.async.Future;
import de.linusdev.lutils.async.Task;
import de.linusdev.lutils.async.exception.NonBlockingThreadException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public interface AsyncManager {

    void checkThread() throws NonBlockingThreadException;

    void onExceptionInListener(@NotNull Future<?, ?> future, @Nullable Task<?, ?> task, @NotNull Throwable throwable);

}
