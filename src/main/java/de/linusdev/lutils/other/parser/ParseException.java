/*
 * Copyright (c) 2024-2025 Linus Andera
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

package de.linusdev.lutils.other.parser;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An exception thrown while parsing
 */
public class ParseException extends Exception {

    private final @Nullable ParseTracker tracker;

    public ParseException() {
        tracker = null;
    }

    /**
     * Create new exception with a custom message.
     */
    public ParseException(@NotNull String message) {
        super(message);
        tracker = null;
    }

    /**
     * Create new exception with message {@code Illegal character ...}.
     */
    public ParseException(char c) {
        this("Illegal character '" + c + "'");
    }

    /**
     * Create new exception with message {@code Illegal character ...}.
     */
    public ParseException(@Nullable ParseTracker tracker, char c) {
        this(tracker, "Illegal character '" + c + "' " + "(0x" + Integer.toHexString(c) + ")");
    }

    public ParseException(@Nullable ParseTracker tracker) {
        super();
        this.tracker = tracker;
    }

    public ParseException(@Nullable ParseTracker tracker, @Nullable String message) {
        super();
        this.tracker = tracker;
    }

    @Override
    public String getMessage() {
        String msg = super.getMessage();

        if(tracker == null)
            return msg + ".";

        return msg + " in line " + tracker.getLine();
    }

    public @Nullable ParseTracker getParseTracker() {
        return tracker;
    }
}
