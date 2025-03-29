/*
 * Copyright (c) 2023-2025 Linus Andera
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

package de.linusdev.lutils.bitfield;

import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

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
    public void testIsSetBitField() {
        IntBitfieldImpl<SomeEnum> bitfield = new IntBitfieldImpl<>(SomeEnum.D, SomeEnum.B, SomeEnum.A);

        IntBitfieldImpl<SomeEnum> bitfield2 = new IntBitfieldImpl<>(SomeEnum.D, SomeEnum.B);
        IntBitfieldImpl<SomeEnum> bitfield3 = new IntBitfieldImpl<>(SomeEnum.D, SomeEnum.B, SomeEnum.C);

        assertTrue(bitfield.isSet(bitfield2));
        assertFalse(bitfield.isSet(bitfield3));
    }

    @Test
    public void testReplaceWithBitField() {
        IntBitfieldImpl<SomeEnum> bitfield = new IntBitfieldImpl<>(SomeEnum.D, SomeEnum.B, SomeEnum.A, SomeEnum.E);

        IntBitfieldImpl<SomeEnum> bitfield2 = new IntBitfieldImpl<>(SomeEnum.D, SomeEnum.B, SomeEnum.E);
        IntBitfieldImpl<SomeEnum> bitfield3 = new IntBitfieldImpl<>(SomeEnum.D, SomeEnum.B, SomeEnum.C);

        assertTrue(bitfield.isSet(bitfield2));
        assertFalse(bitfield.isSet(bitfield3));

        bitfield.replaceWith(bitfield3);

        assertFalse(bitfield.isSet(bitfield2));
        assertTrue(bitfield.isSet(bitfield3));
    }

    @Test
    public void testToList() {

        IntBitfieldImpl<SomeEnum> bitfield = new IntBitfieldImpl<>(SomeEnum.D, SomeEnum.B, SomeEnum.A, SomeEnum.E);

        System.out.println(bitfield.toList(SomeEnum.class));

        assertIterableEquals(
                Stream.of(SomeEnum.D, SomeEnum.B, SomeEnum.A, SomeEnum.E).sorted().toList(),
                bitfield.toList(SomeEnum.class).stream().sorted().toList()
        );

    }

}
