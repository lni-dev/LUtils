/*
 * Copyright (c) 2023 Linus Andera all rights reserved
 */

package de.linusdev.lutils.bitfield;

public enum SomeEnum implements IntBitFieldValue {

    NOTHING(0),
    A(1),
    B(1<<1),
    C(1<<2),
    D(1<<3),
    E(1<<4),

    ;

    private final int value;

    SomeEnum(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return value;
    }
}
