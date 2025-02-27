package de.linusdev.lutils.net.ws;

import de.linusdev.lutils.net.routing.Routing;
import de.linusdev.lutils.net.server.SimpleHttpServer;
import de.linusdev.lutils.net.ws.control.CloseFrame;
import de.linusdev.lutils.net.ws.frame.Frame;
import de.linusdev.lutils.net.ws.frame.OpCodes;
import de.linusdev.lutils.net.ws.frames.writable.WritableTextFrame;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class WebSocketServerTest {

    @Test
    void calculateResponseKey() throws NoSuchAlgorithmException {

        MessageDigest digest = MessageDigest.getInstance("SHA-1");

        assertEquals(
                "s3pPLMBiTxaQ9kYGzzhZRbK+xOo=",
                WebSocketServer.calculateResponseKey("dGhlIHNhbXBsZSBub25jZQ==", digest)
        );


    }

    @Test
    void opcodes() {
        assertTrue(OpCodes.CLOSE.isControlOpCode());
        assertTrue(OpCodes.PING.isControlOpCode());
        assertTrue(OpCodes.PONG.isControlOpCode());
        assertFalse(OpCodes.TEXT_UTF8.isControlOpCode());
        assertFalse(OpCodes.BINARY.isControlOpCode());
    }

    @Test
    void testWebSocketServer() throws NoSuchAlgorithmException, IOException, URISyntaxException, InterruptedException, ExecutionException {

        List<String> serverReceived = new ArrayList<>();
        List<String> clientReceived = new ArrayList<>();
        AtomicReference<byte[]> serverReceivedNonText = new AtomicReference<>(null);

        AtomicInteger serverReceivedPings = new AtomicInteger(0);
        AtomicInteger clientReceivedPongs = new AtomicInteger(0);

        Routing routing = Routing.builder()
                .defaultRoute()
                    .GET(new WebSocketServer(webSocket -> {
                        webSocket.createListener(new WebSocketListener.Listener() {
                            @Override
                            public void onReceived(de.linusdev.lutils.net.ws.@NotNull WebSocket webSocket, @NotNull Frame frame) throws IOException {
                                assertTrue(frame.wasMasked());

                                assertFalse(frame.getReserved1());
                                assertFalse(frame.getReserved2());
                                assertFalse(frame.getReserved3());

                                if(frame.opcode() == OpCodes.TEXT_UTF8) {
                                    String received = frame.toTextFrame().getText();
                                    System.out.println("Server received text frame: " + received);
                                    serverReceived.add(received);

                                } else if (frame.opcode() == OpCodes.PING) {
                                    serverReceivedPings.incrementAndGet();
                                    webSocket.sendPong();
                                } else {
                                    System.out.println("Server received non text frame: " + Arrays.toString(frame.getPayload()));
                                    serverReceivedNonText.set(frame.getPayload());
                                }
                            }

                            @Override
                            public void onError(de.linusdev.lutils.net.ws.@NotNull WebSocket webSocket, @NotNull Throwable error) {
                                error.printStackTrace();
                                fail();
                            }

                            @Override
                            public void onClose(de.linusdev.lutils.net.ws.@NotNull WebSocket webSocket, @NotNull CloseFrame frame) throws IOException {
                                System.out.println("Close!");

                                assertEquals(1000, frame.statusCode().code());
                                assertEquals("Noooooo!", frame.reason());

                                WebSocketListener.Listener.super.onClose(webSocket, frame);
                            }
                        });

                        System.out.println("Websocket created");
                        try {
                            System.out.println("server sending 'test2'");
                            webSocket.writeFrame(new WritableTextFrame("test2"));
                            System.out.println("server sending 'test3'");
                            webSocket.writeFrame(new WritableTextFrame("test3"));
                        } catch (Throwable e) {
                            e.printStackTrace();
                            fail();
                        }

                    })).buildRoute()
                .build();

        int port = 8081;
        SimpleHttpServer server = new SimpleHttpServer(port, routing, Throwable::printStackTrace);


        HttpClient client = HttpClient.newHttpClient();
        var ws = client.newWebSocketBuilder().buildAsync(new URI("ws://localhost:" + port + "/"), new WebSocket.Listener() {
            @Override
            public void onOpen(WebSocket webSocket) {
                System.out.println("Client sending 'test'");
                webSocket.sendPing(ByteBuffer.wrap(new byte[0]));
                webSocket.sendText("test", true);
                webSocket.sendBinary(ByteBuffer.wrap(new byte[]{123, 33}), true);
                webSocket.request(1);
            }

            @Override
            public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
                System.out.println("Client received text: " + data);
                clientReceived.add(data.toString());
                webSocket.request(1);
                return null;
            }

            @Override
            public CompletionStage<?> onBinary(WebSocket webSocket, ByteBuffer data, boolean last) {
                System.out.println("Client received binary: " + data);
                webSocket.request(1);
                return null;
            }

            @Override
            public CompletionStage<?> onPing(WebSocket webSocket, ByteBuffer message) {
                System.out.println("Client received ping: " + message);
                webSocket.request(1);
                return null;
            }

            @Override
            public CompletionStage<?> onPong(WebSocket webSocket, ByteBuffer message) {
                System.out.println("Client received pong: " + message);
                clientReceivedPongs.incrementAndGet();
                webSocket.request(1);
                return null;
            }

            @Override
            public void onError(WebSocket webSocket, Throwable error) {
                error.printStackTrace();
                webSocket.request(1);
            }
        }).get();

        Thread.sleep(1000);

        ws.sendText("Another Test", true);
        ws.sendClose(1000, "Noooooo!");

        Thread.sleep(1000);

        server.shutdown();
        server.getCloseFuture().get();

        assertArrayEquals(serverReceived.toArray(), new String[] {"test", "Another Test"});
        assertEquals(1, serverReceivedPings.get());
        assertArrayEquals(serverReceivedNonText.get(), new byte[]{123, 33});

        assertArrayEquals(clientReceived.toArray(),  new String[] {"test2", "test3"});
        assertEquals(1, clientReceivedPongs.get());

    }
}