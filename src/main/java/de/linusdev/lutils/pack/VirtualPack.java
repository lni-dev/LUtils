/*
 * Copyright (c) 2026 Linus Andera
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

package de.linusdev.lutils.pack;

import de.linusdev.lutils.id.Identifier;
import de.linusdev.lutils.other.log.Logger;
import de.linusdev.lutils.pack.errors.PackLoadingException;
import de.linusdev.lutils.pack.resource.Resource;
import de.linusdev.lutils.pack.resource.ResourceCollection;
import de.linusdev.lutils.version.Version;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class VirtualPack implements InventoriedPack {

    private final @NotNull String name;
    private final @NotNull Identifier id;
    private final @NotNull String description;
    private final @NotNull Version version;

    private final @NotNull Map<String, VirtualFile> files;
    private final @NotNull List<PackGroup<?, ?>> allowedInventoryGroups;
    private final @NotNull Map<PackGroup<?, ?>, Collection<Resource>> resources;

    public VirtualPack(
            @NotNull String name,
            @NotNull Identifier id,
            @NotNull String description,
            @NotNull Version version,
            @NotNull Map<String, VirtualFile> files,
            @NotNull List<PackGroup<?, ?>> allowedInventoryGroups,
            @NotNull Function<@NotNull VirtualPack, @NotNull Map<PackGroup<?, ?>, Collection<Resource>>> resources
    ) {
        this.name = name;
        this.id = id;
        this.description = description;
        this.version = version;
        this.files = files;
        this.allowedInventoryGroups = Collections.unmodifiableList(allowedInventoryGroups);
        this.resources = resources.apply(this);
    }

    @Override
    public @NotNull Collection<PackGroup<?, ?>> inventory() {
        return Collections.unmodifiableSet(resources.keySet());
    }

    @Override
    public @NotNull List<PackGroup<?, ?>> allowedInventoryGroups() {
        return allowedInventoryGroups;
    }

    @Override
    public void loadGroup(@NotNull Logger log, @NotNull PackGroup<?, ?> group, @NotNull ResourceCollection<?> col) {
        for (Resource resource : resources.get(group)) {
            group._addToResourceCollection(col, resource, this);
        }
    }

    @Override
    public void load() throws PackLoadingException {
        // Virtual Packs do not require loading
    }

    @Override
    public boolean isPack() {
        return true;
    }

    @Override
    public boolean isLoaded() {
        return true; // Virtual Packs do not require loading
    }

    @Override
    public @NotNull String name() {
        return name;
    }

    @Override
    public @NotNull Identifier id() {
        return id;
    }

    @Override
    public @NotNull String description() {
        return description;
    }

    @Override
    public @NotNull Version version() {
        return version;
    }

    @Override
    public @NotNull String location() {
        return "virtual-pack_" + id.getIdentifierAsString();
    }

    @Override
    public @NotNull InputStream resolve(@NotNull String file) throws IOException {
        VirtualFile f = files.get(file);

        if(f == null)
            throw new FileNotFoundException();

        return files.get(file).newInputStream();
    }

    @Override
    public boolean exists(@NotNull String file) {
        return files.containsKey(file);
    }

    public interface VirtualFile {
        @NotNull InputStream newInputStream();
    }
}
