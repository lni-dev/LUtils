package de.linusdev.lutils.math.matrix.buffer;

import de.linusdev.lutils.math.matrix.Matrix;
import de.linusdev.lutils.math.matrix.MatrixMemoryLayout;
import de.linusdev.lutils.math.vector.buffer.BBVectorInfo;
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
public abstract class BBMatrix extends Structure implements Matrix {

    protected final @NotNull BBMatrixGenerator generator;
    protected ArrayInfo.ArrayPositionFunction positions;
    protected @NotNull MatrixMemoryLayout memoryLayout = MatrixMemoryLayout.ROW_MAJOR;

    protected BBMatrix(
            @NotNull BBMatrixGenerator generator,
            boolean generateInfo,
            @Nullable StructValue structValue
    ) {
        this.generator = generator;
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
     * position of given {@code x} and {@code y} in {@link #byteBuf}
     */
    protected int posInBuf(int y, int x) {
        return positions.position(positionToIndex(y, x));
    }

    /**
     * position of given {@code index} in {@link #byteBuf}
     */
    protected int posInBuf(int index) {
        return positions.position(index);
    }

    @Override
    protected @Nullable StructureInfo generateInfo() {
        return SSMUtils.getInfo(
                this.getClass(),
                null, null, null, null, null,
                generator
        );
    }

    @Override
    protected void onSetInfo(@NotNull StructureInfo info) {
        super.onSetInfo(info);

        positions = ((BBVectorInfo) info).getPositions();
    }

    @Override
    public @NotNull BBMatrixInfo getInfo() {
        return (BBMatrixInfo) super.getInfo();
    }

    @Override
    public int getWidth() {
        return getInfo().getWidth();
    }

    @Override
    public int getHeight() {
        return getInfo().getHeight();
    }

    @Override
    public @NotNull Structure getStructure() {
        return this;
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
    public @NotNull MatrixMemoryLayout getMemoryLayout() {
        return memoryLayout;
    }

    @Override
    public void setMemoryLayout(@NotNull MatrixMemoryLayout memoryLayout) {
        this.memoryLayout = memoryLayout;
    }

    public static class BBMatrixGenerator implements StaticGenerator {

        private final int width;
        private final int height;
        private final @NotNull NativeType type;

        public BBMatrixGenerator(int width, int height, @NotNull NativeType type) {
            this.width = width;
            this.height = height;
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
            return BBMatrixInfo.create(abi, width, height, type);
        }

        @Override
        public @Nullable StructCodeGenerator codeGenerator() {
            return new StructCodeGenerator() {
                @Override
                public @NotNull String getStructTypeName(@NotNull Language language, @NotNull Class<?> selfClazz, @NotNull StructureInfo info) {
                    BBMatrixInfo bbInfo = (BBMatrixInfo) info;
                    return language.getNativeTypeName(bbInfo.getType()) + bbInfo.getHeight() + "x" + bbInfo.getWidth();
                }

                @Override
                public @NotNull String getStructVarDef(
                        @NotNull Language language,
                        @NotNull Class<?> selfClazz,
                        @NotNull StructureInfo info,
                        @NotNull String varName
                ) {
                    BBMatrixInfo bbInfo = (BBMatrixInfo) info;
                    return language.getNativeTypeName(bbInfo.getType()) + " " + varName + "[" + (bbInfo.getHeight() * bbInfo.getWidth()) + "]"
                            + language.lineEnding;
                }
            };
        }
    }
}
