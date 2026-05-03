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

package de.linusdev.lutils.pack.map;

import de.linusdev.lutils.id.Identifier;
import de.linusdev.lutils.id.IdentifierAccessors;
import de.linusdev.lutils.id.IdentifierType;
import de.linusdev.lutils.pack.InventoriedPack;
import de.linusdev.lutils.pack.Pack;
import de.linusdev.lutils.pack.PackGroup;
import de.linusdev.lutils.pack.VirtualPack;
import de.linusdev.lutils.pack.loader.ProgressReporter;
import de.linusdev.lutils.pack.loader.ReloadListener;
import de.linusdev.lutils.pack.loader.ResourcesLoader;
import de.linusdev.lutils.pack.resource.Resource;
import de.linusdev.lutils.version.Version;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

class ResourceMapTest {

    private final static IdentifierType TEST_TYPE = () -> "test-type";

    private final static PackGroup<ResourceMap<TestResource>, TestResource> TEST_GROUP = PackGroup.newResourceMapGroup(
            "test",
            (_, _) -> {throw new UnsupportedOperationException();},
            (_, _, _, _) -> {}
    );

    private static final InventoriedPack TEST_PACK = new VirtualPack(
            "test",
            Pack.TYPE.of("test", "resource-map"),
            "",
            Version.of("1.0.0"),
            Map.of(),
            List.of(TEST_GROUP),
            pack -> Map.of(
                    TEST_GROUP, List.of(
                            new TestResource(pack, "apple-dog"),
                            new TestResource(pack, "apple-cat"),
                            new TestResource(pack, "apple-deer"),
                            new TestResource(pack, "strawberry-dog"),
                            new TestResource(pack, "orange-deer"),
                            new TestResource(pack, "fly-dog"),
                            new TestResource(pack, "strawberry-cat"),
                            new TestResource(pack, "pineapple-cat")
                    )
            )
    );

    public static class TestResource implements Resource {

        private final @NotNull InventoriedPack source;
        private final @NotNull Identifier id;

        public TestResource(@NotNull InventoriedPack source, @NotNull String id) {
            this.source = source;
            this.id = TEST_TYPE.of("test", id);
        }

        @Override
        public @NotNull InventoriedPack getSource() {
            return source;
        }

        @Override
        public @NotNull Identifier getIdentifier() {
            return id;
        }

        @Override
        public String toString() {
            return getIdentifierAsString();
        }
    }

    @BeforeAll
    static void addTypes() {
        IdentifierAccessors.registerType(TEST_TYPE);
    }

    @Test
    void test() {

        ResourcesLoader loader = new ResourcesLoader(
                List.of(TEST_PACK),
                List.of(), List.of(),
                ReloadListener.SOUT_RELOAD_LISTENER
        );

        loader.reload(ProgressReporter.SOUT_REPORTER);

        ResourceMap<TestResource> res = loader.getResources().get(TEST_GROUP);

        var like = res.like(TEST_TYPE.of("test", "apple"));

        for (Map.Entry<TestResource, Integer> e : like) {
            System.out.println(e.getKey() + ": " + e.getValue());
        }

    }
}