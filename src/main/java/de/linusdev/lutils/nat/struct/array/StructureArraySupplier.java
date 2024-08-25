package de.linusdev.lutils.nat.struct.array;

import de.linusdev.lutils.nat.memory.DirectMemoryManager;
import de.linusdev.lutils.nat.struct.abstracts.Structure;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
@FunctionalInterface
interface StructureArraySupplier<T extends Structure> {

        /**
         * May be used by methods, that require a native array. Enables the method caller to define
         * how the array may be created (for example on a custom {@link DirectMemoryManager}).
         * @param size size of the {@link StructureArray} that should be supplied
         * @param elementClazz the elementClass for the array
         * @param creator {@link de.linusdev.lutils.nat.struct.array.StructureArray.ElementCreator ElementCreator}
         * @return {@link StructureArray} with given {@code size}, {@code elementClazz} and {@code creator}.
         */
        @NotNull StructureArray<T> supply(
                int size,
                @NotNull Class<?> elementClazz,
                @NotNull StructureArray.ElementCreator<T> creator
        );
}