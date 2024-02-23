package de.linusdev.lutils.http.header.value.parameters;

import de.linusdev.lutils.http.header.value.BasicHeaderValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface BasicHeaderValueWithCharset extends BasicHeaderValue {

    /**
     * {@link #set(String, String) Set} parameter 'charset' to given {@code charset}.
     * @param charset charset name or {@code null} to remove charset parameter
     */
    default @NotNull BasicHeaderValueWithCharset setCharset(@Nullable String charset) {
        set("charset", charset);
        return this;
    }

    default @Nullable String getCharset() {
        return get("charset");
    }

}
