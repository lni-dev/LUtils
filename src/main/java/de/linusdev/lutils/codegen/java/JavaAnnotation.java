package de.linusdev.lutils.codegen.java;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class JavaAnnotation {

    protected final @NotNull JavaClass type;
    protected @Nullable Map<JavaVariable, JavaExpression> values;

    public JavaAnnotation(@NotNull JavaClass type) {
        this.type = type;
    }

    public @NotNull JavaClass getType() {
        return type;
    }

    public @Nullable Map<JavaVariable, JavaExpression> getValues() {
        return values;
    }

    public <T> void setValue(@NotNull JavaVariable variable, @NotNull JavaExpression expression) {
        if(values == null)
            values = new HashMap<>();
        values.put(variable, expression);
    }
}
