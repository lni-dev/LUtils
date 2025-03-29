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

package de.linusdev.lutils.net.ws.control;

/**
 * <h1>Websocket Status Codes</h1>
 * <ul>
 *     <li>{@link #NORMAL_CLOSURE}</li>
 *     <li>{@link #GOING_AWAY}</li>
 *     <li>{@link #PROTOCOL_ERROR}</li>
 *     <li>{@link #RECEIVED_UNACCEPTABLE_DATA_TYPE}</li>
 * </ul>
 * @see <a href="https://datatracker.ietf.org/doc/html/rfc6455#section-7.4">RFC 6455</a>
 */
public enum WSStatusCodes implements WSStatusCode {

    NORMAL_CLOSURE(1000),
    GOING_AWAY(1001),
    PROTOCOL_ERROR(1002),
    RECEIVED_UNACCEPTABLE_DATA_TYPE(1003),
    ;

    private final int code;

    WSStatusCodes(int code) {
        this.code = code;
    }

    @Override
    public int code() {
        return code;
    }
}
