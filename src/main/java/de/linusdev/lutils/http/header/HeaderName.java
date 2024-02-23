package de.linusdev.lutils.http.header;

import de.linusdev.lutils.http.header.value.HeaderValue;
import org.jetbrains.annotations.NotNull;

public interface HeaderName {

    /**
     * @return name of this header
     */
    String getName();

    default Header with(String value) {
        return new HeaderImpl(getName(), value);
    }

    default Header with(@NotNull HeaderValue value) {
        return new HeaderImpl(getName(), value.asString());
    }

}
