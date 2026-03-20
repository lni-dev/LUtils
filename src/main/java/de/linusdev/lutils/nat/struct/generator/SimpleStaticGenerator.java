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

package de.linusdev.lutils.nat.struct.generator;

import de.linusdev.lutils.nat.abi.ABI;
import de.linusdev.lutils.nat.abi.ABIs;
import de.linusdev.lutils.nat.struct.annos.RequirementType;
import de.linusdev.lutils.nat.struct.info.StructureInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public abstract class SimpleStaticGenerator implements StaticGenerator {

    protected final @NotNull RequirementType customLengthOption;
    protected final @NotNull RequirementType elementTypeInfo;
    protected final @NotNull ABI @Nullable [] supportedABIs;
    protected final @NotNull ABI defaultABI;

    protected SimpleStaticGenerator(
            @NotNull RequirementType customLengthOption,
            @NotNull RequirementType elementTypeInfo
    ) {
        this(customLengthOption, elementTypeInfo, null, ABIs.defaultABI());
    }

    protected SimpleStaticGenerator(
            @NotNull RequirementType customLengthOption,
            @NotNull RequirementType elementTypeInfo,
            @NotNull ABI @Nullable [] supportedABIs,
            @NotNull ABI defaultABI
    ) {
        this.customLengthOption = customLengthOption;
        this.elementTypeInfo = elementTypeInfo;
        this.supportedABIs = supportedABIs;
        this.defaultABI = defaultABI;
    }


    public boolean check(
            @NotNull Class<?> selfClazz,
            @Nullable ABI abi,
            int @Nullable [] length,
            @NotNull Class<?> @Nullable [] elementTypes
    ) {
        if(customLengthOption == RequirementType.NOT_SUPPORTED && length != null) {
            throw new StructureLayoutGenerationException(selfClazz, "Custom length option is not supported.");
        }

        if(customLengthOption == RequirementType.REQUIRED && length == null) {
            throw new StructureLayoutGenerationException(selfClazz, "Custom length option is required.");
        }

        if(elementTypeInfo == RequirementType.NOT_SUPPORTED && elementTypes != null) {
            throw new StructureLayoutGenerationException(selfClazz, "Element type information is not supported.");
        }

        if(elementTypeInfo == RequirementType.REQUIRED && elementTypes == null) {
            throw new StructureLayoutGenerationException(selfClazz, "Element type information is required.");
        }

        if(abi != null && supportedABIs != null) {
            boolean supported = false;
            for (ABI supportedABI : supportedABIs) {
                if(abi.equals(supportedABI)) {
                    supported = true;
                    break;
                }
            }

            if(!supported) {
                throw new StructureLayoutGenerationException(selfClazz, "The ABI '" + abi.identifier() + "' is not supported. Supported ABIs are: "
                        + Arrays.toString(Arrays.stream(supportedABIs).map(ABI::identifier).toArray()) + ".");
            }
        }

        return false;
    }

    @Override
    public @NotNull StructureInfo calculateInfo(
            @NotNull Class<?> selfClazz,
            @Nullable ABI abi,
            int @Nullable [] length,
            @NotNull Class<?> @Nullable [] elementTypes
    ) {
        check(selfClazz, abi, length, elementTypes);

        if(abi == null)
            abi = defaultABI;

        return calculateInfoChecked(selfClazz, abi, length, elementTypes);
    }

    public abstract @NotNull StructureInfo calculateInfoChecked(
            @NotNull Class<?> selfClazz,
            @NotNull ABI abi,
            int [] length,
            @NotNull Class<?> [] elementTypes
    );
}
