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

package de.linusdev.lutils.interfaces;

/**
 * A Runnable, that has a return value and can throw a throwable.
 * @see AdvRunnable
 * @param <R> return type
 * @param <E> possible throwable type
 */
@SuppressWarnings("unused")
@FunctionalInterface
public interface AdvTRunnable<R, E extends Throwable> {

    R run() throws E;

}
