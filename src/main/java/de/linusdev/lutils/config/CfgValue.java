package de.linusdev.lutils.config;

import de.linusdev.lutils.interfaces.TConverter;
import de.linusdev.lutils.result.BiResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import static de.linusdev.lutils.config.Configuration.load;

/**
 * A config value. Will be automatically {@link Configuration#load(CfgValue) loaded} when the constructor is called.
 * @param <T> config value type
 * @see Configuration#addArgs(String[], Map)
 */
public class CfgValue<T> {
    private final @NotNull String key;
    private final @NotNull TConverter<String, T, Throwable> converter;
    private final @Nullable Function<T, BiResult<Boolean, @Nullable String>> isChangeAllowed;

    private final @NotNull AtomicReference<T> value = new AtomicReference<>(null);

    /**
     *
     * @param key config value key
     * @param converter value converter
     * @param isChangeAllowed function which checks if the value can be changed. Must return a boolean and a reason string.
     */
    public CfgValue(
            @NotNull String key,
            @NotNull TConverter<String, T, Throwable> converter,
            @Nullable Function<T, BiResult<Boolean, @Nullable String>> isChangeAllowed
    ) {
        this.key = key;
        this.converter = converter;
        this.isChangeAllowed = isChangeAllowed;
        load(this);
    }

    public CfgValue(
            @NotNull String key,
            @NotNull TConverter<String, T, Throwable> converter
    ) {
       this(key, converter, null);
    }

    public @NotNull String getKey() {
        return key;
    }

    public T get() {
        return value.get();
    }

    /**
     * Set the value of this config to given {@code value} and ignores {@link #isChangeAllowed}.
     */
    public synchronized void forceSet(T value) {
        T oldValue = this.value.get();
        if(Objects.equals(oldValue, value))
            return;
        this.value.set(value);
        onChanged(oldValue, value);
    }

    public void set(@NotNull String value) throws Throwable {
        T converted = converter.convert(value);

        if(isChangeAllowed != null) {
            BiResult<Boolean, String> allowed = isChangeAllowed.apply(converted);

            if(!allowed.result1()) {
                throw new IllegalStateException(
                        "Changing the config value '" + getKey() + "' is not allowed." +
                        (allowed.result2() == null ? "" : " Reason: " + allowed.result2())
                );
            }
        }

        forceSet(converted);
    }


    @SuppressWarnings("unused")
    protected void onChanged(T oldValue, T newValue) { }
}
