package de.linusdev.lutils.nat.abi;

import de.linusdev.lutils.nat.struct.annos.StructureSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum OverwriteChildABI {
    /**
     * ABI of the children will not be overwritten
     */
    NO_OVERWRITE,

    /**
     * ABI of the children will be overwritten. If {@link StructureSettings} of a child does not
     * support {@link StructureSettings#customLayoutOption()}, this child's ABI will not be overwritten
     * and <b>no</b> exception will be thrown.
     */
    TRY_OVERWRITE,

    /**
     * ABI of the children will be overwritten. If {@link StructureSettings} of any child does not
     * support {@link StructureSettings#customLayoutOption()} and the ABI differs of that child's ABI, an exception will be thrown.
     */
    FORCE_OVERWRITE

    ;

    public static @NotNull OverwriteChildABI max(@Nullable OverwriteChildABI first, @Nullable OverwriteChildABI second) {
        if(first == TRY_OVERWRITE || second == TRY_OVERWRITE) {
            return TRY_OVERWRITE;
        }

        if(first == FORCE_OVERWRITE || second == FORCE_OVERWRITE) {
            return FORCE_OVERWRITE;
        }

        return NO_OVERWRITE;
    }
}
