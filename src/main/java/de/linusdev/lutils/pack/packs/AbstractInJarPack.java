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

import de.linusdev.lutils.io.ResourceUtils;
import de.linusdev.lutils.pack.AbstractPack;
import de.linusdev.lutils.pack.packs.simple.SimpleInJarPack;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;

/**
 * This class can open a pack which is contained as a directory inside the executing jar. See {@link SimpleInJarPack}
 * for a usable implementation.
 */
public abstract class AbstractInJarPack extends AbstractPack {

    private static @NotNull String standardizeRootPath(@NotNull String rootPath) {
        if(!rootPath.startsWith("/"))
            rootPath = "/" + rootPath;

        if(rootPath.endsWith("/"))
            rootPath = rootPath.substring(0, rootPath.length()-1);

        return rootPath;
    }

    /**
     * The resource-path to the root of the pack.
     * Must start with a slash and not end with a slash.
     * E.g.: "/path/to/root".
     * @see #standardizeRootPath(String)
     */
    protected final @NotNull String root;

    protected AbstractInJarPack(@NotNull String root) {
        this.root = standardizeRootPath(root);
    }

    @Override
    public @NotNull InputStream resolve(@NotNull String file) throws IOException {
        if(file.startsWith("/"))
            file = file.substring(1);
        return ResourceUtils.getURLConnectionOfResource(root + "/" + file).openInputStream();

    }

    @Override
    public boolean exists(@NotNull String file) {
        if(file.startsWith("/"))
            file = file.substring(1);
        return ResourceUtils.getURLConnectionOfResource(null, root + "/" + file, true) != null;
    }

    @Override
    public @NotNull String location() {
        return "jar:" + root;
    }

}
