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
import de.linusdev.lutils.http.body.BodyParsers;
import de.linusdev.lutils.http.body.UnparsedBody;
import de.linusdev.lutils.http.status.StatusCodes;
import de.linusdev.lutils.routing.builder.RoutingBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.function.Function;

public class Routing extends Route {

    public static @NotNull RoutingBuilder builder() {
        return new RoutingBuilder();
    }

    /**
     * Prefix for this routing. Always starts and ends with a leading and trailing {@code /}.
     */
    private final @NotNull String prefix;
    /**
     * Same as {@link #prefix}, but with no trailing {@code /}.
     */
    private final @NotNull String prefixNoEndSlash;
    /**
     * handler/listener for exceptions during routing.
     */
    private final @NotNull Function<@NotNull Throwable, @Nullable HTTPMessageBuilder> exceptionHandler;

    /**
     *
     * @param prefix see {@link #prefix}
     * @param defaultRoute fallback {@link Route}, must be present.
     * @param routes sub {@link Route}s
     * @param exceptionHandler  see {@link #exceptionHandler}
     */
    public Routing(
            @NotNull String prefix,
            @NotNull Route defaultRoute,
            @NotNull HashMap<String, Route> routes,
            @NotNull Function<@NotNull Throwable, @Nullable HTTPMessageBuilder> exceptionHandler
    ) {
        super(defaultRoute, routes, new HashMap<>(0), null);
        this.prefix = prefix;
        this.prefixNoEndSlash = prefix.substring(0, prefix.length() - 1);
        this.exceptionHandler = exceptionHandler;
    }

    /**
     * Reads a {@link HTTPRequest} from given {@code stream} and {@link #route(HTTPRequest) routes} it.
     */
    public @NotNull HTTPMessageBuilder route(@NotNull InputStream stream) {
        try {
            return route(HTTPRequest.parse(stream, BodyParsers.newUnparsedBodyParser()));
        } catch (Throwable t) {
            var response = exceptionHandler.apply(t);
            return response == null ? HTTPResponse.builder().setStatusCode(StatusCodes.INTERNAL_SERVER_ERROR) : response;
        }
    }

    /**
     * Starts routing for given {@code request}.
     * @param request incoming {@link HTTPRequest}
     * @return {@link HTTPMessageBuilder} ready to {@link HTTPMessageBuilder#buildResponse(OutputStream, int) build the response}.
     */
    public @NotNull HTTPMessageBuilder route(@NotNull HTTPRequest<UnparsedBody> request) {
        String path = request.getPath() == null ? "/" : request.getPath();
        if(!path.startsWith("/")) path = "/" + path;


        if(!(path.startsWith(prefix) || (path.startsWith(prefixNoEndSlash) && path.length() == prefixNoEndSlash.length())))
            return HTTPResponse.builder().setStatusCode(StatusCodes.BAD_REQUEST);

        // Remove the prefix
        path = path.substring(prefixNoEndSlash.length());

        try {
            HTTPMessageBuilder response = accept(new RoutingState(request, path));
            if(response == null) {
                // This should never happen, as the default route of Routing should always send a response!
                return HTTPResponse.builder().setStatusCode(StatusCodes.INTERNAL_SERVER_ERROR);
            }

            return response;
        } catch (Throwable t) {
            var response = exceptionHandler.apply(t);
            return response == null ? HTTPResponse.builder().setStatusCode(StatusCodes.INTERNAL_SERVER_ERROR) : response;
        }

    }
}
