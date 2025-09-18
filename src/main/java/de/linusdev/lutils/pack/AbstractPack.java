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

package de.linusdev.lutils.pack;

import de.linusdev.lutils.data.json.Json;
import de.linusdev.lutils.data.json.JsonMapImpl;
import de.linusdev.lutils.data.json.parser.JsonParser;
import de.linusdev.lutils.id.Identifier;
import de.linusdev.lutils.other.parser.ParseException;
import de.linusdev.lutils.pack.errors.PackLoadingException;
import de.linusdev.lutils.pack.packs.AbstractExtractedOnDiskPack;
import de.linusdev.lutils.pack.packs.AbstractInJarPack;
import de.linusdev.lutils.pack.packs.AbstractZipPack;
import de.linusdev.lutils.version.Version;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Basic implementation of a {@link Pack}.
 * @see AbstractZipPack
 * @see AbstractInJarPack
 * @see AbstractExtractedOnDiskPack
 */
public abstract class AbstractPack implements Pack {

    final static @NotNull JsonParser JSON_PARSER = new JsonParser()
            .setJsonBuilderSupplier(() -> new JsonMapImpl(new HashMap<>()))
            .setAllowComments(true, (parser, str) -> {});

    private String name;
    private Identifier id;
    private String description;
    private Version version;
    private Map<PackGroup<?, ?>, String> inventory;
    private boolean loaded = false;

    public void load() throws PackLoadingException {
        try(InputStream infoIn = resolve(infoFileName())) {
            Json packInfo = JSON_PARSER.parseStream(infoIn);

            this.name = packInfo.grab("name").requireNotNull().getAs();
            this.id = PackIdUtils.ofJson(packInfo, PackIdType.TYPE);
            this.description = packInfo.grab("description").requireNotNull().getAs();
            this.version = packInfo.grab("version").requireNotNull().<String, Version>castAndConvert(Version::of).get();
            this.inventory = new HashMap<>();

            Json inventory = packInfo.grab("inventory").requireNotNull().getAs();

            for (PackGroup<?, ?> allowedGroup : allowedInventoryGroups()) {
                String groupLocation = inventory.getAs(allowedGroup.name());

                if (groupLocation == null) {
                    //this group is missing
                    continue;
                }

                this.inventory.put(allowedGroup, groupLocation);
            }
        } catch (ParseException e) {
            throw new PackLoadingException(this, "Could not parse '" + infoFileName() + "'.", e);
        } catch (IOException e) {
            throw new PackLoadingException(this, "Could not read '" + infoFileName() + "'.", e);
        } catch (Throwable t) {
            throw new PackLoadingException(this, t);
        }

        loaded = true;
    }

    @Override
    public boolean isPack() {
        return exists(infoFileName());
    }

    /**
     * Checks if the pack is loaded. Throws an exception if it is not.
     * @throws IllegalStateException if pack is not {@link #isLoaded() loaded}.
     */
    protected void ensureLoaded() {
        if(!isLoaded())
            throw new IllegalStateException("Pack is not loaded! Call load() first.");
    }

    public boolean isLoaded() {
        return loaded;
    }

    public @NotNull String name() {
        ensureLoaded();
        return name;
    }

    @Override
    public @NotNull Identifier id() {
        return id;
    }

    public @NotNull String description() {
        ensureLoaded();
        return description;
    }

    public @NotNull Version version() {
        ensureLoaded();
        return version;
    }

    /**
     * Inventory of this pack. Maps all {@link PackGroup}s that are in
     * {@link #allowedInventoryGroups()}
     * and contained in this pack to the group-json location.
     * To access the contained {@link PackGroup}s see {@link Resources}.
     */
    public @NotNull Map<PackGroup<?, ?>, String> inventory() {
        ensureLoaded();
        return inventory;
    }

    /**
     * The name of the info file in this pack type.
     */
    protected abstract @NotNull String infoFileName();

    /**
     * {@link PackGroup Inventory groups} that are allowed to be in this group.
     * If other groups are contained in the pack, it can still be loaded. All groups which
     * are not contained in this list will be ignored.
     * @return The list of allowed inventory groups.
     */
    protected abstract @NotNull List<PackGroup<?, ?>> allowedInventoryGroups();

    @Override
    public String toString() {
        if(isLoaded())
            return getClass().getSimpleName() + " '" + name() + "' v. " + version().getAsUserFriendlyString() + "; id=" + id.getAsString() + ", inventory=" + inventory();
        else
            return getClass().getSimpleName() + " (not yet loaded); location=" + location();
    }

    /**
     * @see Pack#equals(Pack, Object)
     */
    @SuppressWarnings("EqualsDoesntCheckParameterClass")
    @Override
    public boolean equals(Object obj) {
        return Pack.equals(this, obj);
    }

    /**
     * @see Pack#hashcode(Pack)
     */
    @Override
    public int hashCode() {
        return Pack.hashcode(this);
    }
}
