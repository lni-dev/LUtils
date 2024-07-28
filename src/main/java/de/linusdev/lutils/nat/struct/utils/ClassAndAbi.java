package de.linusdev.lutils.nat.struct.utils;

import de.linusdev.lutils.nat.abi.ABI;
import org.jetbrains.annotations.NotNull;

public class ClassAndAbi {
    private final @NotNull Class<?> clazz;
    private final @NotNull ABI abi;

    public ClassAndAbi(@NotNull Class<?> clazz, @NotNull ABI abi) {
        this.clazz = clazz;
        this.abi = abi;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClassAndAbi)) return false;

        ClassAndAbi that = (ClassAndAbi) o;
        return clazz.equals(that.clazz) && abi.identifier().equals(that.abi.identifier());
    }

    @Override
    public int hashCode() {
        int result = clazz.hashCode();
        result = 31 * result + abi.identifier().hashCode();
        return result;
    }
}
