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

package de.linusdev.lutils.net.ws;

import de.linusdev.lutils.interfaces.TConsumer;
import de.linusdev.lutils.net.http.HTTPMessageBuilder;
import de.linusdev.lutils.net.http.HTTPRequest;
import de.linusdev.lutils.net.http.HTTPResponse;
import de.linusdev.lutils.net.http.body.Bodies;
import de.linusdev.lutils.net.http.body.UnparsedBody;
import de.linusdev.lutils.net.http.header.Header;
import de.linusdev.lutils.net.http.header.HeaderMap;
import de.linusdev.lutils.net.http.header.HeaderNames;
import de.linusdev.lutils.net.http.header.value.BasicHeaderValue;
import de.linusdev.lutils.net.http.header.value.IntegerHeaderValue;
import de.linusdev.lutils.net.http.method.Methods;
import de.linusdev.lutils.net.http.method.RequestMethod;
import de.linusdev.lutils.net.http.status.StatusCodes;
import de.linusdev.lutils.net.routing.RoutingState;
import de.linusdev.lutils.net.routing.RoutingStateHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class WebSocketServer implements RoutingStateHandler {

    public static final int SUPPORTED_WEBSOCKET_VERSION = 13;
    public static final @NotNull String MAGIC_STRING = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";

    protected static @NotNull String calculateResponseKey(
            @NotNull String key, @NotNull MessageDigest hashAlgo
    ) {
        String responseKey = key + MAGIC_STRING;
        byte[] hashed = hashAlgo.digest(responseKey.getBytes(StandardCharsets.ISO_8859_1));
        return Base64.getEncoder().encodeToString(hashed);
    }

    private final @NotNull MessageDigest hashAlgo = MessageDigest.getInstance("SHA-1");

    private final @NotNull TConsumer<WebSocket, IOException> createdWebsocketConsumer;

    public WebSocketServer(@NotNull TConsumer<WebSocket, IOException> createdWebsocketConsumer) throws NoSuchAlgorithmException {
        this.createdWebsocketConsumer = createdWebsocketConsumer;
    }

    @Override
    public @Nullable HTTPMessageBuilder handle(@NotNull RoutingState state) throws IOException {
        HTTPRequest<UnparsedBody> request = state.getRequest();
        HeaderMap headers = request.getHeaders();

        if(!RequestMethod.equals(Methods.GET, request.getMethod()))
            return HTTPResponse.builder().setStatusCode(StatusCodes.BAD_REQUEST);

        Header upgradeHeader = headers.get(HeaderNames.UPGRADE);
        if(upgradeHeader == null || !upgradeHeader.getValue().equals("websocket"))
            return HTTPResponse.builder()
                    .setStatusCode(StatusCodes.BAD_REQUEST)
                    .setBody(Bodies.textUtf8().ofStringUtf8("Missing or wrong '" + HeaderNames.UPGRADE + "' header."));

        Header connHeader = headers.get(HeaderNames.CONNECTION);
        if(connHeader == null || !connHeader.parseValue(BasicHeaderValue.PARSER).contains("Upgrade"))
            return HTTPResponse.builder()
                    .setStatusCode(StatusCodes.BAD_REQUEST)
                    .setBody(Bodies.textUtf8().ofStringUtf8("Missing or wrong '" + HeaderNames.CONNECTION + "' header."));

        Header key = headers.get(HeaderNames.SEC_WEBSOCKET_KEY);
        var version = headers.get(HeaderNames.SEC_WEBSOCKET_VERSION).parseValue(IntegerHeaderValue.PARSER);

        if(version != SUPPORTED_WEBSOCKET_VERSION)
            return HTTPResponse.builder()
                    .setStatusCode(StatusCodes.BAD_REQUEST)
                    .setHeader(HeaderNames.SEC_WEBSOCKET_VERSION, "" + SUPPORTED_WEBSOCKET_VERSION);

        String hashedKey = calculateResponseKey(key.getValue(), hashAlgo);

        Socket socket = state.getSocket();

        if(socket == null) {
            throw new IllegalStateException("A socket is required to create a web socket");
        }

        HTTPResponse.builder()
                .setStatusCode(StatusCodes.SWITCHING_PROTOCOLS)
                .setHeader(HeaderNames.SEC_WEBSOCKET_ACCEPT, hashedKey)
                .setHeader(HeaderNames.UPGRADE, "websocket")
                .setHeader(HeaderNames.CONNECTION, "Upgrade")
                .buildResponse(socket.getOutputStream());

        WebSocket webSocket = new WebSocket(socket, false, false);
        createdWebsocketConsumer.consume(webSocket);
        state.handled();
        return null;
    }
}
