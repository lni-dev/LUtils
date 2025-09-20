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

/**
 * <p>
 *     This package provides an interface to load packs containing resources from the disk or from the
 *     executing jar file.
 * </p>
 * <p>
 *     Packs can be loaded using a {@link de.linusdev.lutils.pack.loader.ResourcesLoader ResourcesLoader} instance.
 *     Before loading any packs {@link de.linusdev.lutils.pack.PackGroup groups} must be created an allowed in
 *     {@link de.linusdev.lutils.pack.AbstractPack#allowedInventoryGroups() allowedInventoryGroups}. Each group will
 *     create its own {@link de.linusdev.lutils.pack.resource.ResourceCollection resource collection} which will contain all
 *     resources of this group loaded from all packs by a specific
 *     {@link de.linusdev.lutils.pack.loader.ResourcesLoader ResourcesLoader} instance.
 *     The {@link de.linusdev.lutils.pack.resource.ResourceCollection resource collections} can be accessed from a
 *     {@link de.linusdev.lutils.pack.Resources Resources} instance. Each resource loader has a single resources
 *     instance which can be accessed using {@link de.linusdev.lutils.pack.loader.ResourcesLoader#getResources() getResources()}.
 * </p>
 *
 *
 * <p>
 *     A pack must contain a pack-info json file. The name of this file is defined by the specific pack implementation
 *     (See {@link de.linusdev.lutils.pack.AbstractPack#infoFileName() infoFileName()}). The content of the file must be similar
 *     to:
 * </p>
 * <pre>{@code {
 *   "$schema": "https://raw.githubusercontent.com/lni-dev/LUtils/refs/heads/master/src/main/schema/pack.schema.json",
 *   "name": "Some Pack Name",
 *   "type": "pack",
 *   "namespace": "de.linusdev",
 *   "id": "some-pack-id",
 *   "description": "A nice description for your pack.",
 *   "version": "1.0.0",
 *   "inventory": {
 *     "example-group-name": "resources-of-example-group.json",
 *   }
 * }}</pre>
 * <ul>
 *     <li>{@code $schema} is optional and defines the json shema and is useful for IDE completion</li>
 *     <li>{@code name} is the {@link de.linusdev.lutils.pack.Pack#name() pack name}</li>
 *     <li>
 *         {@code type}, {@code namespace} and {@code id} will be combined to an
 *         {@link de.linusdev.lutils.id.Identifier Identifier} and is the
 *         {@link de.linusdev.lutils.pack.Pack#id() pack id}
 *     </li>
 *     <li>{@code description} is the {@link de.linusdev.lutils.pack.Pack#description() pack description}</li>
 *     <li>{@code version} is the {@link de.linusdev.lutils.pack.Pack#version() pack version}</li>
 *     <li>
 *         {@code inventory} defines which {@link de.linusdev.lutils.pack.PackGroup groups} the pack contains.
 *         It must be a map of {@link de.linusdev.lutils.pack.PackGroup#name() group names} to {@code group-json-file}
 *         locations.
 *     </li>
 * </ul>
 *
 * A {@code group-json-file} must be a json file containing an array of json-objects. It can either be a json-array:
 * <pre>{@code
 * [
 *   { ... resource json ... },
 *   { ... resource json ... }
 * ]
 * }</pre>
 * or a json-object with the key {@code array}:
 * <pre>{@code
 * {
 *   "common": {
 *     "some key": "some value"
 *   },
 *   "array": [
 *     { ... resource json ... },
 *     { ... resource json ... }
 *   ]
 * }
 * }</pre>
 * <p>
 *     As seen above the second option allows a {@code common} object. All key-value paris contained in the {@code common}
 *     object will be added to every resource json contained in {@code array}.
 * </p>
 * <p>
 *     Each resource json will be passed to
 *     {@link de.linusdev.lutils.pack.PackGroup#addToResourceCollection(de.linusdev.lutils.pack.resource.ResourceCollection, de.linusdev.lutils.data.json.Json, de.linusdev.lutils.pack.AbstractPack) addToResourceCollection}
 *     of the corresponding {@link de.linusdev.lutils.pack.PackGroup PackGroup}.
 * </p>
 *
 *
 */
package de.linusdev.lutils.pack;