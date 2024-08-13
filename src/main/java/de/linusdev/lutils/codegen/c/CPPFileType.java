package de.linusdev.lutils.codegen.c;

import org.jetbrains.annotations.NotNull;

public enum CPPFileType {

    SOURCE_CPP("cpp"),
    SOURCE_C("c"),
    HEADER("h"),

    ;

    private final @NotNull String fileEnding;

    CPPFileType(@NotNull String fileEnding) {
        this.fileEnding = fileEnding;
    }

    public @NotNull String getFileEnding() {
        return fileEnding;
    }
}
