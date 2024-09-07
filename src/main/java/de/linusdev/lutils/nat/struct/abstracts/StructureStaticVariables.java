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

package de.linusdev.lutils.nat.struct.abstracts;

import de.linusdev.lutils.nat.struct.annos.StructValue;
import de.linusdev.lutils.nat.struct.annos.StructureSettings;
import de.linusdev.lutils.nat.struct.generator.StaticGenerator;
import de.linusdev.lutils.nat.struct.generator.StructCodeGenerator;
import de.linusdev.lutils.nat.struct.info.StructureInfo;
import de.linusdev.lutils.nat.struct.utils.SSMUtils;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

/**
 * This class shows and describes the public static fields and methods a {@link Structure} requires.
 * Read more about the fields:
 * <ul>
 *     <li>{@link #INFO}</li>
 *     <li>{@link #GENERATOR}</li>
 * </ul>
 * Read more about the methods:
 * <lu>
 *     <li>{@link #newUnallocated()}</li>
 *     <li>{@link #newAllocatable(StructValue)}</li>
 *     <li>{@link #newAllocated(StructValue)}</li>
 * </lu>
 * These methods may also have a slightly different name, because they may contain generics.
 * <br><br>
 * {@link SSMUtils SSMUtils} can help in getting the above fields and functions of
 * a specific structure.
 */
@SuppressWarnings({"DataFlowIssue", "UnnecessaryModifier", "JavadocReference", "unused"})
public interface StructureStaticVariables {

    /**
     * This {@code public static final} variable is required if
     * {@link StructureSettings#requiresCalculateInfoMethod()} is set
     * to {@code false}. The variable must not be {@code null} (unlike this example).
     */
    @SuppressWarnings("unused")
    public static final @NotNull StructureInfo INFO = null;

    /**
     * This {@code public static final} variable is required if
     * {@link StructureSettings#requiresCalculateInfoMethod()} is set
     * to {@code true}. The variable must not be {@code null} (unlike this example). It can also be
     * present, if an {@link StructCodeGenerator} shall be provided (see {@link StaticGenerator#codeGenerator() here}).
     * <br><br>
     * See {@link StaticGenerator} for more information.
     */
    @SuppressWarnings("unused")
    public static final @NotNull StaticGenerator GENERATOR = null;


    /**
     * This method can be optionally implemented.
     * <br><br>
     * Creates a new instance of the structure, <b>without</b> generating its {@link Structure#info info}.
     * This is useful, if this structure will be used as an element in another parent structure.
     * This parent structure, will have to call {@link Structure#useBuffer(Structure, int, StructureInfo) useBuffer()} on its elements
     * @return a freshly created unallocated structure.
     */
    public static @NotNull Structure newUnallocated() {
        throw new UnsupportedOperationException("example only");
    }

    /**
     * This method can be optionally implemented.
     * <br><br>
     * Creates a new instance of the structure, <b>with</b>
     * generating its {@link Structure#info info}. This means {@link Structure#allocate() allocate()} or {@link Structure#claimBuffer(ByteBuffer) claimBuffer()} must be called
     * afterward to use the structure. This is useful, if the buffer for the structure already exist and must be claimed
     * using {@link Structure#claimBuffer(ByteBuffer) claimBuffer()}.
     * @return a freshly created allocatable structure.
     */
    public static @NotNull Structure newAllocatable(StructValue structValue) {
        throw new UnsupportedOperationException("example only");
    }

    /**
     * This method can be optionally implemented.
     * <br><br>
     * Creates a new instance of the structure, <b>with</b>
     * generating its {@link Structure#info info} and calling {@link Structure#allocate() allocate()}. This is useful, if a ready-to-use structure is
     * required.
     * @return a freshly created allocated structure.
     */
    public static @NotNull Structure newAllocated(StructValue structValue) {
        throw new UnsupportedOperationException("example only");
    }

}
