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
import de.linusdev.lutils.net.ws.control.WSStatusCodes;
import de.linusdev.lutils.net.ws.control.writable.WritableCloseFrame;
import de.linusdev.lutils.net.ws.frame.Frame;
import de.linusdev.lutils.net.ws.frame.OpCodes;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
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

            listener.onListenerThreadDeath();

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
        default void onListenerThreadDeath() {}
    }

    @SuppressWarnings("unused")
    public abstract static class AdvancedListener implements Listener {

        private final @NotNull WebSocket socket;

        private OpCodes lastOpcode = null;

        private final StringBuilder textMessage = new StringBuilder();
        private ArrayList<byte[]> binaryPayload = null;

        private final AtomicBoolean onClosedCalled = new AtomicBoolean(false);

        protected AdvancedListener(@NotNull WebSocket socket) {
            this.socket = socket;
        }

        protected abstract void onText(@NotNull String text);
        protected abstract void onBinary(@NotNull ArrayList<byte[]> binary);
        protected abstract void onPing(@NotNull Frame pingFrame);
        protected abstract void onPong(@NotNull Frame pongFrame);
        protected abstract void onClosed();

        private void callOnClosed() {
            synchronized (onClosedCalled) {
                if(onClosedCalled.get()) return;
                onClosedCalled.set(true);
            }

            onClosed();
        }

        public void closeWebSocket() {
            try {
                socket.runSynchronisedWritable(() -> {
                    // Send close frame. Socket will be closed when the other end sends a response close frame.
                    socket.writeFrame(new WritableCloseFrame(WSStatusCodes.NORMAL_CLOSURE));
                });
            } catch (IOException e) {
                onError(socket, e);
            }

            // Start a timer in case the other end does not respond and close the socket after 10 seconds
            new Timer(true).schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        socket.close();
                    } catch (IOException ignored) {}
                }
            }, 10_000);

            callOnClosed();
        }

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
            synchronized (onClosedCalled) {
                if(onClosedCalled.get())
                    return;
            }

            switch (frame.opcode()) {
                case TEXT_UTF8 -> handleText(frame);
                case BINARY -> handleBinary(frame);
                case CONTINUATION -> {
                    if(lastOpcode == OpCodes.TEXT_UTF8) handleText(frame);
                    else if (lastOpcode == OpCodes.BINARY) handleBinary(frame);
                    else throw new Error("This cannot happen");
                }
                case PING -> onPing(frame);
                case PONG -> onPong(frame);
                case CLOSE -> throw new Error("This cannot happen"); // Close frame has a separate listener (onClose).
            }
        }

        @Override
        public void onClose(@NotNull WebSocket webSocket, @NotNull CloseFrame frame) throws IOException {
            Listener.super.onClose(webSocket, frame);
            callOnClosed();
        }

        @Override
        public void onListenerThreadDeath() {
            callOnClosed();
        }
    }

}
