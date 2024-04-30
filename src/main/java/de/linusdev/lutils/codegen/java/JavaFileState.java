package de.linusdev.lutils.codegen.java;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class JavaFileState {

    private final @NotNull JavaPackage currentPackage;

    private final @NotNull List<JavaImport> imports = new ArrayList<>();


    public JavaFileState(
            @NotNull JavaPackage currentPackage
    ) {
        this.currentPackage = currentPackage;
    }

    public boolean addImport(@Nullable JavaImport toAdd) {
        if(toAdd == null)
            return false;

        // Classes in the same package don't have to be imported
        if(currentPackage.equals(toAdd.getPackage()))
            return false;

        // Check if This import is already present
        for(JavaImport jm : imports) {
            if(jm.contains(toAdd))
                return false;
        }

        return imports.add(toAdd);
    }

    public @NotNull List<JavaImport> getImports() {
        return imports;
    }

}
