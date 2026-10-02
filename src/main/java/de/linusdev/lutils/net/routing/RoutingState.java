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

import de.linusdev.lutils.collections.BiIterable;
import de.linusdev.lutils.collections.MapView;
import de.linusdev.lutils.net.http.HTTPRequest;
import de.linusdev.lutils.net.http.body.UnparsedBody;
import de.linusdev.lutils.net.http.method.RequestMethod;
import de.linusdev.lutils.other.iterator.IteratorUtils;
import de.linusdev.lutils.result.BiResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RoutingState {

    private final @Nullable Socket socket;
    private final @NotNull HTTPRequest<UnparsedBody> request;
    private final @NotNull String[] path;
    private final @NotNull ArrayList<BiResult<String, String>> pathParams = new ArrayList<>(1);

    private int pathIndex = 0;
    private boolean handled = false;

    public RoutingState(
            @Nullable Socket socket,
            @NotNull HTTPRequest<UnparsedBody> request,
            @NotNull String path
    ) {
        this.socket = socket;
        this.request = request;

        if(path.startsWith("/"))
            path = path.substring(1);

        if(path.endsWith("/"))
            path = path.substring(0, path.length() - 1);

        this.path = path.split("/");
    }

    public boolean isAnotherPathPartAvailable() {
        return pathIndex < path.length;
    }

    public @NotNull String getNextPathPart() {
        return path[pathIndex++];
    }

    /**
     * The {@link HTTPRequest} being routed.
     */
    public @NotNull HTTPRequest<UnparsedBody> getRequest() {
        return request;
    }

    /**
     * The {@link RequestMethod} of the {@link HTTPRequest} being routed.
     */
    public @NotNull RequestMethod getMethod() {
        return request.getMethod();
    }

    /**
     * The {@link Socket} of the {@link HTTPRequest} being routed.
     */
    public @Nullable Socket getSocket() {
        return socket;
    }

    /**
     * Set a path param.
     * @param key path param key (name)
     * @param value path param value
     */
    public void setPathParam(@NotNull String key, @NotNull String value) {
        pathParams.add(new BiResult<>(key, value));
    }

    /**
     * Get a map of all path params.
     */
    public @NotNull MapView<String, String> getPathParams() {
        return new MapView<>(
                pathParams::size,
                key -> {
                    for (BiResult<String, String> pathParam : pathParams) {
                        if (pathParam.result1().equals(key))
                            return pathParam.result2();
                    }
                    return null;
                },
                BiIterable.of(pathParams, BiResult::result1, BiResult::result2)
        );
    }

    public @Nullable String getFirstPathParam() {
        return pathParams.isEmpty() ? null : pathParams.getFirst().result2();
    }

    /**
     * Marks this routing state as handled. This is useful, if the protocol was upgraded
     * to a different one and the socket should not be closed.
     */
    public void handled() {
        this.handled = true;
    }

    /**
     * Whether this routing state is already handled or not.
     */
    public boolean isHandled() {
        return handled;
    }
}
