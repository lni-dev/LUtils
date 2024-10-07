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

package de.linusdev.lutils.net.ws;

import de.linusdev.lutils.net.ws.control.CloseFrame;
import de.linusdev.lutils.net.ws.control.writable.WritableCloseFrame;
import de.linusdev.lutils.net.ws.frame.Frame;
import de.linusdev.lutils.net.ws.frame.OpCodes;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
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

}
