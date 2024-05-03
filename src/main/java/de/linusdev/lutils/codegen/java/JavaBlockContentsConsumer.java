package de.linusdev.lutils.codegen.java;

import org.jetbrains.annotations.NotNull;

public interface JavaBlockContentsConsumer {

    void accept(@NotNull JavaBlockContents block);
    
}
