package de.linusdev.lutils.http.header;

public enum HeaderNames implements HeaderName {
    CONTENT_TYPE("Content-Type"),
    CONTENT_LENGTH("Content-Length"),
    ;

    private final String name;

    HeaderNames(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
