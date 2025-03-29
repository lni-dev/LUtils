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

package de.linusdev.lutils.nat.enums;

/**
 * Java does not support to set a custom ordinal value of an enum member.
 * In C the value of an enum can be any arbitrary 32 bit int value. This interface aims to solve this problem.
 * <br><br>
 * This interface should be implemented by Java enums, that correspond to native 32 bit enums.
 * Variables that hold a value of this enum should be of type {@link NativeEnumValue32}.
 */
public interface NativeEnumMember32 {

    /**
     * The value used in the native enum member counterpart to this enum member.
     */
    int getValue();

}
