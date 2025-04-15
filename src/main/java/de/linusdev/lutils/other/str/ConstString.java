package de.linusdev.lutils.other.str;

import org.jetbrains.annotations.NotNull;

public class ConstString implements ConstructableString {

    private final @NotNull String value;

    public ConstString(@NotNull String value) {
        this.value = value;
    }

    @Override
    public @NotNull String construct(@NotNull PartsString.Resolver resolver) {
        return value;
    }

    @Override
    public boolean isConstant() {
        return true;
    }
}
