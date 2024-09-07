/*
 * Copyright (c) 2024 Linus Andera
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
