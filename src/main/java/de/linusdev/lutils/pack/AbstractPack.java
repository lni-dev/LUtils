/*
 * Copyright (c) 2025-2026 Linus Andera
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

import de.linusdev.lutils.collections.Entry;
import de.linusdev.lutils.data.json.Json;
import de.linusdev.lutils.data.json.JsonMapImpl;
import de.linusdev.lutils.id.Identifier;
import de.linusdev.lutils.optional.Container;
import de.linusdev.lutils.other.log.Logger;
import de.linusdev.lutils.other.parser.ParseException;
import de.linusdev.lutils.pack.errors.PackContentException;
import de.linusdev.lutils.pack.errors.PackException;
import de.linusdev.lutils.pack.errors.PackLoadingException;
import de.linusdev.lutils.pack.packs.AbstractExtractedOnDiskPack;
import de.linusdev.lutils.pack.packs.AbstractInJarPack;
import de.linusdev.lutils.pack.packs.AbstractZipPack;
import de.linusdev.lutils.pack.resource.ResourceCollection;
import de.linusdev.lutils.version.Version;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static de.linusdev.lutils.pack.Resources.JSON_PARSER;

/**
 * Basic implementation of a {@link Pack}.
 * @see AbstractZipPack
 * @see AbstractInJarPack
 * @see AbstractExtractedOnDiskPack
 */
public abstract class AbstractPack implements InventoriedPack {

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

    @Override
    public boolean isLoaded() {
        return loaded;
    }

    @Override
    public @NotNull String name() {
        ensureLoaded();
        return name;
    }

    @Override
    public @NotNull Identifier id() {
        return id;
    }

    @Override
    public @NotNull String description() {
        ensureLoaded();
        return description;
    }

    @Override
    public @NotNull Version version() {
        ensureLoaded();
        return version;
    }

    @Override
    public @NotNull Collection<PackGroup<?, ?>> inventory() {
        ensureLoaded();
        return Collections.unmodifiableSet(inventory.keySet());
    }

    @Override
    public void loadGroup(@NotNull Logger log, @NotNull PackGroup<?, ?> group, @NotNull ResourceCollection<?> col) throws PackContentException {
        loadGroup(log, group, inventory.get(group), col);
    }

    protected void loadGroup(
            @NotNull Logger log,
            @NotNull PackGroup<?, ?> group,
            @NotNull String groupLocation,
            @NotNull ResourceCollection<?> col
    ) throws PackContentException {
        try(var inGroup = resolve(groupLocation)) {
            Json groupData = JSON_PARSER.parseStream(inGroup);

            @Nullable Json defaultItemData = groupData.grab("common").getAs();

            Container<Object> continuationCon = groupData.grab("continuations");
            Container<Object> arrayCon = groupData.grab("array");

            if(continuationCon.isNull() && arrayCon.isNull()) {
                throw new IllegalArgumentException("File '" + groupLocation + "' in pack '" + name() + "' is missing both 'array' and 'continuations', but at least one must be present.");
            }

            boolean addedSomething = false;

            for (Object continuation : continuationCon.orDefaultIfNull(List.of()).asList().get()) {
                addedSomething = true;
                loadGroup(log, group, (String) continuation, col);
            }

            for (Object item : arrayCon.orDefaultIfNull(List.of()).asList().get()) {
                addedSomething = true;
                if(item instanceof String itemLocation) {
                    // Another location
                    Json jItem = null;
                    try(var inItem = this.resolve(itemLocation)) {
                        jItem = JSON_PARSER.parseStream(inItem);

                        if(defaultItemData != null) {
                            // We can safely cast to JsonMapImpl since we defined the used implementation in JSON_PARSER.
                            for (Entry<String, Object> entry : defaultItemData)
                                ((JsonMapImpl) jItem).add(entry.getKey(), entry.getValue());
                        }

                        group._addToResourceCollection(col, jItem, this);
                    } catch (Throwable t) {
                        if(jItem == null)
                            throw new PackContentException(this, itemLocation, t);
                        throw new PackContentException(this, itemLocation, jItem, t);
                    }
                } else {
                    // It is the item
                    Json jItem = (Json) item;
                    try {
                        if(defaultItemData != null) {
                            // We can safely cast to JsonMapImpl since we defined the used implementation in JSON_PARSER.
                            for (Entry<String, Object> entry : defaultItemData)
                                ((JsonMapImpl) jItem).add(entry.getKey(), entry.getValue());
                        }

                        group._addToResourceCollection(col, jItem, this);
                    } catch (Throwable t) {
                        throw new PackContentException(this, groupLocation, jItem, t);
                    }

                }
            }

            if(!addedSomething) {
                log.warning("File '" + groupLocation + "' in pack '" + name() + "' is not adding anything!");
            }
        } catch (PackException e) {
            throw e;
        } catch (Throwable t) {
            throw new PackContentException(this, groupLocation, t);
        }
    }

    /**
     * The name of the info file in this pack type.
     */
    protected abstract @NotNull String infoFileName();

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
