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

package de.linusdev.lutils.net.routing.builder;

import de.linusdev.lutils.net.http.HTTPMessageBuilder;
import de.linusdev.lutils.net.http.HTTPResponse;
import de.linusdev.lutils.net.http.status.StatusCodes;
import de.linusdev.lutils.net.routing.Route;
import de.linusdev.lutils.net.routing.Routing;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@SuppressWarnings("JavadocReference")
public class RoutingBuilder {

    private final @NotNull HashMap<String, RouteBuilder<?>> routes = new HashMap<>();
    private @NotNull RouteBuilder<RoutingBuilder> defaultRoute;
    private @NotNull String prefix = "/";
    private @NotNull Function<@NotNull Throwable, @Nullable HTTPMessageBuilder> exceptionHandler = t -> null;

    public RoutingBuilder() {
        defaultRoute = defaultRoute().defaultHandler(request ->
                HTTPResponse.builder().setStatusCode(StatusCodes.NOT_FOUND));
    }

    /**
     * Add a sub route.<br>
     * If a route with the path {@code ""} is added, it will be used, if a request with its path being the
     * {@link #setPrefix(String) prefix} is routed.
     */
    public @NotNull RouteBuilder<RoutingBuilder> route(@NotNull String path) {
        RouteBuilder<RoutingBuilder> builder = new RouteBuilder<>(this, this);
        routes.put(path, builder);
        return builder;
    }

    /**
     * Build the default fallback route.
     */
    public @NotNull RouteBuilder<RoutingBuilder> defaultRoute() {
        defaultRoute = new RouteBuilder<>(this, this);
        defaultRoute.setDefaultRouteIsSelf(true);
        return defaultRoute;
    }

    /**
     * Set path prefix that every route must have.
     * Leading and trailing {@code /} will automatically be added if not present in given {@code prefix}.
     */
    public RoutingBuilder setPrefix(@NotNull String prefix) {
        if(!prefix.startsWith("/")) prefix = "/" + prefix;
        if(!prefix.endsWith("/")) prefix = prefix + "/";

        this.prefix = prefix;
        return this;
    }

    /**
     * Set {@link Routing#exceptionHandler exceptionHandler}.
     */
    public RoutingBuilder setExceptionHandler(@NotNull Function<@NotNull Throwable, @Nullable HTTPMessageBuilder> exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
        return this;
    }

    /**
     * Build the {@link Routing}.
     */
    public @NotNull Routing build() {
        HashMap<String, Route> routes = new HashMap<>(this.routes.size());
        for (Map.Entry<String, RouteBuilder<?>> route : this.routes.entrySet()) {
            routes.put(route.getKey(), route.getValue().getRoute());
        }

        return new Routing(prefix, defaultRoute.getRoute(), routes, exceptionHandler);
    }

}
