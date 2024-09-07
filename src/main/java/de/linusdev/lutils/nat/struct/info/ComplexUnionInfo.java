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

package de.linusdev.lutils.nat.struct.info;

import de.linusdev.lutils.nat.abi.ABI;
import de.linusdev.lutils.nat.abi.OverwriteChildABI;
import de.linusdev.lutils.nat.struct.abstracts.ComplexStructure;
import de.linusdev.lutils.nat.struct.abstracts.ComplexUnion;
import de.linusdev.lutils.nat.struct.abstracts.StructVarUtils;
import de.linusdev.lutils.nat.struct.abstracts.Structure;
import de.linusdev.lutils.nat.struct.annos.StructValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ComplexUnionInfo extends UnionInfo {

    /**
     * Reads all fields of given {@code clazz} annotated with {@link StructValue}
     * and generates a {@link ComplexUnionInfo} from it.
     * @param clazz class extending {@link ComplexUnion}
     * @return {@link ComplexUnionInfo} for this structure
     */
    public static @NotNull ComplexUnionInfo generateFromStructVars(
            @NotNull Class<?> clazz,
            @NotNull ABI abi,
            @Nullable OverwriteChildABI overwriteChildAbi
    ) {
        return StructVarUtils.getStructVars(
                clazz, abi, overwriteChildAbi,
                (varInfos, infos) -> {
                    UnionInfo info = abi.calculateUnionLayout(false, infos);
                    return new ComplexUnionInfo(info, abi, varInfos);
                }
        );
    }

    /**
     * {@link ABI} used to create this info.
     */
    protected final @NotNull ABI abi;

    /**
     * information about values inside the structure
     */
    protected final @NotNull StructVarInfo @NotNull [] childrenInfo;

    private ComplexUnionInfo(
            @NotNull UnionInfo info,
            @NotNull ABI abi,
            @NotNull StructVarInfo @NotNull [] infos
    ) {
        super(
                info.alignment,
                info.compressed,
                info.sizes[0], info.sizes[1], info.sizes[2], // pre-padding, size, post-padding
                info.positions
        );
        this.abi = abi;
        this.childrenInfo = infos;
    }

    /**
     * @see #abi
     */
    public @NotNull ABI getABI() {
        return abi;
    }

    /**
     * @see #childrenInfo
     */
    public @NotNull StructVarInfo @NotNull [] getChildrenInfo() {
        return childrenInfo;
    }

    /**
     * Gets all children through reflection. This should be used sparsely. But it is required if this {@link StructureInfo}
     * was automatically generated (see {@link #generateFromStructVars(Class, ABI, OverwriteChildABI)}) with no {@link StructValue#value() element order} specified.
     * @param instance instance of the {@link ComplexStructure} this info belongs to
     * @return children {@link Structure} array
     */
    public @NotNull Structure @NotNull [] getChildren(@NotNull ComplexUnion instance) {
        Structure[] structures = new Structure[childrenInfo.length];

        for(int i = 0; i < structures.length; i++) {
            structures[i] = childrenInfo[i].get(instance);
        }

        return structures;
    }

}
