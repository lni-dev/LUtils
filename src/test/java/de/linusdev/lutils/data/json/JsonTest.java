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

package de.linusdev.lutils.data.json;

import de.linusdev.lutils.data.json.parser.JsonParser;
import de.linusdev.lutils.other.parser.ParseException;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class JsonTest {

    public static @Nullable String toUpperCase(@Nullable String string) {
        if(string == null)
            return null;
        return string.toUpperCase(Locale.ROOT);
    }

    @Test
    void convert() throws IOException, ParseException {
        Json json = JsonParser.DEFAULT_INSTANCE.parseString("{\"test\": \"some string\", \"null\": null}");

        assertEquals(
                "SOME STRING",
                json.convert("test", (String s) -> s.toUpperCase(Locale.ROOT), "a", "b")
        );

        assertEquals(
                "DOES NOT EXIST",
                json.convert("test2", JsonTest::toUpperCase, "DOES NOT EXIST", "b")
        );

        assertEquals(
                "NULL",
                json.<String, String, RuntimeException>convert("null", String::toUpperCase, "DOES NOT EXIST", "NULL")
        );

        assertEquals(
                "SOME STRING",
                json.convert("test", (String s) -> s.toUpperCase(Locale.ROOT), "a")
        );

        assertEquals(
                "DOES NOT EXIST",
                json.convert("test2", JsonTest::toUpperCase, "DOES NOT EXIST")
        );

        assertNull(json.convert("null", JsonTest::toUpperCase, "DOES NOT EXIST"));

        assertEquals(
                "SOME STRING",
                json.convert("test", (String s) -> s.toUpperCase(Locale.ROOT))
        );

        assertNull(json.convert("test2", JsonTest::toUpperCase));
        assertNull(json.convert("null", JsonTest::toUpperCase));

        assertEquals(
                "SOME STRING",
                json.convert("test", (String s) -> s.toUpperCase(Locale.ROOT), "a", "b")
        );

        assertEquals(
                "DOES NOT EXIST OR NULL",
                json.getAndConvertOrDefault("test2", JsonTest::toUpperCase, "DOES NOT EXIST OR NULL")
        );

        assertEquals(
                "DOES NOT EXIST OR NULL",
                json.getAndConvertOrDefault("null", JsonTest::toUpperCase, "DOES NOT EXIST OR NULL")
        );

    }
}