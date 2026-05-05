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

package de.linusdev.lutils.other.str;

import de.linusdev.lutils.math.LMath;
import org.jetbrains.annotations.NotNull;

public class LevenshteinDistance {


    static int recLD(@NotNull String str1, @NotNull String str2, int minSimilarity) {
        return recLD(new Cache(str1, str2), 0, 0, minSimilarity);
    }

    static int recLD(@NotNull Cache cache, int str1Index, int str2Index, int minSimilarity) {
        int penalty = cache.str1.charAt(str1Index) == cache.str2.charAt(str2Index) ? 0 : 1;

        if(minSimilarity == 0 || minSimilarity == penalty)
            return minSimilarity;

        int deletion =      minSimilarity > 0 ? cache.get(str1Index+1,    str2Index,    minSimilarity - 1       ) + 1 : 1;
        int insertion =     minSimilarity > 0 ? cache.get(str1Index,      str2Index+1,  minSimilarity - 1       ) + 1 : 1;
        int substitution =  cache.get(str1Index+1,    str2Index+1,  minSimilarity - penalty ) + penalty;

        return LMath.min(deletion, insertion, substitution);
    }

    static class Cache {

        private final int[] values;
        private final int str1Length;
        private final int str2Length;

        public final String str1;
        public final String str2;

        public Cache(@NotNull String str1, @NotNull String str2) {
            this.str1 = str1;
            this.str2 = str2;
            str1Length = str1.length();
            str2Length = str2.length();
            values = new int[str1Length*str2Length];
        }

        public int get(int str1Index, int str2Index, int minSimilarity) {

            if(str1Length == str1Index)
                return str2Length - str2Index;

            if(str2Length == str2Index)
                return str1Length - str1Index;

            int cached = values[str1Index * str2Length + str2Index];
            if(cached!= 0)
                return cached-1;

            cached = recLD(this, str1Index, str2Index, minSimilarity);
            values[str1Index * str2Length + str2Index] = cached + 1;
            return cached;
        }
    }

}
