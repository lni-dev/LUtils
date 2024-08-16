

package de.linusdev.lutils.version;

import de.linusdev.lutils.other.UnknownConstantException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum ReleaseType {

    EARLY_ACCESS("early-access"),

    RELEASE(null),
    SNAPSHOT("snapshot"),

    CLOSED_ALPHA("closed-alpha"),
    CLOSED_BETA("closed-beta"),
    ALPHA("alpha"),
    BETA("beta"),

    ;

    public static @NotNull ReleaseType ofAppendix(@Nullable String appendix) {
        if(appendix == null)
            return RELEASE;
        for (ReleaseType type : values()) {
            if(type.appendix != null && type.appendix.equals(appendix))
                return type;
        }

        throw new UnknownConstantException(appendix);
    }

    private final @Nullable String appendix;

    ReleaseType(@Nullable String appendix) {
        this.appendix = appendix;
    }

    public @Nullable String appendix() {
        return appendix;
    }
}
