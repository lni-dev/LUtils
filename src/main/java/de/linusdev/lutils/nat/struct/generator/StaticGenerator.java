package de.linusdev.lutils.nat.struct.generator;

import de.linusdev.lutils.nat.abi.OverwriteChildABI;
import de.linusdev.lutils.nat.struct.abstracts.Structure;
import de.linusdev.lutils.nat.struct.annos.StructValue;
import de.linusdev.lutils.nat.struct.annos.StructureSettings;
import de.linusdev.lutils.nat.abi.ABI;
import de.linusdev.lutils.nat.struct.info.StructureInfo;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @see #calculateInfo(Class, StructValue, StructValue[], ABI, OverwriteChildABI) 
 */
public interface StaticGenerator {

    /**
     * This method is required to be overwritten, if {@link StructureSettings#requiresCalculateInfoMethod()} is set
     * to {@code true}. In that case the return value must not be {@code null}. Before this method is called,
     * given {@code structValue} was checked if it conforms to the {@link StructureSettings} specified by given {@code selfClazz}.
     *
     * @param selfClazz           the class of the {@link Structure} itself
     * @param structValue         the fixed length annotation if any is given
     * @param elementsStructValue array of struct values for the elements.
     */
    @SuppressWarnings("unused")
    default @NotNull StructureInfo calculateInfo(
            @NotNull Class<?> selfClazz,
            @Nullable StructValue structValue,
            @NotNull StructValue @NotNull [] elementsStructValue,
            @NotNull ABI abi,
            @NotNull OverwriteChildABI overwriteChildAbi
    ) {
        //noinspection DataFlowIssue: Example only
        return null;
    }

    @ApiStatus.Experimental
    default @Nullable StructCodeGenerator codeGenerator() {
        return null;
    }

}
