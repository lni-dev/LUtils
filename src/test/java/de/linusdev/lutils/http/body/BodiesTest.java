package de.linusdev.lutils.http.body;

import de.linusdev.lutils.http.HTTPResponse;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BodiesTest {

    @Test
    void html() throws IOException {
        String response = HTTPResponse.builder()
                .setBody(Bodies.html().ofResource(BodiesTest.class, "index.html"))
                .buildResponse();

        assertEquals("""
                HTTP/1.1 200 OK
                Content-Length: 312
                Content-Type: text/html
                
                <!--
                  ~ Copyright (c) 2024 Linus Andera all rights reserved
                  -->
                
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                
                    <!-- title -->
                    <title>Test</title>
                    <meta property="og:title" content="Test"/>
                </head>
                <body>
                <div id="container">
                    Test!
                </div>
                </body>
                </html>""".replaceAll("\n", "\r\n"),
                response
        );
    }

    @Test
    void css() throws IOException {
        String response = HTTPResponse.builder()
                .setBody(Bodies.css().ofRegularFile(Paths.get("src/test/resources/de/linusdev/lutils/http/body/index.css")))
                .buildResponse();

        assertEquals("""
                HTTP/1.1 200 OK
                Content-Length: 469
                Content-Type: text/css
                
                :root {
                    --text-color: rgb(230, 237, 243);
                    --background-color: rgb(0,0,0);
                }
                
                * {
                    font-family: 'Roboto M', sans-serif;
                    font-size: 1.05rem;
                    margin: 0;
                    padding: 0;
                    box-sizing: border-box;
                    color: var(--text-color);
                    background: var(--background-color);
                }
                
                #container {
                    display: flex;
                    flex-direction: column;
                }
                
                #log {
                    display: flex;
                    flex-direction: column;
                }
                
                .log-element {
                    padding: 10px;
                }""".replaceAll("\n", "\r\n"),
                response
        );
    }
}