
/*
 * Copyright (c) 2024 Linus Andera
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.linusdev.lutils.version;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a version of the following form:
 * {@code [prefix-]simpleVersion[-postfix][:typeAppendix]}
 * <br>Examples:
 * <ul>
 *     <li>{@code 1.0.0}</li>
 *     <li>{@code game-1.0.0-f1:early-access}</li>
 *     <li>{@code 1.0.0:beta}</li>
 * </ul>
 */
public interface Version extends Comparable<Version> {

    static @NotNull Version of(
            @NotNull ReleaseType type,
            int major, int minor, int patch
    ) {
        return of(
                type,
                SimpleVersion.of(major, minor, patch),
                null, null
        );
    }

    static @NotNull Version of(
            @NotNull ReleaseType type,
            @NotNull String simpleVersion
    ) {
        return of(
                type,
                SimpleVersion.of(simpleVersion),
                null, null
        );
    }

    static @NotNull Version of(
            @NotNull ReleaseType type,
            @NotNull SimpleVersion simpleVersion,
            @Nullable String prefix,
            @Nullable String postfix
    ) {
        return new VersionImpl(type, simpleVersion, prefix, postfix);
    }

    @NotNull Pattern VERSION_PATTERN = Pattern.compile(
            "((?<prefix>.+)-)?(?<version>\\d+.\\d+.\\d+)(-(?<postfix>[^-:]+))?(:(?<typeAppendix>.+))?"
    );

    /**
     * Constructs a {@link Version} of given string. string must match the {@link #VERSION_PATTERN}
     */
    static @NotNull Version of(
            @NotNull String version
    ) {
        Matcher matcher = VERSION_PATTERN.matcher(version);
        if(!matcher.find()) {
            throw new IllegalStateException("Illegal version '" + version + "'");
        }
        return of(
                ReleaseType.ofAppendix(matcher.group("typeAppendix")),
                SimpleVersion.of(matcher.group("version")),
                matcher.group("prefix"),
                matcher.group("postfix")
        );
    }

    @NotNull ReleaseType type();

    @NotNull SimpleVersion version();

    @Nullable String prefix();

    @Nullable String postfix();

    default @NotNull String getAsUserFriendlyString() {
        String prefix = prefix() == null ? "" : prefix() + "-";
        String postfix = postfix() == null ? "" :  "-" + postfix();
        String typeAppendix = type().appendix() == null ? "" : ":" + type().appendix();
        return prefix + version().getAsUserFriendlyString() + postfix + typeAppendix;
    }

    /**
     * Same as {@link #getAsUserFriendlyString()}, but instead of a {@code :}, before
     * the release type, a underscore {@code _} is added instead.
     */
    default @NotNull String getAsArchiveReadyString() {
        String prefix = prefix() == null ? "" : prefix() + "-";
        String postfix = postfix() == null ? "" :  "-" + postfix();
        String typeAppendix = type().appendix() == null ? "" : "_" + type().appendix();
        return prefix + version().getAsUserFriendlyString() + postfix + typeAppendix;
    }

    static boolean equals(@Nullable Version that, @Nullable Object other) {
        if(that == other) return true;
        if(other == null) return false;
        if(that == null) return false;
        if(!(other instanceof Version otherVersion)) return false;

        return that.getAsUserFriendlyString().equals(otherVersion.getAsUserFriendlyString());
    }

    @Override
    default int compareTo(@NotNull Version other) {
        int v = this.version().compareTo(other.version()) * 2;
        if(v == 0) {
            if(this.type() == ReleaseType.RELEASE && other.type() != ReleaseType.RELEASE)
                return 1;
            if(this.type() != ReleaseType.RELEASE && other.type() == ReleaseType.RELEASE)
                return -1;
        }

        return v;
    }
}
