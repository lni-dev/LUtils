/*
 * Copyright (c) 2022-2023 Linus Andera
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

package de.linusdev.lutils.llist;

import org.jetbrains.annotations.Nullable;

public class LLinkedListEntry<O> {

    private volatile @Nullable O value;
    private volatile @Nullable LLinkedListEntry<O> next;

    public LLinkedListEntry(@Nullable O value) {
        this.value = value;
    }

    public @Nullable LLinkedListEntry<O> getNext() {
        return next;
    }

    public synchronized void setNext(@Nullable LLinkedListEntry<O> next) {
        this.next = next;
    }

    public @Nullable O getValue() {
        return value;
    }

    public synchronized void setValue(@Nullable O value) {
        this.value = value;
    }
}
