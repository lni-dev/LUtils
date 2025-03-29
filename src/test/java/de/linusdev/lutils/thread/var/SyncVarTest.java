/*
 * Copyright (c) 2025 Linus Andera
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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SyncVarTest {

    @Test
    public void test() {
        SyncVar<Object> var = SyncVar.createSyncVar();

        assertNull(var.get());
        Object a = new  Object();
        var.set(a);
        assertEquals(var.get(), a);

        assertFalse(var.setIfNull(new Object()));


        Object b = new Object();

        var.set(null);

        assertTrue(var.setIfNull(b));
        assertEquals(var.get(), b);
        assertNotEquals(var.get(), a);

    }

    @Test
    public void test2() {
        Object a = new  Object();
        SyncVar<Object> var = SyncVar.createSyncVar(a);

        assertNotNull(var.get());
        assertEquals(var.get(), a);

        assertFalse(var.setIfNull(new Object()));


        Object b = new Object();

        var.set(null);

        assertTrue(var.setIfNull(b));
        assertEquals(var.get(), b);
        assertNotEquals(var.get(), a);

    }

}