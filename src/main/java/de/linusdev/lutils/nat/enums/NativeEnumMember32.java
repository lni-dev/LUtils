package de.linusdev.lutils.nat.enums;

/**
 * Java does not support to set a custom ordinal value of an enum member.
 * In C the value of an enum can be any arbitrary 32 bit int value. This interface aims to solve this problem.
 * <br><br>
 * This interface should be implemented by Java enums, that correspond to native 32 bit enums.
 * Variables that hold a value of this enum should be of type {@link NativeEnumValue32}.
 */
@SuppressWarnings("unused")
public interface NativeEnumMember32 {

    /**
     * The value used in the native enum member counterpart to this enum member.
     */
    int getValue();

}
