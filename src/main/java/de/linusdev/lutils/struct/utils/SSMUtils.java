package de.linusdev.lutils.struct.utils;

import de.linusdev.lutils.struct.abstracts.Structure;
import de.linusdev.lutils.struct.abstracts.StructureStaticVariables;
import de.linusdev.lutils.struct.annos.FixedLength;
import de.linusdev.lutils.struct.annos.StructureSettings;
import de.linusdev.lutils.struct.exception.IllegalStructVarException;
import de.linusdev.lutils.struct.generator.StaticGenerator;
import de.linusdev.lutils.struct.info.StructureInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;

/**
 * {@link Structure} static methods utils
 */
public interface SSMUtils {

    private static StructureInfo getInfo(@NotNull Class<?> clazz, @Nullable Field errorField) {

        Field infoField;
        try {
            infoField = clazz.getField("INFO");
        } catch (NoSuchFieldException e) {
            if(errorField != null)
                throw (IllegalStructVarException) new IllegalStructVarException(errorField,
                        clazz.getCanonicalName() + " is missing 'public static StructureInfo INFO' variable")
                        .initCause(e);
            throw new IllegalStateException(clazz.getCanonicalName() + " is missing 'public static StructureInfo INFO' variable");
        }

        try {
            return (StructureInfo) infoField.get(null);
        } catch (IllegalAccessException e) {
            if (errorField != null)
                throw (IllegalStructVarException) new IllegalStructVarException(errorField,
                        "Cannot access 'public static StructureInfo INFO' variable of " + clazz.getCanonicalName())
                        .initCause(e);
            throw new IllegalStateException("Cannot access 'public static StructureInfo INFO' variable of " + clazz.getCanonicalName());
        }
    }

    /**
     * @see StructureStaticVariables#INFO
     * @see StaticGenerator#calculateInfo(Class, FixedLength) StaticGenerator.calculateInfo(...)
     */
    static @NotNull StructureInfo getInfo(
            @NotNull Class<?> structClass,
            @Nullable FixedLength fl,
            @Nullable Field errorField,
            @Nullable StaticGenerator generator
            ) {
        StructureSettings settings = structClass.getAnnotation(StructureSettings.class);

        if(!Structure.class.isAssignableFrom(structClass)) {
            if(errorField != null)
                throw new IllegalStructVarException(errorField, "Class is not a sub class of Structure.");

            throw new IllegalStateException(structClass.getCanonicalName() + " does not extend Structure.");
        }

        if(settings != null && settings.requiresCalculateInfoMethod()) {

            if(fl == null && settings.requiresFixedLengthAnnotation()) {
                if(errorField != null)
                    throw new IllegalStructVarException(errorField,
                            "Structure requires fixed length annotation, but @FixedLength annotation not given.");
                throw new IllegalStateException("Structure requires fixed length annotation, but @FixedLength annotation not given.");
            }

            return (generator == null ? getGenerator(structClass, errorField) : generator).calculateInfo(structClass, fl);
        }

        return getInfo(structClass, errorField);
    }

    static @NotNull StaticGenerator getGenerator(
            @NotNull Class<?> structClass,
            @Nullable Field errorField
    ) {
        if(!Structure.class.isAssignableFrom(structClass)) {
            if(errorField != null)
                throw new IllegalStructVarException(errorField, "Class is not a sub class of Structure.");

            throw new IllegalStateException(structClass.getCanonicalName() + " does not extend Structure.");
        }

        Field infoField;
        try {
            infoField = structClass.getField("GENERATOR");
            return (StaticGenerator) infoField.get(null);
        } catch (NoSuchFieldException e) {
            if(errorField != null)
                throw (IllegalStructVarException) new IllegalStructVarException(errorField,
                        structClass.getCanonicalName() + " is missing 'public static StructureInfo GENERATOR' variable")
                        .initCause(e);
            throw new IllegalStateException(structClass.getCanonicalName() + " is missing 'public static StructureInfo GENERATOR' variable");
        } catch (IllegalAccessException e) {
            if (errorField != null)
                throw (IllegalStructVarException) new IllegalStructVarException(errorField,
                        "Cannot access 'public static StructureInfo INFO' variable of " + structClass.getCanonicalName())
                        .initCause(e);
            throw new IllegalStateException("Cannot access 'public static StructureInfo INFO' variable of " + structClass.getCanonicalName());
        }

    }


}
