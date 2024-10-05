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

package de.linusdev.lutils.net.server;

import de.linusdev.lutils.net.routing.Routing;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.function.Consumer;

public class SimpleHttpServer {

    private final @NotNull ServerSocket serverSocket;
    private final @NotNull SvThread thread;

    private final @NotNull Routing routing;
    private final @NotNull Consumer<Throwable> exceptionHandler;

    private volatile boolean keepAlive = true;

    public SimpleHttpServer(
            int port,
            @NotNull Routing routing,
            @NotNull Consumer<Throwable> exceptionHandler
    ) throws IOException {
        this.serverSocket = new ServerSocket(port, 3);
        this.routing = routing;
        this.exceptionHandler = exceptionHandler;

        this.thread = new SvThread();
        this.thread.start();
    }

    public void shutdown() {
        keepAlive = false;
    }

    private class SvThread extends Thread {

        public SvThread() {
            super("simple-http-server");
            setDaemon(true);
        }

        @Override
        public void run() {

            while (keepAlive) {
                try {
                    routing.route(serverSocket.accept());
                } catch (IOException e) {
                    exceptionHandler.accept(e);
                }
            }
        }
    }
}
