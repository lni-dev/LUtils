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

import de.linusdev.lutils.net.http.method.Methods;
import de.linusdev.lutils.net.http.method.RequestMethod;
import de.linusdev.lutils.net.routing.RequestHandler;
import de.linusdev.lutils.net.routing.Route;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("JavadocReference")
public class RouteBuilder<PARENT> implements RouteBuilderParent {

    private final @NotNull HashMap<RequestMethod, RequestHandler> handlers = new HashMap<>();
    private @Nullable RequestHandler defaultHandler;
    private final @NotNull HashMap<String, RouteBuilder<?>> routes = new HashMap<>();
    private @Nullable RouteBuilder<RouteBuilder<PARENT>> defaultRoute;
    private boolean defaultRouteIsSelf = false;

    private final @NotNull RoutingBuilder routingBuilder;
    private final @NotNull PARENT parent;


    public RouteBuilder(
            @NotNull RoutingBuilder routingBuilder,
            @NotNull PARENT parent
    ) {
        this.routingBuilder = routingBuilder;
        this.parent = parent;
    }

    /**
     * Add a handler for {@link Methods#GET GET}.
     */
    public RouteBuilder<PARENT> GET(@NotNull RequestHandler handler) {
        handlers.put(Methods.GET, handler);
        return this;
    }

    /**
     * Set this routes {@link Route#defaultHandler}.
     */
    public RouteBuilder<PARENT> defaultHandler(@Nullable RequestHandler defaultHandler) {
        this.defaultHandler = defaultHandler;
        return this;
    }

    /**
     * Add a sub route with given {@code path}.
     * @throws IllegalArgumentException if {@code path} is empty.
     */
    public @NotNull RouteBuilder<RouteBuilder<PARENT>> route(@NotNull String path) {
        if(path.isEmpty())
            throw new IllegalArgumentException("Path cannot be empty.");
        RouteBuilder<RouteBuilder<PARENT>> builder = new RouteBuilder<>(routingBuilder, this);
        routes.put(path, builder);
        return builder;
    }

    /**
     * Add a {@link Route#defaultRoute} to this route.
     */
    public @NotNull RouteBuilder<RouteBuilder<PARENT>> defaultRoute() {
        defaultRoute = new RouteBuilder<>(routingBuilder, this);
        defaultRoute.setDefaultRouteIsSelf(true);
        return defaultRoute;
    }

    @SuppressWarnings("SameParameterValue")
    void setDefaultRouteIsSelf(boolean defaultRouteIsSelf) {
        this.defaultRouteIsSelf = defaultRouteIsSelf;
    }

    /**
     * Get the {@link Route} from this builder.
     */
    @NotNull Route getRoute() {
        HashMap<String, Route> routes = new HashMap<>(this.routes.size());
        for (Map.Entry<String, RouteBuilder<?>> route : this.routes.entrySet()) {
            routes.put(route.getKey(), route.getValue().getRoute());
        }
        if(defaultRouteIsSelf)
            return new Route(true, routes, handlers, defaultHandler);
        return new Route(defaultRoute == null ? null : defaultRoute.getRoute(), routes, handlers, defaultHandler);
    }

    public PARENT buildRoute() {
        return parent;
    }


}
