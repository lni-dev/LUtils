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

package de.linusdev.lutils.other.debug;

import de.linusdev.lutils.other.array.ArrayUtils;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DebuggableTest {

    private record T(
            String value1,
            int anInt,
            double aDouble,
            T subRecord,
            List<String> subList,
            Map<String, String> subMap
    ){}

    @Test
    void getDebugInfoString() {

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("k1", "v1");
        map.put("k2", "v2");

        T t = new T("some value", 342, 342.9898,
                new T("wow", 1, 0.9, null, null, null),
                List.of("s1", "s2", "s3"),
                map
        );

        assertEquals("Record 'T': [ Too deep, skipping... ]", Debuggable.getDebugInfoString(t, 0));

        assertEquals("""
                Record 'T':\s
                 * value1: some value
                 * anInt: 342
                 * aDouble: 342.9898
                 * subRecord: Record 'T': [ Too deep, skipping... ]
                 * subList: ListN: [ Too deep, skipping... ]
                 * subMap: LinkedHashMap: [ Too deep, skipping... ]""", Debuggable.getDebugInfoString(t, 1));

        assertEquals("""
                Record 'T':\s
                 * value1: some value
                 * anInt: 342
                 * aDouble: 342.9898
                 * subRecord: Record 'T':\s
                    * value1: wow
                    * anInt: 1
                    * aDouble: 0.9
                    * subRecord: null
                    * subList: null
                    * subMap: null
                 * subList: ListN:\s
                    * size: 3
                    * items:\s
                      - s1
                      - s2
                      - s3
                 * subMap: LinkedHashMap:\s
                    * size: 2
                    * entries:\s
                      - v1: v1
                      - v2: v2""", Debuggable.getDebugInfoString(t, 2));


        assertEquals("""
                Some String:\s
                 * Some Info!""", new DebugInfoStringBuilder("Test", "Some String", 10)
                .addInformation("Some Info!")
                .build()
        );


    }

    @Test
    void addListInline() {
        assertEquals("""
                Some String:\s
                 * inline-list: [ 1, 2, 4, 5 ]""", new DebugInfoStringBuilder("Test", "Some String", 10)
                .addListInline("inline-list", ArrayUtils.iterableArray(new int[]{1, 2, 4, 5}))
                .build()
        );
    }
}