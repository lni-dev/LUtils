

package de.linusdev.lutils.version;

import org.jetbrains.annotations.NotNull;

public class SimpleVersionImpl implements SimpleVersion {

    private final int major;
    private final int minor;
    private final int patch;

    public SimpleVersionImpl(int major, int minor, int patch) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    @Override
    public int major() {
        return major;
    }

    @Override
    public int minor() {
        return minor;
    }

    @Override
    public int patch() {
        return patch;
    }

    @Override
    public @NotNull String toString() {
        return getAsUserFriendlyString();
    }
}