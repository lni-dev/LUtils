/*
 * Copyright (c) 2025 Linus Andera
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.linusdev.lutils.pack.packs;

import de.linusdev.lutils.pack.AbstractPack;
import de.linusdev.lutils.pack.packs.simple.SimpleExtractedOnDiskPack;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * This class can open a pack which is a directory on the disk (no zip, not in jar). See {@link SimpleExtractedOnDiskPack}
 * for a usable implementation.
 */
public abstract class AbstractExtractedOnDiskPack extends AbstractPack {

    protected final @NotNull Path root;

    protected AbstractExtractedOnDiskPack(@NotNull Path root) {
        this.root = root;
    }

    @Override
    public @NotNull InputStream resolve(@NotNull String file) throws IOException {
        if(file.startsWith("/"))
            file = file.substring(1);

        Path pathToFile = root.resolve(file);

        if(!Files.exists(pathToFile))
            throw new FileNotFoundException("Cannot find file '" + file + "' in " + this);

        return Files.newInputStream(root.resolve(file));
    }

    @Override
    public boolean exists(@NotNull String file) {
        if(file.startsWith("/"))
            file = file.substring(1);
        return Files.exists(root.resolve(file));
    }

    @Override
    public @NotNull String location() {
        return root.toAbsolutePath().toString();
    }

}
