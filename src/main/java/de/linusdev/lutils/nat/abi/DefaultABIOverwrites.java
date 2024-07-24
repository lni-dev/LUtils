package de.linusdev.lutils.nat.abi;

import de.linusdev.lutils.nat.struct.annos.StructureLayoutSettings;
import org.jetbrains.annotations.NotNull;

/**
 * @see #MSVC_X64
 * @see #CVG4J_OPEN_CL
 */
@SuppressWarnings("unused")
public class DefaultABIOverwrites {

    public static final @NotNull Class<?> MSVC_X64 = Ows.MSVC_X64.class;
    public static final @NotNull Class<?> CVG4J_OPEN_CL = Ows.CVG4J_OPEN_CL.class;

    public static class Ows {
        @StructureLayoutSettings(DefaultABIs.MSVC_X64)
        public static class MSVC_X64 {}

        @StructureLayoutSettings(DefaultABIs.CVG4J_OPEN_CL)
        public static class CVG4J_OPEN_CL {}
    }

}
