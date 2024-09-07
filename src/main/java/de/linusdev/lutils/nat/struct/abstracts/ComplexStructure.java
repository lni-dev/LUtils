/*
 * Copyright (c) 2023-2024 Linus Andera
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

import de.linusdev.lutils.nat.abi.OverwriteChildABI;
import de.linusdev.lutils.nat.struct.annos.RequirementType;
import de.linusdev.lutils.nat.struct.annos.StructValue;
import de.linusdev.lutils.nat.struct.annos.StructureSettings;
import de.linusdev.lutils.nat.struct.generator.Language;
import de.linusdev.lutils.nat.struct.generator.StaticGenerator;
import de.linusdev.lutils.nat.struct.generator.StructCodeGenerator;
import de.linusdev.lutils.nat.struct.info.ComplexStructureInfo;
import de.linusdev.lutils.nat.struct.info.StructVarInfo;
import de.linusdev.lutils.nat.struct.info.StructureInfo;
import de.linusdev.lutils.nat.abi.ABI;
import de.linusdev.lutils.nat.struct.mod.ModTrackingStructure;
import de.linusdev.lutils.nat.struct.utils.ClassAndAbi;
import de.linusdev.lutils.nat.struct.utils.SSMUtils;
import de.linusdev.lutils.nat.struct.utils.Utils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;


@StructureSettings(requiresCalculateInfoMethod = true, customLayoutOption = RequirementType.OPTIONAL)
public abstract class ComplexStructure extends ModTrackingStructure {

    @SuppressWarnings("unused") // accessed via reflection
    public static final @NotNull StaticGenerator GENERATOR = new ComplexStructureGenerator();

    protected Structure [] items;

    public ComplexStructure(
            boolean trackModifications
    ) {
        super(trackModifications);
    }

    protected void init(
            @Nullable StructValue structValue,
            boolean generateInfo,
            @Nullable Structure @NotNull ... items
    ) {
        if(items.length != 0)
            this.items = items;
        if(generateInfo) {
            setInfo(SSMUtils.getInfo(
                    this.getClass(),
                    structValue,
                    null, null, null, null,
                    GENERATOR
            ));
        }
    }

    @Override
    protected void useBuffer(
            @NotNull Structure mostParentStructure,
            int offset,
            @NotNull StructureInfo info
    ) {
        super.useBuffer(mostParentStructure, offset, info);
        if(items == null)
            return;
        ComplexStructureInfo cInfo = getInfo();
        StructVarInfo[] childrenInfos = cInfo.getChildrenInfo();

        int[] sizes = cInfo.getSizes();

        int position = 0;
        for(int i = 0; i < items.length ; i++) {
            position += sizes[i * 2];
            if(items[i] != null)
                items[i].useBuffer(mostParentStructure, offset + position, childrenInfos[i].getInfo());
            position += sizes[i * 2 + 1];
        }
    }

    @Override
    public @NotNull ComplexStructureInfo getInfo() {
        return (ComplexStructureInfo) super.getInfo();
    }

    @Override
    protected void onSetInfo(@NotNull StructureInfo info) {
        super.onSetInfo(info);

        if(items == null)
            items = getInfo().getChildren(this);
    }

    @Override
    protected @Nullable StructureInfo generateInfo() {
        return SSMUtils.getInfo(
                this.getClass(),
                null, null, null, null, null,
                GENERATOR
        );
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

        //noinspection DataFlowIssue: Always returns not null
        return toString(GENERATOR.codeGenerator().getStructTypeName(Language.OPEN_CL, getClass(), cInfo), sb.toString());
    }

    private static class ComplexStructureGenerator implements StaticGenerator {
        private final @NotNull Map<ClassAndAbi, ComplexStructureInfo> INFO_MAP = new HashMap<>();
        private final @NotNull Object INFO_MAP_LOCK = new Object();

        @Override
        public @NotNull StructureInfo calculateInfo(
                @NotNull Class<?> selfClazz,
                @Nullable StructValue structValue,
                @NotNull StructValue @NotNull [] elementsStructValue,
                @NotNull ABI abi,
                @Nullable OverwriteChildABI overwriteChildAbi
        ) {
            synchronized (INFO_MAP_LOCK) {
                ClassAndAbi key = new ClassAndAbi(selfClazz, abi);
                ComplexStructureInfo info = INFO_MAP.get(key);

                if(info == null) {
                    info = ComplexStructureInfo.generateFromStructVars(selfClazz, abi, overwriteChildAbi);
                    INFO_MAP.put(key, info);
                }

                return info;
            }
        }

        @Override
        public @NotNull StructCodeGenerator codeGenerator() {
            return new StructCodeGenerator() {
                @Override
                public @NotNull String generateStructCode(
                        @NotNull Language language,
                        @NotNull Class<?> selfClazz,
                        @NotNull StructureInfo info
                ) {
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
                            //noinspection DataFlowIssue: Experimental
                            text = childGenerator.codeGenerator().getStructVarDef(language, childInfo.getClazz(), childInfo.getInfo(), childInfo.getVarName());
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
                public @NotNull String getStructTypeName(
                        @NotNull Language language,
                        @NotNull Class<?> selfClazz,
                        @NotNull StructureInfo info
                ) {
                    String name = selfClazz.getSimpleName();

                    if(name.endsWith("Struct"))
                        name = name.substring(0, name.length() - "Struct".length());

                    if(name.endsWith("Structure"))
                        name = name.substring(0, name.length() - "Structure".length());

                    return name;
                }
            };
        }
    }
}
