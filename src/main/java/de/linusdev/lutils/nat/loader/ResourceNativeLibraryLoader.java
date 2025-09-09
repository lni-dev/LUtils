/*
 * Copyright (c) 2023-2025 Linus Andera
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

package de.linusdev.lutils.nat.loader;

import de.linusdev.lutils.io.FileUtils;
import de.linusdev.lutils.io.ResourceUtils;
import de.linusdev.lutils.os.OsType;
import de.linusdev.lutils.os.OsUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Helper class to load native libraries from resource files.
 * It will look for a library as passed to the constructor. If it cant find a library it will also look for libraries
 * with the same name but common prefixes or, if no file ending was passed, common file endings.
 * @see OsType#getDefaultNativeLibraryFileEnding()
 * @see OsType#getPossibleNativeLibraryFilePrefixes()
 * @see System#load(String) 
 */
@SuppressWarnings("unused")
public class ResourceNativeLibraryLoader {

    private final @NotNull List<ResourceUtils.StreamURLConnection> resources;

    private static @NotNull ResourceUtils.StreamURLConnection findLib(@Nullable Class<?> caller, @NotNull String libPath) {
        ArrayList<String> triedPaths = new ArrayList<>();

        String tried = libPath;
        ResourceUtils.StreamURLConnection res = ResourceUtils.getURLConnectionOfResource(caller, libPath, true);

        if(res != null)
            return res;
        triedPaths.add(tried);

        String lib = FileUtils.getFileName(libPath);
        String path = FileUtils.getParentDirectory(libPath);

        if(FileUtils.getFileEnding(libPath) == null) {
            for (String ending : OsUtils.CURRENT_OS.getDefaultNativeLibraryFileEnding()) {

                tried = libPath + "." + ending;
                res = ResourceUtils.getURLConnectionOfResource(caller, tried, true);

                if(res != null)
                    return res;
                triedPaths.add(tried);

                for (String prefix : OsUtils.CURRENT_OS.getPossibleNativeLibraryFilePrefixes()) {
                    tried = path + prefix + lib + "." + ending;
                    res = ResourceUtils.getURLConnectionOfResource(caller, tried, true);

                    if(res != null)
                        return res;
                    triedPaths.add(tried);
                }

            }
        } else {
            for (String prefix : OsUtils.CURRENT_OS.getPossibleNativeLibraryFilePrefixes()) {
                tried = path + prefix + lib;
                res = ResourceUtils.getURLConnectionOfResource(caller, tried, true);

                if(res != null)
                    return res;
                triedPaths.add(tried);
            }
        }

        StringBuilder sb = new StringBuilder();
        for (String triedPath : triedPaths)
            sb.append("\n - ").append(triedPath);
        throw new Error("Could not find the resource '" + libPath + "'. Tried the following locations:" + sb);
    }

    /**
     * @param caller only needed if relative paths are passed
     * @param libPaths array of paths to the resource files. See {@link ResourceNativeLibraryLoader} for more information.
     */
    public ResourceNativeLibraryLoader(@Nullable Class<?> caller, String ... libPaths) {
        this.resources = new ArrayList<>(libPaths.length);

        for (String libPath : libPaths) {
            resources.add(findLib(caller, libPath));
        }
    }

    /**
     * Load the libraries. This will create a temporary directory to copy the libs to and then
     * {@link System#load(String) load} them from there.
     * @throws IOException If IO operations fail
     */
    public void load() throws IOException {
        Path tmp = Files.createTempDirectory("lutils-native-lib-loader");

        Logger.getLogger("ResourceLibraryLoader").config("Created temporary directory '" + tmp + "' for native libraries.");

        int i = 0; // to avoid collisions
        for (ResourceUtils.StreamURLConnection resource : resources) {
            Path file = tmp.resolve(i++ + "-" + FileUtils.getFileName(resource.getPath()));
            Files.copy(resource.openInputStream(), file);
            System.load(file.toAbsolutePath().toString());
            file.toFile().deleteOnExit();
        }

        tmp.toFile().deleteOnExit();
    }



}
