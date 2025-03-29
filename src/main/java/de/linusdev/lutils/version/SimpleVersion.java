

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

package de.linusdev.lutils.version;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a version of the form {@code major.minor.patch}
 */
public interface SimpleVersion extends Comparable<SimpleVersion> {

    static @NotNull SimpleVersion of(int major, int minor, int patch) {
        return new SimpleVersionImpl(major, minor, patch);
    }

    /**
     * Parses versions of the following form:
     * <ul>
     *     <li>"1.2.3" -&gt; 1.2.3</li>
     *     <li>"1.2"   -&gt; 1.2.0</li>
     *     <li>"1"     -&gt; 1.0.0</li>
     * </ul>
     */
    static @NotNull SimpleVersion of(@NotNull String simpleVersion) {
        var parts = simpleVersion.split("\\.");

        return of(
                Integer.parseInt(parts[0]),
                parts.length >= 2 ? Integer.parseInt(parts[1]) : 0,
                parts.length >= 3 ? Integer.parseInt(parts[2]) : 0
        );
    }

    int major();
    int minor();
    int patch();

    default @NotNull String getAsUserFriendlyString() {
        return major() + "." + minor() + "." + patch();
    }

    @Override
    default int compareTo(@NotNull SimpleVersion other) {
        if(this.major() > other.major())
            return 3;
        else if (this.major() < other.major()) {
            return -3;
        }

        // this.major() == other.major()

        if(this.minor() > other.minor())
            return 2;
        else if (this.minor() < other.minor()) {
            return -2;
        }

        // this.minor() == other.minor()

        if(this.patch() > other.patch())
            return 1;
        else if (this.patch() < other.patch()) {
            return -1;
        }

        // this.patch() == other.patch()

        return 0;
    }
}
