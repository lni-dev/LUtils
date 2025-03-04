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

package de.linusdev.lutils.net.server;

import de.linusdev.lutils.async.Future;
import de.linusdev.lutils.async.Nothing;
import de.linusdev.lutils.async.Task;
import de.linusdev.lutils.async.completeable.CompletableFuture;
import de.linusdev.lutils.async.completeable.CompletableTask;
import de.linusdev.lutils.async.exception.NonBlockingThreadException;
import de.linusdev.lutils.async.manager.AsyncManager;
import de.linusdev.lutils.interfaces.ExceptionHandler;
import de.linusdev.lutils.net.routing.Routing;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.URI;
import java.net.URISyntaxException;

public class SimpleHttpServer implements AsyncManager {

    private final @NotNull ServerSocket serverSocket;
    private final ExceptionHandler exceptionHandler;
    /**
     * This future will be completed before the server is closed.
     */
    private final @NotNull CompletableFuture<Nothing, SimpleHttpServer, CompletableTask<Nothing, SimpleHttpServer>> closeFuture;

    private volatile boolean keepAlive = true;


    public SimpleHttpServer(
            int port,
            @NotNull Routing routing,
            @NotNull ExceptionHandler exceptionHandler
    ) throws IOException {
        this.serverSocket = new ServerSocket(port, 3);
        this.exceptionHandler = exceptionHandler;
        this.closeFuture = CompletableFuture.create(this, false);

        Thread thread = new Thread(() -> {
            while (keepAlive) {
                try {
                    routing.route(serverSocket.accept());
                } catch (Throwable e) {
                    exceptionHandler.accept(e);
                }
            }
        },"simple-http-server");
        thread.setDaemon(true);
        thread.start();
    }

    @SuppressWarnings("unused")
    public @NotNull Future<Nothing, SimpleHttpServer> getCloseFuture() {
        return closeFuture;
    }

    public void shutdown() {
        try {
            closeFuture.complete(Nothing.INSTANCE, this, null);
            keepAlive = false;
            serverSocket.close();
        } catch (Throwable e) {
            exceptionHandler.accept(e);
        }
    }

    @SuppressWarnings("unused")
    public void print() {
        System.out.println("SimpleHttpServer running on: http://localhost:" + serverSocket.getLocalPort());
    }

    @SuppressWarnings("unused")
    public void openInBrowser(@NotNull String path) {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                Desktop.getDesktop().browse(new URI("http://localhost:" + serverSocket.getLocalPort() + "/" + path));
            } catch (IOException | URISyntaxException ignored) {}
        }
    }

    @Override
    public void checkThread() throws NonBlockingThreadException {

    }

    @Override
    public void onExceptionInListener(@NotNull Future<?, ?> future, @Nullable Task<?, ?> task, @NotNull Throwable throwable) {
        exceptionHandler.accept(throwable);
    }
}
