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


    static int recLD(@NotNull CharSequence a, @NotNull CharSequence b) {

        // TODO: optimize!

        if(a.isEmpty())
            return b.length();

        if(b.isEmpty())
            return a.length();

        int penalty = a.charAt(0) == b.charAt(0) ? 0 : 1;

        int deletion = recLD(new CharSequenceView(a, 1), b) + 1;
        int insertion = recLD(new CharSequenceView(b, 1), a) + 1;
        int substitution = recLD(new CharSequenceView(a, 1), new CharSequenceView(b, 1)) + penalty;

        return LMath.min(deletion, insertion, substitution);
    }

}
