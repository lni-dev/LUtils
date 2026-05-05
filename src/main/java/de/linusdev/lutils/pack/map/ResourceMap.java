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
import de.linusdev.lutils.other.str.StringUtils;
import de.linusdev.lutils.pack.resource.Resource;
import de.linusdev.lutils.pack.resource.ResourceCollection;
import org.jetbrains.annotations.NotNull;

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
    public @NotNull List<SimilarityResult<R>> like(@NotNull Identifier id) {
        String searchId = id.id();
        Map<R, Integer> simList = new HashMap<>();

        for (Map.Entry<String, R> entry : values.entrySet()) {
            String currentId = Identifier.ofString(entry.getKey()).id();

            // Diff to complete id
            int diff = StringUtils.levenshteinDistance(searchId, currentId, 10);
            addToSimList(simList, entry.getValue(), diff);

            if(diff <= PART_PENALTY)
                continue;

            // Diff to parts of id
            Pattern pattern = Pattern.compile(Identifier.IDENTIFIER_ALLOWED_SEPARATORS_REGEX + "+");
            String[] cIdParts = pattern.split(currentId);
            String[] sIdParts = pattern.split(searchId);

            int maxSimCount = 0;
            boolean foundMaxSim = false;
            for (String sIdPart : sIdParts) {
                foundMaxSim = false;
                for (String cIdPart : cIdParts) {
                    int partDiff = StringUtils.levenshteinDistance(sIdPart, cIdPart, 10) + PART_PENALTY;
                    if(partDiff < diff)
                        addToSimList(simList, entry.getValue(), partDiff);

                    if(!foundMaxSim && partDiff == PART_PENALTY) {
                        maxSimCount++;
                        foundMaxSim = true;
                    }
                }
            }

            if(maxSimCount == sIdParts.length) {
                // very similar remove PART_PENALTY/2
                addToSimList(simList, entry.getValue(), sIdParts.length == cIdParts.length ? 0 : PART_PENALTY/2);
            }
        }

        return simList.entrySet().stream()
                .sorted(Comparator.comparingInt(Map.Entry::getValue))
                .map(entry -> new SimilarityResult<>(entry.getKey(), entry.getValue()))
                .toList();
    }

    private static final int PART_PENALTY = 2;

    private static <R> void addToSimList(@NotNull Map<R, Integer> simList, R value, int newDiff) {
        simList.compute(value, (_, cDiff) -> {
            if(cDiff == null || newDiff < cDiff)
                return newDiff;
            return cDiff;
        });
    }
}
