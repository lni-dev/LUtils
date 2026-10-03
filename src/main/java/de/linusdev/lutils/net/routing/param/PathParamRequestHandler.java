package de.linusdev.lutils.net.routing.param;

import de.linusdev.lutils.net.http.HTTPMessageBuilder;
import de.linusdev.lutils.net.http.HTTPRequest;
import de.linusdev.lutils.net.http.body.UnparsedBody;
import de.linusdev.lutils.net.routing.RequestHandler;
import de.linusdev.lutils.net.routing.RoutingState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public interface PathParamRequestHandler extends RequestHandler {
   @Nullable HTTPMessageBuilder handle(@Nullable HTTPRequest<UnparsedBody> request, @NotNull String param);

    @Override
    default @Nullable HTTPMessageBuilder handle(@NotNull HTTPRequest<UnparsedBody> request) {
        throw new UnsupportedOperationException("call handle(RoutingState) instead.");
    }

    @Override
    @Nullable
    default HTTPMessageBuilder handle(@NotNull RoutingState state) throws IOException {
        @Nullable String param = state.getFirstPathParam();

        if(param == null)
            throw new IllegalStateException("Missing path param.");

        return handle(state.getRequest(), param);
    }
}
