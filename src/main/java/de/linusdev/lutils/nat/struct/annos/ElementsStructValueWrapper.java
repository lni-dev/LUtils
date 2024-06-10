package de.linusdev.lutils.nat.struct.annos;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;

@SuppressWarnings("ClassExplicitlyAnnotation")
public class ElementsStructValueWrapper implements ElementsStructValue {

    private final @NotNull StructValue[] values;

    public ElementsStructValueWrapper(@NotNull StructValue[] values) {
        this.values = values;
    }

    @Override
    public StructValue[] value() {
        return values;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return ElementsStructValue.class;
    }
}
