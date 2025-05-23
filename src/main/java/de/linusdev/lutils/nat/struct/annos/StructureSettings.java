/*
 * Copyright (c) 2023-2025 Linus Andera
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

package de.linusdev.lutils.nat.struct.annos;

import de.linusdev.lutils.nat.abi.ABI;
import de.linusdev.lutils.nat.abi.OverwriteChildABI;
import de.linusdev.lutils.nat.struct.abstracts.Structure;
import de.linusdev.lutils.nat.struct.abstracts.StructureStaticVariables;
import de.linusdev.lutils.nat.struct.generator.StaticGenerator;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.*;

/**
 * Marks this class as a {@link Structure} and contains specific information about it.
 */
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target(ElementType.TYPE)
@Documented
public @interface StructureSettings {

    /**
     *
     * @return {@code true} if this structures {@link StructureStaticVariables#INFO} must be retrieved through the
     * {@link StaticGenerator#calculateInfo(Class, StructValue, StructValue[], ABI, OverwriteChildABI) calculateInfo} method
     */
    boolean requiresCalculateInfoMethod() default false;

    /**
     * Whether this structure supports/requires the {@link StructValue#length()} field.
     */
    @NotNull RequirementType customLengthOption() default RequirementType.NOT_SUPPORTED;

    /**
     * Whether this structure supports/requires the {@link StructValue#elementType()} field.
     */
    @NotNull RequirementType customElementTypesOption() default RequirementType.NOT_SUPPORTED;

    @NotNull RequirementType customLayoutOption() default RequirementType.NOT_SUPPORTED;
}
