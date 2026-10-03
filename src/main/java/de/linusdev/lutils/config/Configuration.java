package de.linusdev.lutils.config;

import de.linusdev.lutils.other.log.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Configuration {
    protected static @NotNull Logger LOG = Logger.getLogger();

    private static final @NotNull Pattern KEY_VALUE_PATTERN = Pattern.compile("(?<key>[^=]+)=(?<value>.*)");
    protected static final @NotNull Map<String, String> ARGS = new ConcurrentHashMap<>();

    /**
     * Set arguments passed a main method. These will be available to all {@link CfgValue configs} {@link #load(CfgValue) loaded}
     * after calling this method.
     * <br><br>
     * The following argument types can be parsed:
     * <ul>
     *     <li>--key=value</li>
     *     <li>--key value</li>
     *     <li>-key (Parsed as flag, sets the value to true)</li>
     * </ul>
     *
     * @param args the args array directly from the main method. {@code args[0]} will be ignored.
     * @param substituteNames maps argument names to {@link CfgValue#getKey() config keys}.
     */
    public static void addArgs(String[] args, @Nullable Map<String, String> substituteNames) {
        if(args.length <= 1)
            return;

        String key = null;

        for (int i = 1; i < args.length; i++) {
            String arg = args[i];

            if(arg.startsWith("--")) {
                arg = arg.substring(2);
                Matcher matcher = KEY_VALUE_PATTERN.matcher(arg);

                if(matcher.matches()) {
                    key = matcher.group("key");
                    String value = matcher.group("value");

                    if(substituteNames != null)
                        key = substituteNames.getOrDefault(key, key);

                    ARGS.put(key, value);
                    key = null;

                } else {
                    key = arg;
                    if(substituteNames != null)
                        key = substituteNames.getOrDefault(key, key);
                }

            } else if (arg.startsWith("-")) {
                key = arg.substring(1);
                if(substituteNames != null)
                    key = substituteNames.getOrDefault(key, key);
                ARGS.put(key, "true");
                key = null;

            } else {
                if(key == null)
                    continue;
                ARGS.put(key, arg);
                key = null;

            }
        }
    }

    /**
     * Load the value of given {@link CfgValue config}. The config value priority is:
     * <ul>
     *     <li>{@link #ARGS Arguments} (set by {@link #addArgs(String[], Map)})</li>
     *     <li>{@link System#getProperties() System properties}</li>
     *     <li>{@link System#getenv() Evniornment variables}</li>
     * </ul>
     * <b>No</b> error is thrown, if the configuration fails to load because given {@code value} cannot be changed. However
     * an error will be logged to {@link #LOG}.
     * @param value the config to load.
     */
    protected static void load(@NotNull CfgValue<?> value) {
        try {
            LOG.debug("Checking config value '" + value.getKey() + "':" +
                    "\nproperty: " + System.getProperty(value.getKey()) +
                    "\nenvironment: " + System.getenv(value.getKey())
            );

            String property = System.getProperty(value.getKey());
            String env = System.getenv(value.getKey());
            String arg = ARGS.get(value.getKey());

            if(arg != null) {
                value.set(arg);
            } else if(property != null) {
                value.set(property);
            } else if(env != null) {
                value.set(env);
            }

        } catch (Throwable e) {
            LOG.error("Failed to set config value '" + value.getKey() + "':", e);
        }
    }
}
