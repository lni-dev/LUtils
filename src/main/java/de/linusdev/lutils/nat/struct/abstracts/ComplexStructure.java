/*
 * Copyright (c) 2023-2026 Linus Andera
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
import de.linusdev.lutils.nat.struct.annos.StructValue;
import de.linusdev.lutils.nat.struct.generator.Language;
import de.linusdev.lutils.nat.struct.generator.SimpleStaticGenerator;
import de.linusdev.lutils.nat.struct.generator.StaticGenerator;
import de.linusdev.lutils.nat.struct.generator.StructCodeGenerator;
import de.linusdev.lutils.nat.struct.info.ComplexStructureInfo;
import de.linusdev.lutils.nat.struct.info.StructVarInfo;
import de.linusdev.lutils.nat.struct.info.StructureInfo;
import de.linusdev.lutils.nat.struct.mod.ModTrackingStructure;
import de.linusdev.lutils.nat.struct.utils.ClassAndAbi;
import de.linusdev.lutils.nat.struct.utils.SSMUtils;
import de.linusdev.lutils.other.str.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;


/**
 * ComplexStructure allows easily creating structures containing only other already existing structures.
 * All classes extending ComplexStructure already contain the required {@link StructureStaticVariables#GENERATOR Generator}
 * to generate the correct {@link StructureInfo}.
 * <br>
 * How to use:<br>
 * <ol>
 *     <li>Create a new class extending ComplexStructure.</li>
 *     <li>Create public final variables typed with Subclasses of {@link Structure}. This will be your structure elements</li>
 *     <li>Annotate all variables which should be elements of your structure with {@link StructValue}</li>
 *     <li>If you require a specific ordering, you should set each {@link StructValue#value()} to the index
 *     of the element</li>
 *     <li>
 *         Create a constructor and call {@link #init(boolean, Structure...) init}.
 *         See documentation of {@link #init(boolean, Structure...) init} and the
 *         {@link #ComplexStructure(ABI, boolean) constructor} for more information.
 *     </li>
 *     <li>
 *         Optionally add the static methods {@link StructureStaticVariables#newUnallocated() newUnallocated} and
 *         {@link StructureStaticVariables#newAllocatable(ABI, int[], Class[])  newAllocatable}.
 *     </li>
 * </ol>
 * Example: {@link de.linusdev.lutils.nat.struct.examples.ExampleComplexStructure ExampleComplexStructure}. It is a
 * good idea to copy this example class and adjust according to the steps above it.
 *
 *
 */
public abstract class ComplexStructure extends ModTrackingStructure {

    @SuppressWarnings("unused") // accessed via reflection
    public static final @NotNull StaticGenerator GENERATOR = new ComplexStructureGenerator();

    protected Structure [] items;

    /**
     * Constructor for a Complex Structure.
     * @param trackModifications see {@link #trackModifications}.
     */
    public ComplexStructure(@Nullable ABI abi, boolean trackModifications) {
        super(abi, trackModifications);
    }

    /**
     * Initialise this complex structure.
     *
     * @param genInfo
     * @param items   A non-empty array may only be optionally passed if every element's {@link StructValue#value() index} is set. If
     *                this is the case the array must contain each element at the correct index. The reason to pass this
     *                parameter is only to improve performance.
     */
    protected void init(boolean genInfo, @Nullable Structure @NotNull ... items) {
        if(items.length != 0)
            this.items = items;
        if(genInfo)
            setInfo(getInfoOrFail());
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
        ComplexStructureInfo cInfo = getInfo();
        StructVarInfo[] childrenInfos = cInfo.getChildrenInfo();

        long[] sizes = cInfo.getSizes();

        long position = 0;
        for(int i = 0; i < items.length ; i++) {
            position += sizes[i * 2];
            if(items[i] != null)
                items[i].useBuffer(mostParentStructure, offset + position, childrenInfos[i].getInfo());
            position += sizes[i * 2 + 1];
        }
    }

    @Override
    protected @Nullable StaticGenerator getGenerator() {
        return GENERATOR;
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
        return GENERATOR.calculateInfo(this.getClass(), abi, null, null);
    }

    @Override
    public String toString() {
        ComplexStructureInfo cInfo = getInfo();
        StringBuilder sb = new StringBuilder();

        long @NotNull [] sizes = cInfo.getSizes();
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

            sb.append(StringUtils.indent(text, "    ", true)).append("\n");
        }

        //noinspection DataFlowIssue: Always returns not null
        return toString(GENERATOR.codeGenerator().getStructTypeName(Language.OPEN_CL, getClass(), cInfo), sb.toString());
    }

    private static class ComplexStructureGenerator extends SimpleStaticGenerator {
        private final @NotNull Map<ClassAndAbi, ComplexStructureInfo> INFO_MAP = new HashMap<>();
        private final @NotNull Object INFO_MAP_LOCK = new Object();

        protected ComplexStructureGenerator() {
            super(RequirementType.NOT_SUPPORTED, RequirementType.NOT_SUPPORTED);
        }

        @Override
        public @NotNull StructureInfo calculateInfoChecked(
                @NotNull Class<?> selfClazz,
                @NotNull ABI abi,
                int[] length,
                @NotNull Class<?>[] elementTypes
        ) {
            synchronized (INFO_MAP_LOCK) {
                ClassAndAbi key = new ClassAndAbi(selfClazz, abi);
                ComplexStructureInfo info = INFO_MAP.get(key);

                if(info == null) {
                    info = ComplexStructureInfo.generateFromStructVars(selfClazz, abi);
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

                    long @NotNull [] sizes = cInfo.getSizes();
                    @NotNull StructVarInfo @NotNull [] childrenInfo = cInfo.getChildrenInfo();

                    int paddingIndex = 0;

                    sb.append(language.getStartStructString(true, getStructTypeName(language, selfClazz, info)));

                    for(int i = 0; i < sizes.length; i++) {
                        if(sizes[i] == 0) continue;
                        String text;
                        if((((i-1) % 2) == 0)) {
                            StructVarInfo childInfo = childrenInfo[(i - 1)/2];
                            StaticGenerator childGenerator = SSMUtils.getGenerator(childInfo.getClazz());
                            //noinspection DataFlowIssue: Experimental
                            text = childGenerator.codeGenerator().getStructVarDef(language, childInfo.getClazz(), childInfo.getInfo(), childInfo.getVarName());
                        } else {
                            StringBuilder pad = new StringBuilder();
                            paddingIndex = language.addPadding(pad, Math.toIntExact(sizes[i]), paddingIndex);
                            text = pad.toString();
                        }

                        sb.append(StringUtils.indent(text, "    ", true)).append("\n");
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
