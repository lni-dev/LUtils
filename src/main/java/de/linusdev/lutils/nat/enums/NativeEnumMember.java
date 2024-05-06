package de.linusdev.lutils.nat.enums;

/**
 * Java does not support to set a custom ordinal value of an enum member.
 * In C the value of an enum can be any arbitrary int value. This interface aims to solve this problem.
 */
@SuppressWarnings("unused")
public interface NativeEnumMember {

    /**
     * The value used in the native enum member counterpart to this enum member.
     */
    int getValue();

}
