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
import de.linusdev.lutils.net.http.body.BodyParsers;
import de.linusdev.lutils.net.http.body.UnparsedBody;
import de.linusdev.lutils.net.http.status.StatusCodes;
import de.linusdev.lutils.net.routing.builder.RoutingBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
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
     * Parses the input stream of given {@code socket} to a {@link HTTPRequest} and routes it.
     * It will then automatically send the by the routing selected {@link HTTPResponse} to
     * the output stream of given {@code socket}. Also closes the socket if required.
     * @param socket socket to route
     * @throws IOException while writing to or reading from the sockets streams.
     */
    public void route(@NotNull Socket socket) throws IOException {
        HTTPMessageBuilder response = route(socket, socket.getInputStream());
        if(response != null) {
            response.buildResponse(socket.getOutputStream());
            socket.close();
        }

    }

    /**
     * Starts routing for given {@code request}.
     * @param request incoming {@link HTTPRequest}
     * @return {@link HTTPMessageBuilder} ready to {@link HTTPMessageBuilder#buildResponse(OutputStream, int) build the response}.
     */
    public @Nullable HTTPMessageBuilder route(@NotNull HTTPRequest<UnparsedBody> request) {
        return route(null, request);
    }

    /**
     * Reads a {@link HTTPRequest} from given {@code stream} and {@link #route(HTTPRequest) routes} it.
     */
    private @Nullable HTTPMessageBuilder route(@Nullable Socket socket, @NotNull InputStream stream) {
        try {
            return route(socket, HTTPRequest.parse(stream, BodyParsers.newUnparsedBodyParser()));
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
    private @Nullable HTTPMessageBuilder route(
            @Nullable Socket socket,
            @NotNull HTTPRequest<UnparsedBody> request
    ) {
        String path = request.getPathAndQuery() == null ? "/" : request.getPathAndQuery().getPath();
        if(!path.startsWith("/")) path = "/" + path;


        if(!(path.startsWith(prefix) || (path.startsWith(prefixNoEndSlash) && path.length() == prefixNoEndSlash.length())))
            return HTTPResponse.builder().setStatusCode(StatusCodes.BAD_REQUEST);

        // Remove the prefix
        path = path.substring(prefixNoEndSlash.length());

        try {
            RoutingState state = new RoutingState(socket, request, path);
            HTTPMessageBuilder response = accept(state);
            if(response == null && !state.isHandled()) {
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
