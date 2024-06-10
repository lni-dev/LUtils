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

package de.linusdev.lutils.nat.struct.annos;

import de.linusdev.lutils.nat.struct.abstracts.ComplexStructure;
import de.linusdev.lutils.nat.struct.abstracts.Structure;
import de.linusdev.lutils.nat.struct.info.StructureInfo;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.*;

/**
 * Holds information about a structure, which may be required to create its {@link StructureInfo}.
 * <br><br>
 * Instances of this interface can also be created at runtime using {@link SVWrapper}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface StructValue {

    /**
     * index of this value in the struct or -1 if it does not matter.
     * <br><br>
     * Currently only supported in children fields of {@link ComplexStructure}.
     * @return index or -1
     */
    int value() default -1;

    /**
     * If the annotated variable's type is a structure, which may vary in length,
     * this can be used to define that length.
     * <br><br>
     * The structure must {@link StructureSettings#customLengthOption() support or require} this field for
     * it to be set to any value different from the default value.
     * @return int array containing the fixed length(s)
     */
    int @NotNull [] length() default -1;

    /**
     * If the annotated variable's type is a structure, which may have varying element types,
     * this can be used to define these element types.
     * <br><br>
     * The structure must {@link StructureSettings#customElementTypesOption() support or require} this field for
     * it to be set to any value different from the default value.
     * @return {@link Class} array containing the element types
     */
    @NotNull Class<?> @NotNull [] elementType() default Structure.class;

    /**
     * If the annotated variable's type is a structure, which layout should be different,
     * than the default layout of the structure. The given class must be annotated with {@link StructureLayoutSettings}
     * defining the new structure layout.
     * <br><br>
     * The structure must {@link StructureSettings#customLayoutOption() support or require} this field for
     * it to be set to any value different from the default value.
     * @return {@link Class} which is annotated with {@link StructureLayoutSettings}.
     */
    @NotNull Class<?> overwriteStructureLayout() default Structure.class;

}
