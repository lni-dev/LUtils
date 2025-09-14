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
import de.linusdev.lutils.pack.errors.PackLoadingException;
import de.linusdev.lutils.pack.packs.simple.SimpleZipPack;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * This class can open a pack which is zipped. See {@link SimpleZipPack}
 * for a usable implementation.
 */
public abstract class AbstractZipPack extends AbstractPack implements AutoCloseable {

    private final @NotNull Path file;
    private ZipFile zip;

    protected AbstractZipPack(@NotNull Path file) {
        this.file = file;
    }

    protected synchronized void initZipFile() throws PackLoadingException {
        if(zip == null) {
            try {
                zip = new ZipFile(file.toFile());
            } catch (IOException e) { // Will catch ZipException too
                throw new PackLoadingException(this, "Could not open zip file.", e);
            }
        }
    }

    @Override
    public void load() throws PackLoadingException {
        initZipFile();
        super.load();
    }

    @Override
    public boolean exists(@NotNull String file) {
        return zip.getEntry(file) != null;
    }

    @Override
    public @NotNull InputStream resolve(@NotNull String file) throws IOException {
        ZipEntry entry =  zip.getEntry(file);

        if(entry == null)
            throw new FileNotFoundException(file);

        return zip.getInputStream(entry);
    }

    @Override
    public void close() throws IOException {
        zip.close();
    }

    @Override
    public @NotNull String location() {
        return file.toAbsolutePath().toString();
    }

}
