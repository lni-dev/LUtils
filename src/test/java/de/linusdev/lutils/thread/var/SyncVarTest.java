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