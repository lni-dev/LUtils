/*
 * Copyright (c) 2024 Linus Andera
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

package de.linusdev.lutils.nat.struct.info;

import org.jetbrains.annotations.NotNull;

/**
 * {@link StructureInfo} for unions. {@link #sizes} does not contain the sizes of the unions element.
 * instead it will always be of length three and only define the paddings and length of the union itself.
 */
public class UnionInfo extends StructureInfo {

    /**
     * @see #getPositions()
     */
    protected final int @NotNull [] positions;

    public UnionInfo(
            int alignment,
            boolean compressed,
            int prePadding, int size, int postPadding,
            int @NotNull [] positions
    ) {
        super(alignment, compressed, prePadding, size, postPadding);
        this.positions = positions;
    }

    /**
     * Positions at which the different elements start
     * @return array of positions
     */
    public int @NotNull [] getPositions() {
        return positions;
    }
}
