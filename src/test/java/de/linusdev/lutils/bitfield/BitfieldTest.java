/*
 * Copyright (c) 2023 Linus Andera all rights reserved
 */

package de.linusdev.lutils.bitfield;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BitfieldTest {

    @Test
    public void test() {
        IntBitfieldImpl<SomeEnum> field = new IntBitfieldImpl<>();

        field.set(SomeEnum.A, SomeEnum.E);

        assertTrue(field.isSet(SomeEnum.A));
        assertTrue(field.isSet(SomeEnum.E));
        assertFalse(field.isSet(SomeEnum.B));

        field.reset();

        assertFalse(field.isSet(SomeEnum.A));
        assertFalse(field.isSet(SomeEnum.E));
        assertFalse(field.isSet(SomeEnum.B));

        field.set(SomeEnum.A, SomeEnum.B, SomeEnum.C, SomeEnum.D, SomeEnum.E);

        assertTrue(field.isSet(SomeEnum.A));
        assertTrue(field.isSet(SomeEnum.B));
        assertTrue(field.isSet(SomeEnum.C));
        assertTrue(field.isSet(SomeEnum.D));
        assertTrue(field.isSet(SomeEnum.E));

        field.unset(SomeEnum.A);

        assertFalse(field.isSet(SomeEnum.A));
    }

    @Test
    public void testSetBitField() {
        IntBitfieldImpl<SomeEnum> bitfield = new IntBitfieldImpl<>(SomeEnum.D, SomeEnum.B, SomeEnum.A);

        IntBitfieldImpl<SomeEnum> bitfield2 = new IntBitfieldImpl<>(SomeEnum.D, SomeEnum.B);
        IntBitfieldImpl<SomeEnum> bitfield3 = new IntBitfieldImpl<>(SomeEnum.D, SomeEnum.B, SomeEnum.C);

        assertTrue(bitfield.isSet(bitfield2));
        assertFalse(bitfield.isSet(bitfield3));
    }

}
