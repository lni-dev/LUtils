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

import de.linusdev.lutils.net.ws.control.CloseFrame;
import de.linusdev.lutils.net.ws.control.writable.WritableCloseFrame;
import de.linusdev.lutils.net.ws.frame.Frame;
import de.linusdev.lutils.net.ws.frame.OpCodes;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class WebSocketListener {

    private static final @NotNull AtomicInteger ID_SUPPLIER = new AtomicInteger(0);

    private final int id;

    public WebSocketListener(
            @NotNull WebSocket webSocket,
            @NotNull Listener listener
    ) {
        this.id = ID_SUPPLIER.incrementAndGet();
        new Thread(() -> {
            while (!webSocket.isClosed()) {
                try {
                    Frame frame = webSocket.readFrame();

                    if(frame.opcode() == OpCodes.CLOSE)
                        listener.onClose(webSocket, frame.toCloseFrame());
                    else
                        listener.onReceived(webSocket, frame);


                } catch (IOException e) {

                    if(webSocket.isClosed())
                        break;

                    listener.onError(webSocket, e);
                }
            }

        },"web-socket-listener-" + id).start();
    }

    @SuppressWarnings("unused")
    public int getId() {
        return id;
    }

    public interface Listener {
        void onReceived(@NotNull WebSocket webSocket, @NotNull Frame frame) throws IOException;
        void onError(@NotNull WebSocket webSocket, @NotNull Throwable error);
        default void onClose(@NotNull WebSocket webSocket, @NotNull CloseFrame frame) throws IOException {
            webSocket.runSynchronisedWritable(() -> {
                webSocket.writeFrame(new WritableCloseFrame(frame.statusCode()));
                webSocket.close();
            });
        }
    }

    @SuppressWarnings("unused")
    public abstract static class AdvancedListener implements Listener {

        private OpCodes lastOpcode = null;

        private final StringBuilder textMessage = new StringBuilder();
        private ArrayList<byte[]> binaryPayload = null;

        abstract void onText(@NotNull String text);
        abstract void onBinary(@NotNull ArrayList<byte[]> binary);
        abstract void onPing(@NotNull Frame pingFrame);
        abstract void onPong(@NotNull Frame pongFrame);

        private void handleText(@NotNull Frame frame) {
            assert frame.opcode() == OpCodes.TEXT_UTF8;
            textMessage.append(frame.toTextFrame().getText());

            if(frame.isFinal()) {
                onText(textMessage.toString());
                textMessage.setLength(0);
            } else {
                lastOpcode = frame.opcode();
            }
        }

        private void handleBinary(@NotNull Frame frame) {
            assert frame.opcode() == OpCodes.BINARY;

            if(binaryPayload == null)
                binaryPayload = new ArrayList<>();

            binaryPayload.add(frame.getPayload());

            if(frame.isFinal()) {
                onBinary(binaryPayload);
                binaryPayload = null;
            } else {
                lastOpcode = frame.opcode();
            }
        }

        @Override
        public void onReceived(@NotNull WebSocket webSocket, @NotNull Frame frame) {
            switch (frame.opcode()) {
                case CONTINUATION -> {
                    if(lastOpcode == OpCodes.TEXT_UTF8) handleText(frame);
                    else if (lastOpcode == OpCodes.BINARY) handleBinary(frame);
                    else throw new Error("This cannot happen");
                }
                case TEXT_UTF8 -> handleText(frame);
                case BINARY -> handleBinary(frame);
                case CLOSE -> throw new Error("This cannot happen");
                case PING -> onPing(frame);
                case PONG -> onPong(frame);
            }
        }

    }

}
