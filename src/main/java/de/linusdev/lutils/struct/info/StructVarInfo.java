package de.linusdev.lutils.struct.info;

import de.linusdev.lutils.struct.abstracts.ComplexStructure;
import de.linusdev.lutils.struct.abstracts.Structure;
import de.linusdev.lutils.struct.utils.SSMUtils;
import de.linusdev.lutils.struct.annos.FixedLength;
import de.linusdev.lutils.struct.annos.StructValue;
import de.linusdev.lutils.struct.exception.IllegalStructVarException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;

public class StructVarInfo {

    public static @Nullable StructVarInfo ofField(@NotNull Field field) {
        @NotNull Class<?> fieldClass = field.getType();
        StructValue sv = field.getAnnotation(StructValue.class);
        FixedLength fl = field.getAnnotation(FixedLength.class);

        if(sv == null) return null;

        StructureInfo info = SSMUtils.getInfo(fieldClass, fl, field, null);
        //noinspection unchecked: Checked in above method
        return new StructVarInfo(sv, fl, (Class<? extends Structure>) fieldClass, field.getName(), info, field);
    }

    protected final @NotNull StructValue structValue;
    protected final @Nullable FixedLength fixedLength;
    protected final @NotNull Class<? extends Structure> clazz;
    protected final @NotNull String varName;
    protected final @NotNull StructureInfo info;
    protected final @NotNull Field field;

    public StructVarInfo(
            @NotNull StructValue structValue,
            @Nullable FixedLength fixedLength,
            @NotNull Class<? extends Structure> clazz,
            @NotNull String varName,
            @NotNull StructureInfo info,
            @NotNull Field field) {
        this.structValue = structValue;
        this.fixedLength = fixedLength;
        this.clazz = clazz;
        this.varName = varName;
        this.info = info;
        this.field = field;
    }

    public @NotNull StructValue getStructValue() {
        return structValue;
    }

    public @Nullable FixedLength getFixedLength() {
        return fixedLength;
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

    public Structure get(@NotNull ComplexStructure instance) {
        try {
            return (Structure) field.get(instance);
        } catch (IllegalAccessException e) {
            throw (IllegalStructVarException) new IllegalStructVarException(field, "Cannot access field.").initCause(e);
        }
    }
}
