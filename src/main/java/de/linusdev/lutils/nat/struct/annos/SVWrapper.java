package de.linusdev.lutils.nat.struct.annos;

import de.linusdev.lutils.nat.struct.abstracts.Structure;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;

@SuppressWarnings("ClassExplicitlyAnnotation")
public class SVWrapper implements StructValue {

    /**
     * @param length {@link StructValue#length()}
     * @param elementType {@link StructValue#elementType()}
     * @param overwriteStructureLayout {@link StructValue#overwriteStructureLayout()}
     * @see StructValue
     */
    public static @NotNull SVWrapper of(
            int length,
            @NotNull Class<?> elementType,
            @Nullable Class<?> overwriteStructureLayout
    ) {
        return new SVWrapper(new int[]{length}, new Class[]{elementType}, overwriteStructureLayout);
    }

    /**
     * @param length {@link StructValue#length()}
     * @param elementType {@link StructValue#elementType()}
     * @see StructValue
     */
    public static @NotNull SVWrapper of(
            int length,
            @NotNull Class<?> elementType
    ) {
        return new SVWrapper(new int[]{length}, new Class[]{elementType}, null);
    }

    /**
     * @param length {@link StructValue#length()}
     * @see StructValue
     */
    public static @NotNull SVWrapper length(
            int length
    ) {
        return new SVWrapper(new int[]{length}, null, null);
    }

    /**
     * @param elementType {@link StructValue#elementType()}
     * @see StructValue
     */
    public static @NotNull SVWrapper elementType(
            @NotNull Class<?> elementType
    ) {
        return new SVWrapper(null, new Class[]{elementType}, null);
    }

    /**
     * @param overwriteStructureLayout {@link StructValue#overwriteStructureLayout()}
     * @see StructValue
     */
    public static @NotNull SVWrapper overwriteLayout(@Nullable Class<?> overwriteStructureLayout) {
        return new SVWrapper( null, null, overwriteStructureLayout);
    }

    private final int @NotNull [] length;
    private final @NotNull Class<?> @NotNull [] elementType;
    private final @NotNull Class<?> overwriteStructureLayout;

    public SVWrapper(
            int @Nullable [] length,
            Class<?> @Nullable [] elementType,
            @Nullable Class<?> overwriteStructureLayout
    ) {
        this.length = length == null ? new int[]{-1} : length;
        this.elementType = elementType == null ? new Class[]{Structure.class} : elementType;
        this.overwriteStructureLayout = overwriteStructureLayout == null ? Structure.class : overwriteStructureLayout;
    }

    @Override
    public int value() {
        return -1;
    }

    @Override
    public int @NotNull [] length() {
        return length;
    }

    @Override
    public @NotNull Class<?> @NotNull [] elementType() {
        return elementType;
    }

    @Override
    public @NotNull Class<?> overwriteStructureLayout() {
        return overwriteStructureLayout;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return StructValue.class;
    }
}
