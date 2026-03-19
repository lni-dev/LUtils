/*
 * Copyright (c) 2024-2026 Linus Andera
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

package de.linusdev.lutils.nat.struct.abstracts;

import de.linusdev.lutils.nat.abi.ABI;
import de.linusdev.lutils.nat.struct.annos.RequirementType;
import de.linusdev.lutils.nat.struct.generator.SimpleStaticGenerator;
import de.linusdev.lutils.nat.struct.generator.StaticGenerator;
import de.linusdev.lutils.nat.struct.info.ComplexUnionInfo;
import de.linusdev.lutils.nat.struct.info.StructVarInfo;
import de.linusdev.lutils.nat.struct.info.StructureInfo;
import de.linusdev.lutils.nat.struct.mod.ModTrackingStructure;
import de.linusdev.lutils.nat.struct.utils.ClassAndAbi;
import de.linusdev.lutils.nat.struct.utils.SSMUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public abstract class ComplexUnion extends ModTrackingStructure {

    @SuppressWarnings("unused") // accessed via reflection
    public static final @NotNull StaticGenerator GENERATOR = new ComplexUnionGenerator();

    protected Structure [] items;

    public ComplexUnion(@Nullable ABI abi, boolean trackModifications) {
        super(abi, trackModifications);
    }

    protected void init(@Nullable Structure @NotNull ... items) {
        if(items.length != 0)
            this.items = items;
    }

    @Override
    protected void useBuffer(
            @NotNull Structure mostParentStructure,
            long offset,
            @NotNull StructureInfo info
    ) {
        super.useBuffer(mostParentStructure, offset, info);
        if(items == null)
            return;
        ComplexUnionInfo cInfo = getInfo();
        StructVarInfo[] childrenInfos = cInfo.getChildrenInfo();

        int[] positions = cInfo.getPositions();

        for(int i = 0; i < items.length ; i++) {
            if(items[i] != null)
                items[i].useBuffer(mostParentStructure, offset + positions[i], childrenInfos[i].getInfo());
        }
    }

    @Override
    protected @Nullable StaticGenerator getGenerator() {
        return GENERATOR;
    }

    @Override
    public @NotNull ComplexUnionInfo getInfo() {
        return (ComplexUnionInfo) super.getInfo();
    }

    @Override
    protected void onSetInfo(@NotNull StructureInfo info) {
        super.onSetInfo(info);

        if(items == null)
            items = getInfo().getChildren(this);
    }

    @Override
    protected @Nullable StructureInfo generateInfo() {
        return SSMUtils.getInfo(GENERATOR, this.getClass(), abi, null, null);
    }

    private static class ComplexUnionGenerator extends SimpleStaticGenerator {
        private final @NotNull Map<ClassAndAbi, ComplexUnionInfo> INFO_MAP = new HashMap<>();
        private final @NotNull Object INFO_MAP_LOCK = new Object();

        protected ComplexUnionGenerator() {
            super(
                    RequirementType.NOT_SUPPORTED,
                    RequirementType.NOT_SUPPORTED
            );
        }

        @Override
        public @NotNull StructureInfo calculateInfoChecked(@NotNull Class<?> selfClazz, @NotNull ABI abi, int[] length, @NotNull Class<?>[] elementTypes) {
            synchronized (INFO_MAP_LOCK) {
                ClassAndAbi key = new ClassAndAbi(selfClazz, abi);
                ComplexUnionInfo info = INFO_MAP.get(key);

                if(info == null) {
                    info = ComplexUnionInfo.generateFromStructVars(selfClazz, abi);
                    INFO_MAP.put(key, info);
                }

                return info;
            }
        }
    }
}
