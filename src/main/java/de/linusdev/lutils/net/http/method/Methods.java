/*
 * Copyright (c) 2023-2025 Linus Andera
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

package de.linusdev.lutils.net.http.method;

import org.jetbrains.annotations.NotNull;

/**
 * Basic HTTP request methods.
 *
 * <ul>
 *     <li>{@link #GET} - Retrieves a resource without modifying it.</li>
 *     <li>{@link #HEAD} - Retrieves only the headers of a resource, without its response body.</li>
 *     <li>{@link #POST} - Submits data to a resource, commonly to create a new resource or trigger an action.</li>
 *     <li>{@link #PUT} - Creates or completely replaces a resource at a specified URI.</li>
 *     <li>{@link #DELETE} - Removes a resource identified by the request URI.</li>
 *     <li>{@link #CONNECT} - Establishes a tunnel to the server, commonly used by HTTP proxies for HTTPS.</li>
 *     <li>{@link #OPTIONS} - Describes the communication options supported by a resource or server.</li>
 *     <li>{@link #TRACE} - Performs a diagnostic loop-back test to inspect the request as received by the server.</li>
 * </ul>
 */
public enum Methods implements RequestMethod {
    GET("GET"),
    HEAD("HEAD"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE"),
    CONNECT("CONNECT"),
    OPTIONS("OPTIONS"),
    TRACE("TRACE"),
    ;

    private final String name;

    Methods(String name) {
        this.name = name;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
