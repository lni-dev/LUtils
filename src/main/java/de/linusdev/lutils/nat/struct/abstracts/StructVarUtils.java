package de.linusdev.lutils.nat.struct.abstracts;

import de.linusdev.lutils.nat.abi.ABI;
import de.linusdev.lutils.nat.abi.OverwriteChildABI;
import de.linusdev.lutils.nat.struct.info.StructVarInfo;
import de.linusdev.lutils.nat.struct.info.StructureInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.Arrays;

public interface StructVarUtils {

    static <T> T getStructVars(
            @NotNull Class<?> clazz,
            @NotNull ABI abi,
            @Nullable OverwriteChildABI overwriteChildAbi,
            @NotNull StructVarResultCollector<T> collector
    ) {
        Field[] fields = clazz.getFields();

        StructVarInfo[] varInfos = new StructVarInfo[fields.length];
        int index = 0;
        int size = 0;

        for(Field field : fields) {
            StructVarInfo info = StructVarInfo.ofField(field, abi, overwriteChildAbi);

            if(info == null) continue;

            //get INFO
            if(info.getStructValue().value() == -1) varInfos[index++] = info;
            else varInfos[info.getStructValue().value()] = info;
            size++;
        }

        StructureInfo[] infos = new StructureInfo[size];
        for (int i = 0; i < size; i++)
            infos[i] = varInfos[i].getInfo();

        return collector.collect(
                Arrays.copyOf(varInfos, size),
                infos
        );
    }

    @FunctionalInterface
    interface StructVarResultCollector<T> {

        T collect(
                @NotNull StructVarInfo @NotNull[] varInfos,
                @NotNull StructureInfo @NotNull [] infos
        );
    }

}
