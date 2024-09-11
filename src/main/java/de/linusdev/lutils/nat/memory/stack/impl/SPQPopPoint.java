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

import de.linusdev.lutils.nat.memory.stack.PopPoint;
import de.linusdev.lutils.nat.memory.stack.Stack;
import org.jetbrains.annotations.NotNull;

public class SPQPopPoint extends SPQSafePoint implements PopPoint {

        public SPQPopPoint(int index, @NotNull StackPointerQueue spq, @NotNull Stack stack) {
            super(index, spq, stack);
        }

        @Override
        public void close() {
            if(closed)
                throw new IllegalStateException("This SafePoint has already been checked!");
            closed = true;
            spq.popToPopPoint(this);
        }
    }