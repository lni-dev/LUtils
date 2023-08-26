package de.linusdev.lutils.struct.abstracts;

import de.linusdev.lutils.struct.annos.StructureSettings;
import de.linusdev.lutils.struct.generator.StaticGenerator;
import de.linusdev.lutils.struct.info.StructureInfo;
import org.jetbrains.annotations.NotNull;

/**
 * This class shows and describes the public static fields a {@link Structure} requires.
 */
@SuppressWarnings({"DataFlowIssue", "UnnecessaryModifier"})
public interface StructureStaticVariables {

    /**
     * This {@code public static final} variable is required if {@link StructureSettings#requiresCalculateInfoMethod()} is set
     * to {@code false}. The variable must not be {@code null} (unlike this example).
     */
    @SuppressWarnings("unused")
    public static final @NotNull StructureInfo INFO = null;

    /**
     * This public static final variable is always required.
     */
    @SuppressWarnings("unused")
    public static final @NotNull StaticGenerator GENERATOR = null;


}
