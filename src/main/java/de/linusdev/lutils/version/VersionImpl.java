
package de.linusdev.lutils.version;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VersionImpl implements Version{

    private final @NotNull ReleaseType type;
    private final @NotNull SimpleVersion version;
    private final @Nullable String prefix;
    private final @Nullable String postfix;

    public VersionImpl(@NotNull ReleaseType type, @NotNull SimpleVersion version, @Nullable String prefix, @Nullable String postfix) {
        this.type = type;
        this.version = version;
        this.prefix = prefix;
        this.postfix = postfix;
    }

    @Override
    public @NotNull ReleaseType type() {
        return type;
    }

    @Override
    public @NotNull SimpleVersion version() {
        return version;
    }

    @Override
    public @Nullable String prefix() {
        return prefix;
    }

    @Override
    public @Nullable String postfix() {
        return postfix;
    }

    @Override
    public String toString() {
        return getAsUserFriendlyString();
    }

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override
    public boolean equals(Object obj) {
        return Version.equals(this, obj);
    }
}