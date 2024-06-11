package de.linusdev.lutils.nat.array;

import de.linusdev.lutils.nat.NativeType;
import de.linusdev.lutils.nat.abi.OverwriteChildABI;
import de.linusdev.lutils.nat.struct.abstracts.Structure;
import de.linusdev.lutils.nat.struct.annos.RequirementType;
import de.linusdev.lutils.nat.struct.annos.StructValue;
import de.linusdev.lutils.nat.struct.annos.StructureSettings;
import de.linusdev.lutils.nat.struct.generator.StaticGenerator;
import de.linusdev.lutils.nat.struct.info.ArrayInfo;
import de.linusdev.lutils.nat.struct.info.StructureInfo;
import de.linusdev.lutils.nat.abi.ABI;
import de.linusdev.lutils.nat.struct.utils.SSMUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;

@StructureSettings(
        requiresCalculateInfoMethod = true,
        customLengthOption = RequirementType.REQUIRED,
        customLayoutOption = RequirementType.OPTIONAL
)
public abstract class NativePrimitiveTypeArray<T> extends Structure implements NativeArray<T> {

    /**
     * Function to calculate the position for an element at a specific index.
     * Will always be not {@code null} after {@link #useBuffer(Structure, int, StructureInfo)} has been called.
     * That means, this struct must be {@link #allocate() allocated}, {@link #claimBuffer(ByteBuffer) claimed a buffer}
     * or {@link #useBuffer(Structure, int, StructureInfo) usebuffer called}.
     */
    protected ArrayInfo.ArrayPositionFunction positions;

    protected NativePrimitiveTypeArray(
            @Nullable StructValue structValue,
            boolean generateInfo,
            @NotNull StaticGenerator generator
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

    @Override
    public int length() {
        return getInfo().getLength();
    }

    @Override
    protected void useBuffer(@NotNull Structure mostParentStructure, int offset, @NotNull StructureInfo info) {
        super.useBuffer(mostParentStructure, offset, info);
        positions = getInfo().getPositions();
    }

    @Override
    public @NotNull ArrayInfo getInfo() {
        return (ArrayInfo) super.getInfo();
    }

    /**
     * @see #positions
     */
    public @NotNull ArrayInfo.ArrayPositionFunction getPositions() {
        if(positions == null)
            throw new IllegalStateException("Cannot get positions function, because this struct has not yet been allocated or claimed a buffer.");
        return positions;
    }

    public static class PrimitiveArrayStaticGenerator implements StaticGenerator {

        private final NativeType nativeType;

        public PrimitiveArrayStaticGenerator(NativeType nativeType) {
            this.nativeType = nativeType;
        }

        @Override
        public @NotNull StructureInfo calculateInfo(
                @NotNull Class<?> selfClazz,
                @Nullable StructValue structValue,
                @NotNull StructValue @NotNull [] elementsStructValue,
                @NotNull ABI abi,
                @Nullable OverwriteChildABI overwriteChildAbi
        ) {
            assert structValue != null; // required per StructureSettings annotation.

            return abi.calculateArrayLayout(
                    false,
                    nativeType.getMemorySizeable(abi.types()),
                    structValue.length()[0],
                    -1
            );
        }
    }
}
