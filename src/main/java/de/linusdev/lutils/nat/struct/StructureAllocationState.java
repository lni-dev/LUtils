/*
 * Copyright (c) 2025 Linus Andera
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

package de.linusdev.lutils.nat.struct;

import de.linusdev.lutils.nat.abi.ABI;
import de.linusdev.lutils.nat.struct.abstracts.ComplexStructure;
import de.linusdev.lutils.nat.struct.abstracts.Structure;
import de.linusdev.lutils.nat.struct.array.StructureArray;
import de.linusdev.lutils.nat.struct.info.StructureInfo;

import java.nio.ByteBuffer;

/**
 * The possible allocation state of a {@link Structure}.
 * <ul>
 *     <li>{@link #ALLOCATED}</li>
 *     <li>{@link #ALLOCATABLE}</li>
 *     <li>{@link #UNALLOCATED}</li>
 * </ul>
 */
public enum StructureAllocationState {
    /**
     * This structure has an {@link Structure#getInfo() info} and {@link Structure#isInitialised() is already backed by byte buffer}.
     * This means the structure is ready to be used and parsed to native code.
     */
    ALLOCATED,
    /**
     * This structure has an {@link Structure#getInfo() info} but {@link Structure#isInitialised() is <b>not</b> backed by byte buffer}.
     * This means the structure is ready to be {@link Structure#allocate() allocated} or
     * {@link Structure#claimBuffer(ByteBuffer) an already existing allocation can be assigned}.
     */
    ALLOCATABLE,
    /**
     * This structure is uninitialised. This means it does not have an {@link Structure#getInfo() info} yet.
     * It may be possible that the structure can create an info using {@link Structure#getOrGenerateInfo()}.
     * <br><br>
     * This also means. that it is possible, that the {@link ABI} of this structure is not yet set.
     * <br><br>
     * This state is particularly useful if the parent structure will supply its children with a
     * fitting {@link StructureInfo}. For example, this state is required by child-elements of {@link ComplexStructure}
     * and {@link StructureArray}.
     */
    UNALLOCATED,
}
