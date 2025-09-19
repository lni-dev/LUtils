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
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void isNull() throws IOException, ParseException {
        Json json = JsonParser.DEFAULT_INSTANCE.parseString("{\"test\": \"some string\", \"null\": null}");

        assertThrows(NoSuchElementException.class, () -> json.isNull("key does not exist :)"));
        assertTrue(json.isNull("null"));
        assertFalse(json.isNull("test"));

    }

    @Test
    void getAs() throws IOException, ParseException {
        Json json = JsonParser.DEFAULT_INSTANCE.parseString("{\"test\": \"some string\", \"null\": null}");

        assertEquals("some string", json.getAs("test"));
        assertNull(json.getAs("null"));
        assertNull(json.getAs("does not exist"));

        assertEquals("some string", json.getAs("test", "defaultIfAbsent"));
        assertNull(json.getAs("null", "defaultIfAbsent"));
        assertEquals("defaultIfAbsent", json.getAs("does not exist", "defaultIfAbsent"));

        assertEquals("some string", json.getAs("test", "defaultIfAbsent", "defaultIfNull"));
        assertEquals("defaultIfNull",json.getAs("null", "defaultIfAbsent", "defaultIfNull"));
        assertEquals("defaultIfAbsent", json.getAs("does not exist", "defaultIfAbsent", "defaultIfNull"));

        assertDoesNotThrow(() -> json.requireNotNullAndGetAs("test"));
        assertThrows(NullPointerException.class, () -> json.requireNotNullAndGetAs("null"));
        assertThrows(NullPointerException.class, () -> json.requireNotNullAndGetAs("does not exist"));

    }

    @Test
    void process() throws IOException, ParseException {
        Json json = JsonParser.DEFAULT_INSTANCE.parseString("{\"test\": \"some string\", \"null\": null}");

        AtomicBoolean processed = new AtomicBoolean(false);


        json.process("test", (String str) ->  {
            processed.set(true);
            assertEquals("some string", str);
        });
        assertTrue(processed.getAndSet(false));

        json.process("null", (String str) ->  {
            processed.set(true);
            assertNull(str);
        });
        assertTrue(processed.getAndSet(false));

        json.process("doesNotExist", (String str) ->  {
            processed.set(true);
            assertNull(str);
        });
        assertTrue(processed.getAndSet(false));




        json.processIfNotNull("test", (String str) ->  {
            processed.set(true);
            assertEquals("some string", str);
        });
        assertTrue(processed.getAndSet(false));

        json.processIfNotNull("null", (String str) -> processed.set(true));
        assertFalse(processed.getAndSet(false));

        json.processIfNotNull("doesNotExist", (String str) -> processed.set(true));
        assertFalse(processed.getAndSet(false));




        json.processIfExistent("test", (String str) ->  {
            processed.set(true);
            assertEquals("some string", str);
        });
        assertTrue(processed.getAndSet(false));

        json.processIfExistent("null", (String str) ->  {
            processed.set(true);
            assertNull(str);
        });
        assertTrue(processed.getAndSet(false));

        json.processIfExistent("doesNotExist", (String str) -> processed.set(true));
        assertFalse(processed.getAndSet(false));
    }

    @Test
    void requireNotNullAndConvert() throws IOException, ParseException {
        Json json = JsonParser.DEFAULT_INSTANCE.parseString("{\"test\": \"some string\", \"null\": null}");

        assertEquals("SOME STRING", json.requireNotNullAndConvert("test", JsonTest::toUpperCase));
        assertThrows(NullPointerException.class, () -> json.requireNotNullAndConvert("null", JsonTest::toUpperCase));
        assertThrows(NullPointerException.class, () -> json.requireNotNullAndConvert("does not exist", JsonTest::toUpperCase));
    }

    @Test
    void requireNotNullAndProcess() throws IOException, ParseException {
        Json json = JsonParser.DEFAULT_INSTANCE.parseString("{\"test\": \"some string\", \"null\": null}");
        AtomicBoolean processed = new AtomicBoolean(false);

        json.requireNotNullAndProcess("test", (String str) -> {
            assertEquals("some string", str);
            processed.set(true);
        });
        assertTrue(processed.getAndSet(false));

        assertThrows(NullPointerException.class, () -> json.requireNotNullAndProcess("null", (String str) -> processed.set(true)));
        assertFalse(processed.getAndSet(false));

        assertThrows(NullPointerException.class, () -> json.requireNotNullAndProcess("does not exist", (String str) -> processed.set(true)));
        assertFalse(processed.getAndSet(false));
    }
}