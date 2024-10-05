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

package de.linusdev.lutils.net.http.header;

public enum HeaderNames implements HeaderName {
    CONNECTION("Connection"),
    UPGRADE("Upgrade"),

    CONTENT_TYPE("Content-Type"),
    CONTENT_LENGTH("Content-Length"),

    SEC_WEBSOCKET_KEY("Sec-WebSocket-Key"),
    SEC_WEBSOCKET_VERSION("Sec-WebSocket-Version"),
    SEC_WEBSOCKET_ACCEPT("Sec-WebSocket-Accept"),
    ;

    private final String name;

    HeaderNames(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
