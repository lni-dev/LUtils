/*
 * Copyright (c) 2023-2024 Linus Andera
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

package de.linusdev.lutils.thread.var;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class SyncVarImpl<T> implements SyncVar<T>{

    private final @NotNull Object lock = new Object();

    private @Nullable T var;

    public SyncVarImpl(@Nullable T var) {
        this.var = var;
    }

    @Override
    public @Nullable T get() {
        return var;
    }

    @Override
    public void doSynchronised(@NotNull Consumer<@NotNull SyncVar<T>> consumer) {
        synchronized (lock) {
            consumer.accept(this);
        }
    }

    @Override
    public <S> S computeSynchronised(@NotNull Function<@NotNull SyncVar<T>, S> computer) {
        synchronized (lock) {
            return computer.apply(this);
        }
    }

    @Override
    public boolean consumeIfNotNull(@NotNull Consumer<@NotNull T> consumer) {
        synchronized (lock) {
            if(var != null){
                consumer.accept(var);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean setIfNull(@Nullable T value) {
        synchronized (lock) {
            if(var == null) {
                var = value;
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean setIfNull(@NotNull Supplier<T> supplier) {
        synchronized (lock) {
            if(var == null) {
                var = supplier.get();
                return true;
            }
        }

        return false;
    }

    @Override
    public void set(@Nullable T value) {
        synchronized (lock) {
            var = value;
        }
    }
}
