package de.linusdev.lutils.net.ws;

import de.linusdev.lutils.net.routing.Routing;
import de.linusdev.lutils.net.server.SimpleHttpServer;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.WebSocket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
    void test() throws NoSuchAlgorithmException, IOException, URISyntaxException {

        Routing routing = Routing.builder()
                .defaultRoute()
                    .GET(new WebSocketServer(webSocket -> {

                    })).buildRoute()
                .build();

        SimpleHttpServer server = new SimpleHttpServer(8080, routing, Throwable::printStackTrace);

        //new WebSocket.Builder().buildAsync(new URI("ws://localhost:8080/"));

        server.shutdown();

    }
}