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

package de.linusdev.lutils.nat.memory.stack.impl;

import de.linusdev.lutils.nat.memory.stack.SafePoint;
import de.linusdev.lutils.nat.memory.stack.Stack;
import org.jetbrains.annotations.NotNull;

public class SPQSafePoint implements SafePoint {

        final int index;
        final @NotNull StackPointerQueue spq;
        final @NotNull Stack stack;
        boolean closed = false;

        public SPQSafePoint(int index, @NotNull StackPointerQueue spq, @NotNull Stack stack) {
            this.index = index;
            this.spq = spq;
            this.stack = stack;
        }

        @Override
        public @NotNull Stack getStack() {
            return stack;
        }

        @Override
        public void close() {
            if(closed)
                throw new IllegalStateException("This SafePoint has already been checked!");
            closed = true;
            spq.checkSafePoint(this);
        }
    }