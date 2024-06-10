package de.linusdev.lutils.math.vector.buffer;

import de.linusdev.lutils.math.vector.Vector;
import de.linusdev.lutils.nat.NativeType;
import de.linusdev.lutils.nat.abi.OverwriteChildABI;
import de.linusdev.lutils.nat.struct.abstracts.Structure;
import de.linusdev.lutils.nat.struct.annos.RequirementType;
import de.linusdev.lutils.nat.struct.annos.StructValue;
import de.linusdev.lutils.nat.struct.annos.StructureSettings;
import de.linusdev.lutils.nat.struct.generator.Language;
import de.linusdev.lutils.nat.struct.generator.StaticGenerator;
import de.linusdev.lutils.nat.struct.generator.StructCodeGenerator;
import de.linusdev.lutils.nat.struct.info.ArrayInfo;
import de.linusdev.lutils.nat.struct.info.StructureInfo;
import de.linusdev.lutils.nat.abi.ABI;
import de.linusdev.lutils.nat.struct.utils.SSMUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@StructureSettings(requiresCalculateInfoMethod = true, customLayoutOption = RequirementType.OPTIONAL)
public abstract class BBVector extends Structure implements Vector {

    protected ArrayInfo.ArrayPositionFunction positions;

    public BBVector(
            @NotNull BBVectorGenerator generator,
            boolean generateInfo,
            @Nullable StructValue structValue
    ) {
        if(generateInfo) {
            setInfo(SSMUtils.getInfo(
                    this.getClass(),
                    structValue,
                    null,
                    null,
                    null,
                    null,
                    generator
            ));
        }
    }

    /**
     * position of given {@code index} in {@link #byteBuf}
     */
    protected int posInBuf(int index) {
        return positions.position(index);
    }

    @Override
    protected void onSetInfo(@NotNull StructureInfo info) {
        super.onSetInfo(info);

        positions = ((BBVectorInfo) info).getPositions();
    }

    @Override
    public boolean isArrayBacked() {
        return false;
    }

    @Override
    public boolean isBufferBacked() {
        return true;
    }

    @Override
    public @NotNull Structure getStructure() {
        return this;
    }

    @Override
    public boolean isView() {
        return false;
    }

    public static class BBVectorGenerator implements StaticGenerator {

        private final int elementCount;
        private final @NotNull NativeType type;

        public BBVectorGenerator(int elementCount, @NotNull NativeType type) {
            this.elementCount = elementCount;
            this.type = type;
        }

        @Override
        public @NotNull StructureInfo calculateInfo(
                @NotNull Class<?> selfClazz,
                @Nullable StructValue structValue,
                @NotNull StructValue @NotNull [] elementsStructValue,
                @NotNull ABI abi,
                @Nullable OverwriteChildABI overwriteChildAbi
        ) {
            return BBVectorInfo.create(abi, elementCount, type);
        }

        @Override
        public @NotNull StructCodeGenerator codeGenerator() {
            return new StructCodeGenerator() {
                @Override
                public @NotNull String getStructTypeName(
                        @NotNull Language language,
                        @NotNull Class<?> selfClazz,
                        @NotNull StructureInfo info
                ) {
                    BBVectorInfo bbInfo = (BBVectorInfo) info;

                    return language.getNativeTypeName(bbInfo.getType()) + bbInfo.getLength();
                }
            };
        }
    }

}
