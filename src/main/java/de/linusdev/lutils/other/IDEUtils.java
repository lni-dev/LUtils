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

package de.linusdev.lutils.other;

import org.jetbrains.annotations.NotNull;

import java.net.URISyntaxException;

@SuppressWarnings("unused")
public class IDEUtils {

    /**
     * Should not be called in production code. This function is not reliable!
     * @param yourMainClass class containing your main() method.
     * @return {@code true} if your compiled main class is not contained in a jar file.
     */
    public static boolean isRunFromIDE(@NotNull Class<?> yourMainClass) {
        try {
            return !yourMainClass
                    .getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI()
                    .getPath()
                    .endsWith(".jar");
        } catch (URISyntaxException e) {
            return false;
        }
    }

}
