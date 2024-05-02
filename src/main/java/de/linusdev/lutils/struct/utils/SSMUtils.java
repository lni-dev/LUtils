package de.linusdev.lutils.struct.utils;

import de.linusdev.lutils.struct.abstracts.Structure;
import de.linusdev.lutils.struct.abstracts.StructureStaticVariables;
import de.linusdev.lutils.struct.annos.FixedLength;
import de.linusdev.lutils.struct.annos.StructureLayoutSettings;
import de.linusdev.lutils.struct.annos.StructureSettings;
import de.linusdev.lutils.struct.exception.IllegalStructVarException;
import de.linusdev.lutils.struct.generator.StaticGenerator;
import de.linusdev.lutils.struct.info.ABI;
import de.linusdev.lutils.struct.info.StructureInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * {@link Structure} static methods utils
 */
public interface SSMUtils {

    /**
     * Get the {@link StructureInfo StructureInfo INFO} public static final variable ({@link StructureStaticVariables#INFO this}) of given {@code structClass}.
     * @param structClass the class of the structure, whose info shall be acquired.
     * @param errorField If an error is thrown, the error will be associated with this {@link Field}
     * @see StructureStaticVariables#INFO
     */
    private static StructureInfo getInfo(
            @NotNull Class<?> structClass,
            @Nullable Field errorField
    ) {

        Field infoField;
        try {
            infoField = structClass.getField("INFO");
        } catch (NoSuchFieldException e) {
            if(errorField != null)
                throw (IllegalStructVarException) new IllegalStructVarException(errorField,
                        structClass.getCanonicalName() + " is missing 'public static StructureInfo INFO' variable")
                        .initCause(e);
            throw new IllegalStateException(structClass.getCanonicalName() + " is missing 'public static StructureInfo INFO' variable");
        }

        try {
            return (StructureInfo) infoField.get(null);
        } catch (IllegalAccessException e) {
            if (errorField != null)
                throw (IllegalStructVarException) new IllegalStructVarException(errorField,
                        "Cannot access 'public static StructureInfo INFO' variable of " + structClass.getCanonicalName())
                        .initCause(e);
            throw new IllegalStateException("Cannot access 'public static StructureInfo INFO' variable of " + structClass.getCanonicalName());
        }
    }

    /**
     * Get the {@link StructureInfo} of given {@code structClass}.
     * @param structClass the class of the structure, whose info shall be acquired.
     * @param fl the {@link FixedLength} annotation this struct is annotated with or {@code null}
     * @param errorField If an error is thrown, the error will be associated with this {@link Field}
     * @param generator The {@link StaticGenerator} of the structure or {@code null} if it was not yet acquired. (will be automatically acquired by this method)
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

    /**
     * Get the {@link StaticGenerator} ({@link StructureStaticVariables#GENERATOR this}) of given {@code structClass}.
     * @param structClass the class of the structure, whose generator shall be acquired.
     * @param errorField If an error is thrown, the error will be associated with this {@link Field}.
     * @see StructureStaticVariables#GENERATOR
     */
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


    static @NotNull ABI getABI(
            @NotNull Class<?> structClass
    ) {
        StructureLayoutSettings layout = structClass.getAnnotation(StructureLayoutSettings.class);

        if(layout.selectorMethodName().isBlank())
            return layout.value();

        Class<?> selectorMethodClass = layout.selectorMethodClass();

        try {
            Method selectorMethod = selectorMethodClass.getMethod(layout.selectorMethodName());
            return (ABI) selectorMethod.invoke(null);
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("public static ABI " + layout.selectorMethodName() + "() method in class '" + selectorMethodClass.getCanonicalName() + "' not found.", e);
        } catch (InvocationTargetException e) {
            throw new IllegalStateException("public static ABI " + layout.selectorMethodName() + "() method in class '" + selectorMethodClass.getCanonicalName() + "' threw an exception.", e);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("public static ABI " + layout.selectorMethodName() + "() method in class '" + selectorMethodClass.getCanonicalName() + "' not accessible. Is it public?", e);
        }
    }
}
