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

package de.linusdev.lutils.routing;

import de.linusdev.lutils.http.HTTPMessageBuilder;
import de.linusdev.lutils.http.HTTPRequest;
import de.linusdev.lutils.http.HTTPResponse;
import de.linusdev.lutils.http.body.Bodies;
import de.linusdev.lutils.http.body.UnparsedBody;
import de.linusdev.lutils.http.status.StatusCodes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public interface RequestHandler {

    static @NotNull RequestHandler ofHtmlResource(@NotNull Class<?> clazz, @NotNull String name) {
        return request -> HTTPResponse.builder().setStatusCode(StatusCodes.OK).setBody(Bodies.html().ofResource(clazz, name));
    }

    static @NotNull RequestHandler ofCssResource(@NotNull Class<?> clazz, @NotNull String name) {
        return request -> HTTPResponse.builder().setStatusCode(StatusCodes.OK).setBody(Bodies.css().ofResource(clazz, name));
    }

    static @NotNull RequestHandler ofJsResource(@NotNull Class<?> clazz, @NotNull String name) {
        return request -> HTTPResponse.builder().setStatusCode(StatusCodes.OK).setBody(Bodies.javascript().ofResource(clazz, name));
    }

    @Nullable HTTPMessageBuilder handle(@NotNull HTTPRequest<UnparsedBody> request);

}
