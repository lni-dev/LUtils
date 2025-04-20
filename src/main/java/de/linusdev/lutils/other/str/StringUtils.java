package de.linusdev.lutils.other.str;

import org.intellij.lang.annotations.RegExp;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class StringUtils {

    /**
     * Adds given {@code indent} to the start of every line of given {@code toIndent}.
     * If {@code indentFirstLine} is false, the indent will not be added to the first line.
     * @param toIndent the String to indent
     * @param indent the indent which will be added to the start of every line
     * @param indentFirstLine whether to indent the first line too.
     * @return The indented string as described above.
     */
    public static @NotNull String indent(@NotNull String toIndent, @NotNull String indent, boolean indentFirstLine) {

        if(!indentFirstLine) {
            int lineBreakIndex = toIndent.indexOf('\n');

            if(lineBreakIndex == -1)
                return toIndent;

            return toIndent.substring(0, lineBreakIndex) + "\n" + indent(toIndent.substring(lineBreakIndex + 1), indent, true);
        }

        return toIndent
                .lines()
                .map(s -> indent + s)
                .collect(Collectors.joining("\n", "", ""));
    }

    @RegExp
    public static final String REPLACE_KEY_PATTERN_STRING = "\\$\\{(?<key>[a-zA-Z0-9-_]+)}";
    public static final Pattern REPLACE_KEY_PATTERN = Pattern.compile(REPLACE_KEY_PATTERN_STRING);

    /**
     * Checks if a replace-key with the syntax {@code ${key}} is present in given {@code text}. If at least once replace-key is present,
     * a {@link PartsString} is created.
     * <br><br>
     * Replace-keys must match the regular expression {@value #REPLACE_KEY_PATTERN_STRING}.
     * @param text the value
     * @return {@link PartsString} for given {@code text} or {@code null} if no replace-key is present in {@code text}.
     */
    public static @Nullable ConstructableString computePossibleConstructableStringOfText(@Nullable String text) {
        if(text == null || text.isEmpty())
            return null;

        Matcher matcher = REPLACE_KEY_PATTERN.matcher(text);

        if(!matcher.find())
            return null;

        return buildConstructableString(matcher, text);
    }

    /**
     * Parses all replace-keys with the syntax {@code ${key}} in given {@code text}. If at least one replace-key is present,
     * a {@link PartsString} is created. Otherwise, a {@link ConstString} is created.
     * <br><br>
     * Replace-keys must match the regular expression {@value #REPLACE_KEY_PATTERN_STRING}.
     * @param text the value
     * @return {@link ConstructableString} for given {@code text} or {@code null} if given {@code text} is {@code null}.
     */
    @Contract("!null -> !null; null -> null")
    public static @Nullable ConstructableString getConstructableStringOfText(@Nullable String text) {
        if(text == null)
            return null;

        Matcher matcher = REPLACE_KEY_PATTERN.matcher(text);

        if(!matcher.find())
            return new ConstString(text);

        return buildConstructableString(matcher, text);
    }

    private static @NotNull PartsString buildConstructableString(@NotNull Matcher matcher, @NotNull String text) {
        PartsString.Builder builder = new PartsString.Builder();
        int start = 0;
        do {
            String constant = text.substring(start, matcher.start());
            if(!constant.isEmpty())
                builder.addConstant(constant);
            builder.addPlaceholder(matcher.group("key"));

            start = matcher.end();

        } while (matcher.find());

        if (start != text.length())
            builder.addConstant(text.substring(start));

        return builder.build();
    }

}
