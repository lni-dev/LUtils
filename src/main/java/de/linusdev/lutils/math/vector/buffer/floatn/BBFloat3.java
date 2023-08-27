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

package de.linusdev.lutils.math.vector.buffer.floatn;


import de.linusdev.lutils.math.vector.abstracts.floatn.Float3;
import de.linusdev.lutils.math.vector.buffer.BBVectorInfo;
import de.linusdev.lutils.struct.info.StructureInfo;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class BBFloat3 extends BBFloatN implements Float3 {

    public static BBVectorInfo INFO = BBVectorInfo.create(ELEMENT_TYPE_NAME, MEMBER_COUNT, ELEMENT_SIZE);

    public BBFloat3(boolean allocateBuffer) {
        super(MEMBER_COUNT, allocateBuffer);
    }

    @Override
    public @NotNull StructureInfo getInfo() {
        return INFO;
    }
}
