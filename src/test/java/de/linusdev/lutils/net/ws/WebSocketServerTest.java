package de.linusdev.lutils.net.ws;

import org.junit.jupiter.api.Test;

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
}