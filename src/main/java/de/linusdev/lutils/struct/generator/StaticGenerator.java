package de.linusdev.lutils.struct.generator;

import de.linusdev.lutils.struct.abstracts.Structure;
import de.linusdev.lutils.struct.annos.FixedLength;
import de.linusdev.lutils.struct.annos.StructureSettings;
import de.linusdev.lutils.struct.info.StructureInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface StaticGenerator {
    /**
     * This method is required to be overwritten, if {@link StructureSettings#requiresCalculateInfoMethod()} is set
     * to {@code true}. In that case the return value must not be {@code null}.
     * @param selfClazz the class of the {@link Structure} itself
     * @param fixedLength the fixed length annotation if any is given
     * @see FixedLength
     */
    @SuppressWarnings("unused")
    default @NotNull StructureInfo calculateInfo(@NotNull Class<?> selfClazz, @Nullable FixedLength fixedLength) {
        //noinspection DataFlowIssue: Example only
        return null;
    }

    /**
     * Can be overwritten is struct code can be generated.
     * @param language {@link org.intellij.lang.annotations.Language}
     * @param selfClazz the class of the {@link Structure} itself
     * @param info The {@link StructureInfo} of this structure
     * @return generated struct code or {@code null} if it cannot be generated.
     */
    @SuppressWarnings("unused")
    default @Nullable String generateStructCode(
            @NotNull Language language,
            @NotNull Class<?> selfClazz,
            @NotNull StructureInfo info
    ) {
        return null;
    }

    /**
     * Must be implemented.
     * @param language {@link org.intellij.lang.annotations.Language}
     * @param selfClazz the class of the {@link Structure} itself
     * @param info The {@link StructureInfo} of this structure
     * @return struct type name
     */
    @NotNull String getStructTypeName(
            @NotNull Language language,
            @NotNull Class<?> selfClazz,
            @NotNull StructureInfo info
    );

    /**
     * Can be overwritten if required. For example for arrays.
     * @param language {@link org.intellij.lang.annotations.Language}
     * @param selfClazz the class of the {@link Structure} itself
     * @param info The {@link StructureInfo} of this structure
     * @param varName the name of the variable
     * @return struct variable definition
     */
    @SuppressWarnings("unused")
    default @NotNull String getStructVarDef(
            @NotNull Language language,
            @NotNull Class<?> selfClazz,
            @NotNull StructureInfo info,
            @NotNull String varName
    ) {
        return getStructTypeName(language, selfClazz, info) + " " + varName + language.lineEnding;
    }
}
