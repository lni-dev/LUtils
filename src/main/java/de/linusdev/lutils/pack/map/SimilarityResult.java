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

import de.linusdev.lutils.pack.resource.Resource;
import de.linusdev.lutils.result.Result;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 *
 * @param resource the similar resource
 * @param similarity the similarity of the resource. Smaller values mean more similarity.
 * @param <R>
 */
public record SimilarityResult<R extends Resource>(
        @NotNull R resource,
        @Range(from = 0, to = Integer.MAX_VALUE) int similarity
) implements Result {
    @Override
    public int count() {
        return 2;
    }

    @Override
    public Object get(int index) {
        return index == 0 ? resource : similarity;
    }
}
