/*
 * Copyright (c) 2025 Linus Andera
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

package de.linusdev.lutils.pack;

import de.linusdev.llog.LLog;
import de.linusdev.llog.base.LogInstance;
import de.linusdev.lutils.id.Identifier;
import de.linusdev.lutils.pack.errors.PackLoadingException;
import de.linusdev.lutils.version.Version;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Represents a pack containing resources.
 * <br><br>
 * All Subclasses must implement {@link Object#equals(Object) equals()} and {@link Object#hashCode() hashCode()} and call
 * {@link #equals(Pack, Object)} and {@link #hashcode(Pack)} respectively.
 */
public interface Pack {

    static boolean equals(@NotNull Pack that, @Nullable Object other) {
        if(other instanceof Pack otherPack)
            return equals(that, otherPack);
        return false;
    }

    static boolean equals(@NotNull Pack that, @Nullable Pack other) {
        if(other == null) return false;
        if(!that.location().equals(other.location()))
            return false;

        if(!that.getClass().equals(other.getClass()))
            return false;

        if(!that.isLoaded())
            return true; // We have checked all information given that 'that' is not loaded

        if(!other.isLoaded())
            return false;

        return Identifier.equals(that.id(), other.id());
    }

    static int hashcode(@NotNull Pack that) {
        int hashcode = that.location().hashCode();
        hashcode = hashcode * 31 + that.getClass().hashCode();
        if(that.isLoaded())
            hashcode = hashcode * 31 + Identifier.hashCode(that.id());
        return hashcode;
    }

    @NotNull LogInstance LOG = LLog.getLogInstance();

    /**
     * Loads this pack.
     * @see #isLoaded()
     * @throws PackLoadingException if the pack could not be loaded
     */
    void load() throws PackLoadingException;

    /**
     * Checks if this is a valid pack. Should be checked before calling {@link #load()}.
     * @return {@code true} if this is a valid pack. Otherwise, {@code false}.
     */
    boolean isPack();

    /**
     * Whether this pack is loaded. A pack must be loaded before any
     * information (e.g. {@link #name() name}, {@link #description() description})
     * can be requested.
     */
    boolean isLoaded();

    /**
     * Name of the pack. May be displayed to the user.
     */
    @NotNull String name();

    /**
     * Id of the pack.
     */
    @NotNull Identifier id();

    /**
     * Description of the pack. May be displayed to the user.
     */
    @NotNull String description();

    /**
     * Version of the pack. May be displayed to the user.
     */
    @NotNull Version version();

    /**
     * A string describing the location of the pack as good as possible. Must be available even if the pack is not
     * yet {@link #isLoaded() loaded}.
     */
    @NotNull String location();

    /**
     * Resolves an input stream to a file in this pack.
     * @param file path to file relative to the pack. Must start with a {@code /}.
     * @return {@link InputStream} to read the file.
     * @throws FileNotFoundException if the file does not exist.
     */
    @NotNull InputStream resolve(@NotNull String file) throws IOException;

    /**
     * Whether given {@code file} exists in this pack.
     */
    boolean exists(@NotNull String file);
}
