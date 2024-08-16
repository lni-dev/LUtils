package de.linusdev.lutils.nat.memory;

import de.linusdev.lutils.nat.abi.ABI;
import de.linusdev.lutils.nat.abi.OverwriteChildABI;
import de.linusdev.lutils.nat.struct.abstracts.Structure;
import de.linusdev.lutils.nat.struct.annos.RequirementType;
import de.linusdev.lutils.nat.struct.annos.StructValue;
import de.linusdev.lutils.nat.struct.annos.StructureSettings;
import de.linusdev.lutils.nat.struct.generator.StaticGenerator;
import de.linusdev.lutils.nat.struct.info.StructureInfo;
import de.linusdev.lutils.nat.struct.utils.BufferUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;

@StructureSettings(
        requiresCalculateInfoMethod = true,
        customLengthOption = RequirementType.OPTIONAL
)
public class DirectMemoryStack64 extends Structure implements Stack {

    public static final int DEFAULT_MEMORY_SIZE = 1024 * 1024; // 1 MiB
    public static final int ALIGNMENT = 8;

    private final long address;

    protected long stackPointer;
    private final StackPointerQueue stackPointers = new StackPointerQueue();

    public DirectMemoryStack64(int size) {
        setInfo(new StructureInfo(ALIGNMENT, false, 0, size, 0));
        allocate();
        this.address = getPointer();
        this.stackPointer = address;
    }

    public DirectMemoryStack64() {
        this(DEFAULT_MEMORY_SIZE);
    }

    /**
     * Pushes given {@code structure}) onto this stack and allocates (calls {@link Structure#claimBuffer(ByteBuffer) claimBuffer}).
     * @param structure unallocated {@link Structure}, which should use a part of this stack as its {@link Structure#byteBuf buffer}.
     * @return allocated {@link Structure}
     * @param <T> structure type
     */
    @Override
    public <T extends Structure> T push(@NotNull T structure) {
        stackPointers.push(stackPointer);

        StructureInfo info = structure.getOrGenerateInfo();
        int size = info.getRequiredSize();
        int alignment = info.getAlignment();
        int alignmentFix = stackPointer % alignment == 0 ? 0 : (int) (alignment - (stackPointer % alignment));
        structure.claimBuffer(BufferUtils.slice(byteBuf, (int) ((stackPointer - address) + alignmentFix), size));

        stackPointer += size + alignmentFix;

        return structure;
    }

    /**
     * Pops the last {@link #push(Structure) pushed} structure from this stack. This means, that
     * the stack pointer will be decreased to the state, before the {@link #push(Structure) push} operation.
     * <br><br>
     * The {@link Structure} will still be backed by this stack and may not be used anymore.
     */
    @Override
    public void pop() {
        stackPointer = stackPointers.pop();

    }

    /**
     * @see StackPointerQueue#createSafePoint()
     */
    public boolean createSafePoint() {
        stackPointers.createSafePoint();
        return true;
    }

    /**
     * @see StackPointerQueue#checkSafePoint()
     */
    public boolean checkSafePoint() {
        return stackPointers.checkSafePoint();
    }

    @Override
    public long memorySize() {
        return byteBuf.capacity();
    }

    @Override
    public int getAlignment() {
        return ALIGNMENT;
    }

    @Override
    public long getPointer() {
        return address;
    }

    public long getAddress() {
        return address;
    }

    // Only for completion's Sake. Cannot actually be called, as no unallocated Stack can be created.
    public static final StaticGenerator GENERATOR = new StaticGenerator() {
        @Override
        public @NotNull StructureInfo calculateInfo(
                @NotNull Class<?> selfClazz, @Nullable StructValue structValue, @NotNull StructValue @NotNull [] elementsStructValue, @NotNull ABI abi, @NotNull OverwriteChildABI overwriteChildAbi
        ) {
            int size = structValue != null && structValue.length().length > 0 ? structValue.length()[0] : DEFAULT_MEMORY_SIZE;
            return new StructureInfo(8, false, size, new int[]{0, size, 0});
        }
    };
}
