/*
 * Copyright (c) 2026 Linus Andera
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

package de.linusdev.lutils.nat.memory.allocator;

import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.SymbolLookup;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;

public class MallocAllocator extends MustFreeAllocator {

    private static final MethodHandle MALLOC_HANDLE;
    private static final MethodHandle MFREE_HANDLE;

    static {
        Linker linker = Linker.nativeLinker();
        SymbolLookup stdlib = linker.defaultLookup();

        MALLOC_HANDLE = linker.downcallHandle(
                stdlib.find("malloc").orElse(null),
                FunctionDescriptor.of(ValueLayout.JAVA_LONG, ValueLayout.JAVA_LONG)
        );

        MFREE_HANDLE = linker.downcallHandle(
                stdlib.find("free").orElse(null),
                FunctionDescriptor.ofVoid(ValueLayout.JAVA_LONG)
        );
    }

    @Override
    protected long allocateInternal(long size) {
        try {
            return (long) MALLOC_HANDLE.invokeExact((long) size);

        } catch (Throwable t) {
            throw new Error(t);
        }
    }

    @Override
    protected void freeInternal(long address) {
        try {
            MFREE_HANDLE.invokeExact(address);
        } catch (Throwable t) {
            throw new Error(t);
        }
    }
}
