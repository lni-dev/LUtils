package de.linusdev.lutils.struct.annos;

import de.linusdev.lutils.struct.info.ABI;
import de.linusdev.lutils.struct.info.DefaultABIs;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target(ElementType.TYPE)
@Documented
public @interface StructureLayoutSettings {

    /**
     * Use this to set the {@link ABI} to use when generating the layout of the structure.
     */
    DefaultABIs value() default DefaultABIs.CVG4J;

    /**
     * Class containing the {@link #selectorMethodName()} method.
     */
    Class<?> selectorMethodClass() default Void.class;

    /**
     * Specify, that the ABI to use when generating the layout of the structure, will be selected at runtime.
     * A method with name returned from this method in class {@link #selectorMethodClass()} will be used to select an {@link ABI}.
     * The method described by this
     * Overrides {@link #value()}.
     */
    String selectorMethodName() default "";

}
