package de.linusdev.lutils.http.header.contenttype;

import de.linusdev.lutils.http.header.Header;
import de.linusdev.lutils.http.header.HeaderNames;
import de.linusdev.lutils.http.header.value.BasicHeaderValue;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ContentType extends BasicHeaderValue {

    @Contract("_ -> new")
    static @NotNull ContentType of(@NotNull String name) {
        return new ContentTypes(name);
    }

    /**
     * This {@link ContentType} as content-type {@link Header}
     */
    default Header asHeader() {
        return PARSER.parse(HeaderNames.CONTENT_TYPE, this);
    }

    @Override
    @NotNull
    default ContentType set(@NotNull String name, @Nullable String value) {
        BasicHeaderValue.super.set(name, value);
        return this;
    }

    @Override
    @NotNull
    default ContentType add(@NotNull String value, boolean check) {
        BasicHeaderValue.super.add(value, check);
        return this;
    }

    @Override
    @NotNull
    default ContentType add(@NotNull String value) {
        BasicHeaderValue.super.add(value);
        return this;
    }

    @Override
    @NotNull
    default ContentType remove(@NotNull String value) {
        BasicHeaderValue.super.remove(value);
        return this;
    }
}
