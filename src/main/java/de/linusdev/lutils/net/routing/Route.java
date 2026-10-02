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
import de.linusdev.lutils.net.http.method.RequestMethod;
import de.linusdev.lutils.result.TriResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Route {

    /**
     * Sub routes.
     */
    protected final @NotNull HashMap<String, Route> routes;

    /**
     * Path param routes. First string is the regex and second the parameter name.
     * If the regex matches the hole path part, the path param is selected and other path params will not be checked.
     * Path param routes are evaluated after {@link #routes normal routes}. If a normal route is selected first
     * path param routes are ignored.
     */
    private final @NotNull List<TriResult<@Nullable String, @NotNull String, @NotNull Route>> pathParams;

    /**
     * Default route. Used if no route in {@link #routes} or {@link #pathParams path params} matches an incoming route.
     * If no default route is set, it will fall back to the parents default route.
     */
    protected final @Nullable Route defaultRoute;

    /**
     * Handlers for requests with on this route. Each handler is for a specific {@link RequestMethod}.
     */
    protected final @NotNull Map<RequestMethod, RequestHandler> handlers;
    /**
     * Default handler, used if no handler in {@link #handlers} matches an incoming {@link RequestMethod}.
     * If no default handler is set, it will fall back to the parents default route.
     */
    private final @Nullable RequestHandler defaultHandler;

    /**
     * Create a route.
     *
     * @param defaultRoute   see {@link #defaultRoute}
     * @param routes         see {@link #routes}
     * @param pathParams     see {@link #pathParams}
     * @param handlers       see {@link #handlers}
     * @param defaultHandler see {@link #defaultHandler}
     */
    public Route(
            @Nullable Route defaultRoute,
            @NotNull HashMap<String, Route> routes,
            @NotNull List<TriResult<@Nullable String, @NotNull String, @NotNull Route>> pathParams,
            @NotNull Map<RequestMethod, RequestHandler> handlers,
            @Nullable RequestHandler defaultHandler
    ) {
        this.defaultRoute = defaultRoute;
        this.routes = routes;
        this.pathParams = pathParams;
        this.handlers = handlers;
        this.defaultHandler = defaultHandler;
    }

    /**
     * Create a route
     * @param defaultRouteIsSelf {@code true} will set this routes {@link #defaultRoute} to itself.
     *                                      Useful to make any sub routes root to this route.
     * @param routes see {@link #routes}
     * @param pathParams     see {@link #pathParams}
     * @param handlers see {@link #handlers}
     * @param defaultHandler see {@link #defaultHandler}
     */
    public Route(
            boolean defaultRouteIsSelf,
            @NotNull HashMap<String, Route> routes,
            @NotNull List<TriResult<@Nullable String, @NotNull String, @NotNull Route>> pathParams,
            @NotNull Map<RequestMethod, RequestHandler> handlers,
            @Nullable RequestHandler defaultHandler
    ) {
        this.defaultRoute = defaultRouteIsSelf ? this : null;
        this.routes = routes;
        this.pathParams = pathParams;
        this.handlers = handlers;
        this.defaultHandler = defaultHandler;
    }

    /**
     * Get responds to given {@code RoutingState} if possible.
     */
    public @Nullable HTTPMessageBuilder accept(@NotNull RoutingState state) throws IOException {
        if(state.isHandled()) return null;
        if(state.isAnotherPathPartAvailable()) {
            // There is another part, lets route...
            return fallBackToDefaultRouteIfNull(route(state), state);
        }

        // Check if we have a handler for this request.
        RequestHandler handler = handlers.get(state.getMethod());

        if(handler == null)
            return fallBackToDefaultHandler(state);

        HTTPMessageBuilder response = handler.handle(state);

        if(response == null)
            return fallBackToDefaultHandler(state);

        return response;
    }

    /**
     * Route to default route if {@code response} is {@code null}
     * @return given {@code response} if it is not null, {@code null} if routing with default route was not possible
     * or the response of the {@link #defaultRoute}.
     */
    private @Nullable HTTPMessageBuilder fallBackToDefaultRouteIfNull(
            @Nullable HTTPMessageBuilder response,
            @NotNull RoutingState state
    ) throws IOException {
        if(response == null) {
            // Can route it, maybe we have a default route
            if(defaultRoute == null)
                return null; // no default let the parent use its default route
            return defaultRoute.accept(state); // Let's try the default route!
        }
        return response;
    }

    /**
     * Handle with default handler if possible.
     * @return {@code null} if no {@link #defaultHandler} is present.
     */
    private @Nullable HTTPMessageBuilder fallBackToDefaultHandler(
            @NotNull RoutingState state
    ) throws IOException {
        if(defaultHandler == null)
            return null;
        return defaultHandler.handle(state);
    }

    /**
     * Route to sub routes if possible.
     * @return {@code null} if routing was not possible.
     */
    private @Nullable HTTPMessageBuilder route(@NotNull RoutingState state) throws IOException {
        String nextPathPart = state.getNextPathPart();

        Route route = routes.get(nextPathPart);
        if(route != null)
            return route.accept(state);

        for (TriResult<@Nullable String, @NotNull String, @NotNull Route> pathParam : pathParams) {
            @Nullable String regex = pathParam.result1();
            @NotNull String name = pathParam.result2();
            @NotNull Route paramRoute = pathParam.result3();
            @Nullable Matcher matcher = null;

            if(regex != null)
                matcher = Pattern.compile(regex).matcher(nextPathPart);

            if(regex == null || matcher.matches())  {
                state.setPathParam(name, nextPathPart);
                return paramRoute.accept(state);
            }
        }

        return null;
    }

}
