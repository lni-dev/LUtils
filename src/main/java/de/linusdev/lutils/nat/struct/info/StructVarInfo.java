package de.linusdev.lutils.nat.struct.info;

import de.linusdev.lutils.nat.abi.ABI;
import de.linusdev.lutils.nat.abi.OverwriteChildABI;
import de.linusdev.lutils.nat.struct.abstracts.Structure;
import de.linusdev.lutils.nat.struct.annos.ElementsStructValue;
import de.linusdev.lutils.nat.struct.annos.StructValue;
import de.linusdev.lutils.nat.struct.exception.IllegalStructVarException;
import de.linusdev.lutils.nat.struct.utils.SSMUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;

public class StructVarInfo {

    public static @Nullable StructVarInfo ofField(
            @NotNull Field field,
            @Nullable ABI parentAbi,
            @Nullable OverwriteChildABI overwriteChildAbi
    ) {
        @NotNull Class<?> fieldClass = field.getType();
        StructValue sv = field.getAnnotation(StructValue.class);
        ElementsStructValue esv = field.getAnnotation(ElementsStructValue.class);

        if(sv == null) return null;

        StructureInfo info = SSMUtils.getInfo(fieldClass, sv, esv, parentAbi, overwriteChildAbi, field, null);
        //noinspection unchecked: Checked in above method
        return new StructVarInfo(sv, (Class<? extends Structure>) fieldClass, field.getName(), info, field);
    }

    protected final @NotNull StructValue structValue;
    protected final @NotNull Class<? extends Structure> clazz;
    protected final @NotNull String varName;
    protected final @NotNull StructureInfo info;
    protected final @NotNull Field field;

    public StructVarInfo(
            @NotNull StructValue structValue,
            @NotNull Class<? extends Structure> clazz,
            @NotNull String varName,
            @NotNull StructureInfo info,
            @NotNull Field field) {
        this.structValue = structValue;
        this.clazz = clazz;
        this.varName = varName;
        this.info = info;
        this.field = field;
    }

    public @NotNull StructValue getStructValue() {
        return structValue;
    }

    public @NotNull Class<? extends Structure> getClazz() {
        return clazz;
    }

    public @NotNull String getVarName() {
        return varName;
    }

    public @NotNull StructureInfo getInfo() {
        return info;
    }

    public Structure get(@NotNull Structure instance) {
        try {
            return (Structure) field.get(instance);
        } catch (IllegalAccessException e) {
            throw (IllegalStructVarException) new IllegalStructVarException(field, "Cannot access field.").initCause(e);
        }
    }
}
