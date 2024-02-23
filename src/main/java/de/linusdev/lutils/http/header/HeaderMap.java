package de.linusdev.lutils.http.header;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * {@link HeaderMap}&lt;String, Header&gt;, but the keys ignore case.
 */
public class HeaderMap implements Map<String, Header> {

    private final Map<String, Header> map;

    public HeaderMap() {
        this.map = new HashMap<>();
    }

    private String asKey(Object key) {
        return ((String) key).toLowerCase(Locale.ROOT);
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(asKey(key));
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public Header get(Object key) {
        return map.get(asKey(key));
    }

    public Header get(@NotNull HeaderName name) {
        return map.get(asKey(name.getName()));
    }

    @Nullable
    @Override
    public Header put(String key, @NotNull Header value) {
        return map.put(asKey(key), value);
    }

    @Nullable
    public Header put(String key, String value) {
        return map.put(asKey(key), new HeaderImpl(key, value));
    }

    @Nullable
    public Header put(@NotNull Header header) {
        return map.put(asKey(header.getKey()), header);
    }

    @Override
    public Header remove(Object key) {
        return map.remove(asKey(key));
    }

    public Header remove(@NotNull HeaderName name) {
        return map.remove(asKey(name.getName()));
    }

    @Override
    public void putAll(@NotNull Map<? extends String, ? extends Header> m) {
        m.forEach((key, value) -> map.put(asKey(key), value));
    }

    @Override
    public void clear() {
        map.clear();
    }

    @NotNull
    @Override
    public Set<String> keySet() {
        return map.keySet();
    }

    @NotNull
    @Override
    public Collection<Header> values() {
        return map.values();
    }

    @NotNull
    @Override
    public Set<Entry<String, Header>> entrySet() {
        return map.entrySet();
    }
}
