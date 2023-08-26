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

package de.linusdev.lutils.struct.annos;

import de.linusdev.lutils.struct.abstracts.Structure;
import de.linusdev.lutils.struct.abstracts.StructureStaticVariables;
import de.linusdev.lutils.struct.generator.StaticGenerator;

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
     * {@link StaticGenerator#calculateInfo(Class, FixedLength) calculateInfo} method
     */
    boolean requiresCalculateInfoMethod() default false;

    /**
     *
     * @return {@code true} if this structures {@link StaticGenerator#calculateInfo(Class, FixedLength) calculateInfo}
     * method requires a {@link FixedLength} annotation.
     */
    boolean requiresFixedLengthAnnotation() default false;
}
