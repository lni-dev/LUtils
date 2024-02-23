package de.linusdev.lutils.http.header.value;

import de.linusdev.lutils.http.header.Header;
import de.linusdev.lutils.http.header.HeaderName;
import org.jetbrains.annotations.NotNull;

public interface HeaderValueParser<T> {

    /**
     * Parses given {@code header} to a header-value of type {@link T}
     * @param header {@link Header} who's value should be parsed
     * @return {@link T}
     */
    @NotNull T parse(@NotNull Header header);

    /**
     * Parse given header-value of type {@link T} to a header-ready string.
     * @param value header-value of type {@link T}
     * @return header-ready string representing {@link T}
     */
    @NotNull String parse(@NotNull T value);

    /**
     * Parses given header-value of type {@link T} to a {@link Header}.
     * @param name {@link HeaderName}
     * @param value header-value of type {@link T}
     * @return {@link Header} with given {@code name} and {@code value}
     */
    default @NotNull Header parse(@NotNull HeaderName name, @NotNull T value) {
        return name.with(parse(value));
    }
}
