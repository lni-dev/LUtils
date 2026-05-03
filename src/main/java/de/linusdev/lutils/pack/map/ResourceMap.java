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

package de.linusdev.lutils.pack.map;

import de.linusdev.lutils.id.Identifier;
import de.linusdev.lutils.pack.resource.Resource;
import de.linusdev.lutils.pack.resource.ResourceCollection;
import org.jetbrains.annotations.NotNull;

import java.text.CollationKey;
import java.text.Collator;
import java.util.*;
import java.util.function.Supplier;
import java.util.regex.Pattern;

/**
 * Basic {@link ResourceCollection} implementation.
 * @param <R> resource type
 */
public class ResourceMap<R extends Resource> implements ResourceCollection<R> {

    private final @NotNull HashMap<String, R> values = new HashMap<>();

    public void put(R object) {
        values.put(Identifier.toString(object.getIdentifier()), object);
    }

    public R computeIfAbsent(Identifier id, Supplier<R> computer) {
        return values.computeIfAbsent(Identifier.toString(id), key -> computer.get());
    }

    public R get(@NotNull String id) {
        return values.get(id);
    }

    public R get(@NotNull Identifier id) {
        return values.get(Identifier.toString(id));
    }

    public @NotNull Collection<R> collection() {
        return values.values();
    }

    public int size() {
        return values.size();
    }

    @Override
    public void clear() {
        values.clear();
    }

    @Override
    public @NotNull Iterator<R> iterator() {
        return values.values().iterator();
    }

    @Override
    public @NotNull List<Map.Entry<R, Integer>> like(@NotNull Identifier id) {
        String searchId = id.id();

        LinkedHashMap<R, Integer> simList = new LinkedHashMap<>();
        Collator collator = Collator.getInstance();

        final int PART_MIN_DIFF = 5;

        for (Map.Entry<String, R> entry : values.entrySet()) {
            String currentId = Identifier.ofString(entry.getKey()).id();

            // Diff to complete id
            int diff = Math.abs(collator.compare(searchId, currentId));

            simList.compute(entry.getValue(), (_, cDiff) -> {
                if(cDiff == null || diff < cDiff)
                    return diff;
                return cDiff;
            });

            // Diff to parts of id
            Pattern pattern = Pattern.compile(Identifier.IDENTIFIER_ALLOWED_SEPARATORS_REGEX + "+");
            String[] cIdParts = pattern.split(currentId);
            String[] sIdParts = pattern.split(searchId);

            for (String sIdPart : sIdParts) {
                CollationKey key = collator.getCollationKey(sIdPart);
                for (String cIdPart : cIdParts) {
                    int partDiff = key.compareTo(collator.getCollationKey(cIdPart)) + PART_MIN_DIFF;
                    if(partDiff < diff)
                        simList.compute(entry.getValue(), (_, cDiff) -> {
                            if(cDiff == null || partDiff < cDiff)
                                return partDiff;
                            return cDiff;
                        });
                }
            }
        }


        return simList.entrySet().stream().sorted(Comparator.comparingInt(Map.Entry::getValue)).toList();
    }
}
