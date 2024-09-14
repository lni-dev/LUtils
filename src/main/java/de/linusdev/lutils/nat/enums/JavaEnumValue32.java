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

package de.linusdev.lutils.nat.enums;

public class JavaEnumValue32 <M extends NativeEnumMember32> implements EnumValue32<M> {

    public int value;

    public JavaEnumValue32() {
        this.value = DEFAULT_VALUE;
    }

    public JavaEnumValue32(int value) {
        this.value = value;
    }

    public JavaEnumValue32(M value) {
        this.value = value.getValue();
    }

    @Override
    public void set(int value) {
        this.value = value;
    }

    @Override
    public int get() {
        return value;
    }

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override
    public boolean equals(Object obj) {
        return EnumValue32.equals(this, obj);
    }
}
