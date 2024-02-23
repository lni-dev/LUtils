package de.linusdev.lutils.http.header.contenttype;

import de.linusdev.lutils.http.header.value.BasicHeaderValueImpl;
import de.linusdev.lutils.http.header.value.parameters.BasicHeaderValueWithCharset;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ContentTypes extends BasicHeaderValueImpl implements ContentType {

    public static class Text extends ContentTypes implements BasicHeaderValueWithCharset {

        @Contract(" -> new")
        public static @NotNull Text html() {
            return new Text("html");
        }
        @Contract(" -> new")
        public static @NotNull Text plain() {
            return new Text("plain");
        }

        @Contract(" -> new")
        public static @NotNull Text csv() {
            return new Text("csv");
        }

        protected Text(@NotNull String name) {
            super("text/" + name);
        }

        @Override
        public @NotNull Text setCharset(@Nullable String charset) {
            BasicHeaderValueWithCharset.super.setCharset(charset);
            return this;
        }
    }

    ContentTypes(@NotNull String name) {
        super(name);
    }
}
