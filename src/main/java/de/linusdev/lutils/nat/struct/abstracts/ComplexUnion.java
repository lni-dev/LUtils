package de.linusdev.lutils.nat.struct.abstracts;

import de.linusdev.lutils.nat.abi.ABI;
import de.linusdev.lutils.nat.abi.OverwriteChildABI;
import de.linusdev.lutils.nat.struct.annos.RequirementType;
import de.linusdev.lutils.nat.struct.annos.StructValue;
import de.linusdev.lutils.nat.struct.annos.StructureSettings;
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

@StructureSettings(requiresCalculateInfoMethod = true, customLayoutOption = RequirementType.OPTIONAL)
public abstract class ComplexUnion extends ModTrackingStructure {

    @SuppressWarnings("unused") // accessed via reflection
    public static final @NotNull StaticGenerator GENERATOR = new ComplexUnionGenerator();

    protected Structure [] items;

    protected ComplexUnion(boolean trackModifications) {
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
        ComplexUnionInfo cInfo = getInfo();
        StructVarInfo[] childrenInfos = cInfo.getChildrenInfo();

        int[] positions = cInfo.getPositions();

        for(int i = 0; i < items.length ; i++) {
            if(items[i] != null)
                items[i].useBuffer(mostParentStructure, offset + positions[i], childrenInfos[i].getInfo());
        }
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
        return SSMUtils.getInfo(
                this.getClass(),
                null, null, null, null, null,
                GENERATOR
        );
    }

    private static class ComplexUnionGenerator implements StaticGenerator {
        private final @NotNull Map<ClassAndAbi, ComplexUnionInfo> INFO_MAP = new HashMap<>();
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
                ComplexUnionInfo info = INFO_MAP.get(key);

                if(info == null) {
                    info = ComplexUnionInfo.generateFromStructVars(selfClazz, abi, overwriteChildAbi);
                    INFO_MAP.put(key, info);
                }

                return info;
            }
        }
    }
}
