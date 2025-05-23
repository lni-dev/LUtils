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
import de.linusdev.lutils.net.http.body.UnparsedBody;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public interface RoutingStateHandler extends RequestHandler {

    @Override
    default @Nullable HTTPMessageBuilder handle(@NotNull HTTPRequest<UnparsedBody> request) {
        throw new UnsupportedOperationException("call handle(RoutingState) instead.");
    }

    @Override
    @Nullable
    HTTPMessageBuilder handle(@NotNull RoutingState state) throws IOException;
}
