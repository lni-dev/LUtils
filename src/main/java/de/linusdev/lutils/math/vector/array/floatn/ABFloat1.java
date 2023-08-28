/*
 * Copyright (c) 2023 Linus Andera
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

package de.linusdev.lutils.math.vector.array.floatn;

import de.linusdev.lutils.math.vector.abstracts.floatn.Float1;

@SuppressWarnings("unused")
public class ABFloat1 extends ABFloatN implements Float1 {

    public ABFloat1(float x) {
        super(new float[]{x}, 0);
    }

    public ABFloat1(float[] array, int arrayStartIndex) {
        super(array, arrayStartIndex);
    }

    public ABFloat1() {
        super(MEMBER_COUNT);
    }
}
