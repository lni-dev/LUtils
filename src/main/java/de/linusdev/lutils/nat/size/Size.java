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

package de.linusdev.lutils.nat.size;

import org.jetbrains.annotations.NotNull;

public class Size {

    private final long size;

    public Size(long size, @NotNull ByteUnits unit) {
        this.size = unit.getValue() * size;
    }

    public Size(long size) {
        this.size = size;
    }

    public long get() {
        return size;
    }

    public int getAsInt() {
        assert size < Integer.MAX_VALUE;
        return (int) size;
    }

    @Override
    public String toString() {

        if(ByteUnits.KiB.getValue() > size)
            return size + " bytes";
        if(ByteUnits.MiB.getValue() > size)
            return (size / ByteUnits.KiB.getValue()) + "" + ByteUnits.KiB + " bytes";
        if(ByteUnits.GiB.getValue() > size)
            return (size / ByteUnits.MiB.getValue()) + "" + ByteUnits.MiB + " bytes";
        if(ByteUnits.TiB.getValue() > size)
            return (size / ByteUnits.GiB.getValue()) + "" + ByteUnits.GiB + " bytes";
        if(ByteUnits.PiB.getValue() > size)
            return (size / ByteUnits.TiB.getValue()) + "" + ByteUnits.TiB + " bytes";

        return (size / ByteUnits.PiB.getValue()) + "" + ByteUnits.PiB + " bytes";
    }
}
