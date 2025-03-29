/*
 * Copyright (c) 2024-2025 Linus Andera
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
import de.linusdev.lutils.nat.abi.DefaultABIs;
import de.linusdev.lutils.nat.abi.OverwriteChildABI;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target(ElementType.TYPE)
@Documented
public @interface StructureLayoutSettings {

    /**
     * Use this to set the {@link ABI} to use when generating the layout of the structure.
     */
    @NotNull DefaultABIs value() default DefaultABIs.DEFAULT;

    /**
     * Class containing the {@link #selectorMethodName()} method. If this is set to anything except {@link Void},
     * {@link #value()} is ignored.
     */
    @NotNull Class<?> selectorMethodClass() default Void.class;

    /**
     * Specify, that the ABI to use when generating the layout of the structure, will be selected at runtime.
     * A method with name returned from this method in class {@link #selectorMethodClass()} will be used to select an {@link ABI}.
     * The method described by this
     * Overrides {@link #value()}.
     */
    @NotNull String selectorMethodName() default "";

    /**
     * Specify,if and how the {@link ABI} of all children in the structure, shall be overwritten with the
     * ABI provide by this annotation.
     */
    @NotNull OverwriteChildABI overwriteChildrenABI() default OverwriteChildABI.NO_OVERWRITE;
}
