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

import de.linusdev.lutils.http.HTTPRequest;
import de.linusdev.lutils.http.body.UnparsedBody;
import de.linusdev.lutils.http.method.RequestMethod;
import org.jetbrains.annotations.NotNull;

public class RoutingState {

    private final @NotNull HTTPRequest<UnparsedBody> request;
    private final @NotNull String[] path;

    private int pathIndex = 0;

    public RoutingState(
            @NotNull HTTPRequest<UnparsedBody> request,
            @NotNull String path
    ) {
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
}
