package de.linusdev.lutils.config;

import de.linusdev.lutils.interfaces.TConverter;
import de.linusdev.lutils.other.log.Logger;
import de.linusdev.lutils.result.BiResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

public class CfgValue<T> {

    protected static @NotNull Logger LOG = Logger.getLogger();

    protected static void load(@NotNull CfgValue<?> value) {
        try {
            LOG.debug("Checking config value '" + value.getKey() + "':" +
                    "\nproperty: " + System.getProperty(value.getKey()) +
                    "\nenvironment: " + System.getenv(value.getKey())
            );

            String property = System.getProperty(value.getKey());
            String env = System.getenv(value.getKey());

            if(property != null) {
                value.set(property);
            } else if(env != null) {
                value.set(env);
            }
        } catch (Throwable e) {
            LOG.error("Failed to set config value '" + value.getKey() + "':", e);
        }
    }

    private final @NotNull String key;
    private final @NotNull TConverter<String, T, Throwable> converter;
    private final @Nullable Function<T, BiResult<Boolean, @Nullable String>> isChangeAllowed;

    private final @NotNull AtomicReference<T> value = new AtomicReference<>(null);

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
