/*
 * Copyright (c) 2023 Linus Andera
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

package de.linusdev.lutils.struct.abstracts;

import de.linusdev.lutils.struct.annos.FixedLength;
import de.linusdev.lutils.struct.annos.StructureSettings;
import de.linusdev.lutils.struct.generator.Language;
import de.linusdev.lutils.struct.generator.StaticGenerator;
import de.linusdev.lutils.struct.info.ComplexStructureInfo;
import de.linusdev.lutils.struct.info.StructVarInfo;
import de.linusdev.lutils.struct.info.StructureInfo;
import de.linusdev.lutils.struct.mod.ModTrackingStructure;
import de.linusdev.lutils.struct.utils.SSMUtils;
import de.linusdev.lutils.struct.utils.Utils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

@StructureSettings(requiresCalculateInfoMethod = true)
public abstract class ComplexStructure extends ModTrackingStructure {

    protected Structure [] items;

    private static final @NotNull Map<Class<?>, ComplexStructureInfo> INFO_MAP = new HashMap<>();
    private static final @NotNull Object INFO_MAP_LOCK = new Object();

    @SuppressWarnings("unused")
    public static final @NotNull StaticGenerator GENERATOR = new StaticGenerator() {

        @Override
        public @NotNull ComplexStructureInfo calculateInfo(@NotNull Class<?> selfClazz, @Nullable FixedLength fixedLength) {
            synchronized (INFO_MAP_LOCK) {
                ComplexStructureInfo info = INFO_MAP.get(selfClazz);
                if(info == null) {
                    info = ComplexStructureInfo.generateFromStructVars(selfClazz);
                    INFO_MAP.put(selfClazz, info);
                }

                return info;
            }
        }

        @Override
        public @NotNull String generateStructCode(@NotNull Language language, @NotNull Class<?> selfClazz, @NotNull StructureInfo info) {
            ComplexStructureInfo cInfo = (ComplexStructureInfo) info;
            StringBuilder sb = new StringBuilder();

            int @NotNull [] sizes = cInfo.getSizes();
            @NotNull StructVarInfo @NotNull [] childrenInfo = cInfo.getChildrenInfo();

            int paddingIndex = 0;

            sb.append(language.getStartStructString(true, getStructTypeName(language, selfClazz, info)));

            for(int i = 0; i < sizes.length; i++) {
                if(sizes[i] == 0) continue;
                String text;
                if((((i-1) % 2) == 0)) {
                    StructVarInfo childInfo = childrenInfo[(i - 1)/2];
                    StaticGenerator childGenerator = SSMUtils.getGenerator(childInfo.getClazz(), null);
                    text = childGenerator.getStructVarDef(language, childInfo.getClazz(), childInfo.getInfo(), childInfo.getVarName());
                } else {
                    StringBuilder pad = new StringBuilder();
                    paddingIndex = language.addPadding(pad, sizes[i], paddingIndex);
                    text = pad.toString();
                }

                sb.append(Utils.indent(text, "    ")).append("\n");
            }

            sb.append(language.getEndStructString(true, getStructTypeName(language, selfClazz, info)));

            return sb.toString();
        }

        @Override
        public @NotNull String getStructTypeName(@NotNull Language language, @NotNull Class<?> selfClazz, @NotNull StructureInfo info) {
            String name = selfClazz.getSimpleName();

            if(name.endsWith("Struct"))
                name = name.substring(0, name.length() - "Struct".length());

            if(name.endsWith("Structure"))
                name = name.substring(0, name.length() - "Structure".length());

            return name;
        }
    };

    public ComplexStructure(boolean trackModifications) {
        super(trackModifications);
    }

    protected void init(boolean allocateBuffer, @Nullable Structure @NotNull ... items) {
        this.items = items.length == 0 ? getInfo().getChildren(this) : items;

        if(allocateBuffer)
            allocate();
    }

    @Override
    protected void useBuffer(@NotNull Structure mostParentStructure, int offset) {
        super.useBuffer(mostParentStructure, offset);
        if(items == null)
            return;
        StructureInfo info = getInfo();

        int[] sizes = info.getSizes();

        int position = 0;
        for(int i = 0; i < items.length ; i++) {
            position += sizes[i * 2];
            if(items[i] != null)
                items[i].useBuffer(mostParentStructure, offset + position);
            position += sizes[i * 2 + 1];
        }

    }

    @Override
    public @NotNull ComplexStructureInfo getInfo() {
        return (ComplexStructureInfo) GENERATOR.calculateInfo(getClass(), null);
    }

    @Override
    public String toString() {
        ComplexStructureInfo cInfo = getInfo();
        StringBuilder sb = new StringBuilder();

        int @NotNull [] sizes = cInfo.getSizes();
        @NotNull StructVarInfo @NotNull [] childrenInfo = cInfo.getChildrenInfo();

        for(int i = 0; i < sizes.length; i++) {
            if(sizes[i] == 0) continue;
            String text;
            if((((i-1) % 2) == 0)) {
                StructVarInfo childInfo = childrenInfo[(i - 1)/2];

                text = childInfo.getVarName() + ": " + childInfo.get(this).toString() + "\n";
            } else {
                text = "padding: { size=" + sizes[i] + " }\n";
            }

            sb.append(Utils.indent(text, "    ")).append("\n");
        }

        return toString(GENERATOR.getStructTypeName(Language.OPEN_CL, getClass(), cInfo), sb.toString());
    }
}
