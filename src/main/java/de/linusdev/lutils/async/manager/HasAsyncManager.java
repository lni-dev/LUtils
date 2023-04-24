/*
 * Copyright (c) 2023 Linus Andera all rights reserved
 */

package de.linusdev.lutils.async.manager;

import org.jetbrains.annotations.NotNull;

public interface HasAsyncManager {

    @NotNull AsyncManager getAsyncManager();

}
