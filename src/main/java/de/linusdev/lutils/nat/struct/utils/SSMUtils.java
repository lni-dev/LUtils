package de.linusdev.lutils.nat.struct.utils;

import de.linusdev.lutils.codegen.java.JavaClass;
import de.linusdev.lutils.codegen.java.JavaMethod;
import de.linusdev.lutils.nat.abi.OverwriteChildABI;
import de.linusdev.lutils.nat.struct.abstracts.Structure;
import de.linusdev.lutils.nat.struct.abstracts.StructureStaticVariables;
import de.linusdev.lutils.nat.struct.annos.*;
import de.linusdev.lutils.nat.struct.exception.IllegalStructVarException;
import de.linusdev.lutils.nat.struct.generator.StaticGenerator;
import de.linusdev.lutils.nat.struct.info.StructureInfo;
import de.linusdev.lutils.nat.abi.ABI;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicReference;

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
     * Get the {@link ABI} to use for the given {@code structClass}
     * that may be a variable annotated with given {@code structValue} annotation.
     * <br><br>
     * If {@code parentAbi} is not {@code null} and {@code overwriteChildAbi} is specified with anything but {@link OverwriteChildABI#NO_OVERWRITE},
     * the {@code parentAbi} will be returned. It will also be checked, if given {@code structClass} allows this {@link ABI}.
     * <br><br>
     * If given {@code structValue} annotation specifies a certain {@link ABI}, this {@link ABI} will
     * be returned, <b>without checking if given {@code structClass} allows this.</b>
     * <br><br>
     * Otherwise, the {@link ABI} defined by the {@link StructureLayoutSettings} annotation of given {@code structClass}
     * will be returned.
     * @param structClass {@link Class} of the structure
     * @param structValue {@link StructValue} annotation, if this structure's variable is annotated with it
     * @param parentAbi {@link ABI} of the parent structure (if known)
     * @param settings {@link StructureSettings} of this structure
     * @param overwriteChildAbi If and how the {@link ABI} of the structures children should be overwritten. see {@link OverwriteChildABI}.
     *                          If this struct specifies a {@link OverwriteChildABI#max(OverwriteChildABI, OverwriteChildABI) greater}
     *                          {@link OverwriteChildABI}, this will be returned.
     * @param errorField If an error is thrown, the error will be associated with this {@link Field}
     * @return {@link ABI} to use
     */
    static @NotNull ABI getABI(
            @NotNull Class<?> structClass,
            @Nullable StructValue structValue,
            @Nullable ABI parentAbi,
            @NotNull AtomicReference<@Nullable OverwriteChildABI> overwriteChildAbi,
            @NotNull StructureSettings settings,
            @Nullable Field errorField
    ) {
        StructureLayoutSettings layout;
        @NotNull ABI abi;
        if(structValue != null && structValue.overwriteStructureLayout() != Structure.class)
            layout = structValue.overwriteStructureLayout().getAnnotation(StructureLayoutSettings.class);
        else
            layout = structClass.getAnnotation(StructureLayoutSettings.class);

        if(overwriteChildAbi.get() != null && overwriteChildAbi.get() != OverwriteChildABI.NO_OVERWRITE && settings.customLayoutOption() != RequirementType.NOT_SUPPORTED) {
            if(parentAbi == null)
                throw new IllegalStateException("parentAbi is null, but overwriteChildAbi is specified.");
            abi = parentAbi;

        } else if(layout.selectorMethodName().isBlank()) {
            abi = layout.value();

        } else {
            Class<?> selectorMethodClass = layout.selectorMethodClass();

            try {
                Method selectorMethod = selectorMethodClass.getMethod(layout.selectorMethodName());
                abi = (ABI) selectorMethod.invoke(null);
            } catch (NoSuchMethodException e) {
                throw new IllegalStateException("public static ABI " + layout.selectorMethodName() + "() method in class '" + selectorMethodClass.getCanonicalName() + "' not found.", e);
            } catch (InvocationTargetException e) {
                throw new IllegalStateException("public static ABI " + layout.selectorMethodName() + "() method in class '" + selectorMethodClass.getCanonicalName() + "' threw an exception.", e);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("public static ABI " + layout.selectorMethodName() + "() method in class '" + selectorMethodClass.getCanonicalName() + "' not accessible. Is it public?", e);
            }

        }

        if(
                overwriteChildAbi.get() == OverwriteChildABI.FORCE_OVERWRITE
                        &&
                settings.customLayoutOption() == RequirementType.NOT_SUPPORTED
                        &&
                !ABI.equals(abi, parentAbi)
        ) {
            if(errorField != null)
                throw new IllegalStructVarException(errorField, "This structures ABI cannot be overwritten, but parent requires FORCE_OVERWRITE.");

            throw new IllegalStateException("Structure " + structClass.getCanonicalName() + " does not support overwriting its ABI, but parent requires FORCE_OVERWRITE.");
        }

        overwriteChildAbi.set(OverwriteChildABI.max(overwriteChildAbi.get(), layout.overwriteChildrenABI()));
        return abi;
    }

    /**
     * Get the {@link StructureInfo} of given {@code structClass}.
     * @param structClass the class of the structure, whose info shall be acquired.
     * @param structValue the {@link StructValue} annotation this struct is annotated with or {@code null}
     * @param errorField If an error is thrown, the error will be associated with this {@link Field}
     * @param generator The {@link StaticGenerator} of the structure or {@code null} if it was not yet acquired. (will be automatically acquired by this method)
     * @param parentAbi The {@link ABI} the parent uses or {@code null} if the info of the parent is acquired / it is unknown.
     * @param overwriteChildAbi If and how the {@link ABI} of the structures children should be overwritten. see {@link OverwriteChildABI}.
     * @param elementsStructValue struct value for the structures children.
     * @see StructureStaticVariables#INFO
     * @see StaticGenerator#calculateInfo(Class, StructValue, StructValue[], ABI, OverwriteChildABI) StaticGenerator.calculateInfo(...)
     */
    static @NotNull StructureInfo getInfo(
            @NotNull Class<?> structClass,
            @Nullable StructValue structValue,
            @Nullable ElementsStructValue elementsStructValue,
            @Nullable ABI parentAbi,
            @Nullable OverwriteChildABI overwriteChildAbi,
            @Nullable Field errorField,
            @Nullable StaticGenerator generator
    ) {
        @Nullable StructureSettings settings = sanityChecks(structClass, structValue, errorField);

        // After all checks are done calculate the StructureInfo.
        if(settings != null && settings.requiresCalculateInfoMethod()) {
            AtomicReference<OverwriteChildABI> refOCA = new AtomicReference<>(overwriteChildAbi);
            ABI abi = getABI(structClass, structValue, parentAbi, refOCA, settings, errorField);

            return (generator == null ? getGenerator(structClass, errorField) : generator)
                    .calculateInfo(
                            structClass,
                            structValue,
                            elementsStructValue == null? new StructValue[0] : elementsStructValue.value(),
                            abi,
                            refOCA.get() == null ? OverwriteChildABI.NO_OVERWRITE : refOCA.get()
                    );
        }

        if(overwriteChildAbi == OverwriteChildABI.FORCE_OVERWRITE) {
            if(errorField != null)
                throw new IllegalStructVarException(errorField, "This structures ABI cannot be overwritten, but parent requires FORCE_OVERWRITE.");

            throw new IllegalStateException("Structure " + structClass.getCanonicalName() + " does not support overwriting its ABI, but parent requires FORCE_OVERWRITE.");
        }

        return getInfo(structClass, errorField);
    }

    /**
     * Checks if given {@code structValue} is valid for given {@code structClass}.
     * @param structClass the class of the structure.
     * @param structValue the {@link StructValue} annotation this struct is annotated with or {@code null}
     * @param errorField If an error is thrown, the error will be associated with this {@link Field}
     * @return {@link StructureSettings} of given {@code structClass}.
     */
    static @Nullable StructureSettings sanityChecks(
            @NotNull Class<?> structClass,
            @Nullable StructValue structValue,
            @Nullable Field errorField
    ) {
        @Nullable StructureSettings settings = structClass.getAnnotation(StructureSettings.class);
        
        // Check if structClass extends Structure.
        if(!Structure.class.isAssignableFrom(structClass)) {
            if(errorField != null)
                throw new IllegalStructVarException(errorField, "Class is not a sub class of Structure.");

            throw new IllegalStateException(structClass.getCanonicalName() + " does not extend Structure.");
        }

        // Check if structValue is valid.
        if(structValue == null) {
            if(
                    settings != null && (
                            settings.customLengthOption() == RequirementType.REQUIRED ||
                                    settings.customElementTypesOption() == RequirementType.REQUIRED ||
                                    settings.customLayoutOption() == RequirementType.REQUIRED
                    )
            ) {
                if(errorField != null)
                    throw new IllegalStructVarException(errorField, "Struct requires the StructValue annotation.");

                throw new IllegalStateException(structClass.getCanonicalName() + " requires the StructValue annotation.");
            }
        }
        else {
            // check length
            if(structValue.length()[0] != -1) {
                if(settings == null || settings.customLengthOption() == RequirementType.NOT_SUPPORTED) {
                    if(errorField != null)
                        throw new IllegalStructVarException(errorField, "Struct does not support the StructValue.length variable.");
                    throw new IllegalStateException(structClass.getCanonicalName() + " does not support the StructValue.length variable.");
                }
            } else {
                if(settings != null && settings.customLengthOption() == RequirementType.REQUIRED) {
                    if(errorField != null)
                        throw new IllegalStructVarException(errorField, "Struct requires the StructValue.length variable.");
                    throw new IllegalStateException(structClass.getCanonicalName() + " requires the StructValue.length variable.");
                }
            }

            // check element type
            if(!structValue.elementType()[0].equals(Structure.class)) {
                if(settings == null || settings.customElementTypesOption() == RequirementType.NOT_SUPPORTED) {
                    if(errorField != null)
                        throw new IllegalStructVarException(errorField, "Struct does not support the StructValue.elementTypes variable.");
                    throw new IllegalStateException(structClass.getCanonicalName() + " does not support the StructValue.elementTypes variable.");
                }
            } else {
                if(settings != null && settings.customElementTypesOption() == RequirementType.REQUIRED) {
                    if(errorField != null)
                        throw new IllegalStructVarException(errorField, "Struct requires the StructValue.elementTypes variable.");
                    throw new IllegalStateException(structClass.getCanonicalName() + " requires the StructValue.elementTypes variable.");
                }
            }

            // check overwrite layout
            if(!structValue.overwriteStructureLayout().equals(Structure.class) ) {
                if(settings == null || settings.customLayoutOption() == RequirementType.NOT_SUPPORTED) {
                    if(errorField != null)
                        throw new IllegalStructVarException(errorField, "Struct does not support the StructValue.overwriteStructureLayout variable.");
                    throw new IllegalStateException(structClass.getCanonicalName() + " does not support the StructValue.overwriteStructureLayout variable.");
                }

            } else {
                if(settings != null && settings.customLayoutOption() == RequirementType.REQUIRED) {
                    if(errorField != null)
                        throw new IllegalStructVarException(errorField, "Struct requires the StructValue.overwriteStructureLayout variable.");
                    throw new IllegalStateException(structClass.getCanonicalName() + " requires the StructValue.overwriteStructureLayout variable.");
                }
            }
        }
        
        return settings;
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

    static @NotNull StructCreationMethod getNewUnallocatedMethod(@NotNull Class<?> structClass) {
        return findMethod(structClass, "newUnallocated");
    }

    static @NotNull StructCreationMethod getNewAllocatableMethod(@NotNull Class<?> structClass) {
        return findMethod(structClass, "newAllocatable");
    }

    static @NotNull StructCreationMethod getNewAllocatedMethod(@NotNull Class<?> structClass) {
        return findMethod(structClass, "newAllocated");
    }

    private static @NotNull StructCreationMethod findMethod(@NotNull Class<?> structClass, @NotNull String methodName) {
        for(Method method : structClass.getDeclaredMethods()) {
            if(method.getName().startsWith(methodName)) {
                return  new StructCreationMethod(method);
            }
        }

        throw new UnsupportedOperationException("Given struct-class does not have a " + methodName + "() method.");
    }

    class StructCreationMethod implements JavaMethod {

        private final @NotNull Method method;
        private final @NotNull JavaClass parentClass;
        private final @NotNull JavaClass returnType;
        private final @NotNull String name;

        public StructCreationMethod(@NotNull Method method) {
            this.method = method;
            this.parentClass = JavaClass.ofClass(method.getDeclaringClass());
            this.returnType = JavaClass.ofClass(method.getReturnType());
            this.name = method.getName();
        }

        public @NotNull Method getMethod() {
            return method;
        }

        @Override
        public @NotNull JavaClass getParentClass() {
            return parentClass;
        }

        @Override
        public @NotNull JavaClass getReturnType() {
            return returnType;
        }

        @Override
        public @NotNull String getName() {
            return name;
        }
    }

}
