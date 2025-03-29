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

package de.linusdev.lutils.net.routing;

import de.linusdev.lutils.net.http.HTTPMessageBuilder;
import de.linusdev.lutils.net.http.HTTPRequest;
import de.linusdev.lutils.net.http.HTTPResponse;
import de.linusdev.lutils.net.http.body.Bodies;
import de.linusdev.lutils.net.http.body.UnparsedBody;
import de.linusdev.lutils.net.http.status.StatusCodes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

@SuppressWarnings("unused")
public interface RequestHandler {

    static @NotNull RequestHandler ofHtmlResource(@Nullable Class<?> clazz, @NotNull String name) {
        return request -> HTTPResponse.builder().setStatusCode(StatusCodes.OK).setBody(Bodies.html().ofResource(clazz, name));
    }

    static @NotNull RequestHandler ofHtmlResource(@NotNull String name) {
        return ofHtmlResource(null, name);
    }

    static @NotNull RequestHandler ofCssResource(@Nullable Class<?> clazz, @NotNull String name) {
        return request -> HTTPResponse.builder().setStatusCode(StatusCodes.OK).setBody(Bodies.css().ofResource(clazz, name));
    }

    static @NotNull RequestHandler ofCssResource(@NotNull String name) {
        return ofCssResource(null, name);
    }

    static @NotNull RequestHandler ofJsResource(@Nullable Class<?> clazz, @NotNull String name) {
        return request -> HTTPResponse.builder().setStatusCode(StatusCodes.OK).setBody(Bodies.javascript().ofResource(clazz, name));
    }

    static @NotNull RequestHandler ofJsResource(@NotNull String name) {
        return ofJsResource(null, name);
    }

    @Nullable HTTPMessageBuilder handle(@NotNull HTTPRequest<UnparsedBody> request);

    default @Nullable HTTPMessageBuilder handle(@NotNull RoutingState state) throws IOException {
        return handle(state.getRequest());
    }

}
