package de.linusdev.lutils.codegen.java;

import org.jetbrains.annotations.NotNull;

public interface JavaAnnotateable {

    @NotNull JavaAnnotation addAnnotation(@NotNull JavaClass annotationClass);

    void addAnnotation(@NotNull JavaAnnotation javaAnnotation);

}
