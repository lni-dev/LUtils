

package de.linusdev.lutils.version;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a version of the form {@code major.minor.patch}
 */
public interface SimpleVersion {

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

}
