package de.linusdev.lutils.math.vector.buffer;

import de.linusdev.lutils.math.vector.Vector;
import de.linusdev.lutils.struct.abstracts.Structure;
import de.linusdev.lutils.struct.generator.Language;
import de.linusdev.lutils.struct.generator.StaticGenerator;
import de.linusdev.lutils.struct.info.StructureInfo;
import org.jetbrains.annotations.NotNull;

public abstract class BBVector extends Structure implements Vector {

    @SuppressWarnings("unused")
    public static final @NotNull StaticGenerator GENERATOR = new StaticGenerator() {
        @Override
        public @NotNull String getStructTypeName(@NotNull Language language, @NotNull Class<?> selfClazz, @NotNull StructureInfo info) {
            BBVectorInfo bbInfo = (BBVectorInfo) info;
            return bbInfo.getElementTypeName() + bbInfo.getElementCount();
        }
    };

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

}
