package de.linusdev.lutils.routing;

import de.linusdev.lutils.net.http.HTTPMessageBuilder;
import de.linusdev.lutils.net.http.HTTPRequest;
import de.linusdev.lutils.net.http.HTTPResponse;
import de.linusdev.lutils.net.http.body.Bodies;
import de.linusdev.lutils.net.http.body.BodyParsers;
import de.linusdev.lutils.net.http.body.UnparsedBody;
import de.linusdev.lutils.net.http.status.StatusCodes;
import de.linusdev.lutils.net.routing.Routing;
import de.linusdev.lutils.net.routing.builder.RoutingBuilder;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RoutingTest {

    private HTTPRequest<UnparsedBody> parseRequest(@NotNull HTTPMessageBuilder builder) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        builder.buildRequest(out);

        return HTTPRequest.parse(new ByteArrayInputStream(out.toByteArray()), BodyParsers.newUnparsedBodyParser());
    }

    private HTTPResponse<UnparsedBody> parseResponse(@NotNull HTTPMessageBuilder builder) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        builder.buildResponse(out);

        return HTTPResponse.parse(new ByteArrayInputStream(out.toByteArray()), BodyParsers.newUnparsedBodyParser());
    }

    private final static @NotNull Routing routing = new RoutingBuilder()
            .setPrefix("/some/prefix")
            .setExceptionHandler(throwable -> HTTPResponse.builder().setStatusCode(StatusCodes.INTERNAL_SERVER_ERROR).setBody(Bodies.textUtf8().ofStringUtf8(throwable.toString())))
            .route("testRoute")
            .GET(request -> HTTPResponse.builder().setStatusCode(StatusCodes.OK).setBody(Bodies.textUtf8().ofStringUtf8("Yay!")))
            .route("sub")
            .GET(request -> HTTPResponse.builder().setStatusCode(StatusCodes.FORBIDDEN))
            .buildRoute()
            .buildRoute()
            .route("exception")
            .GET(request -> {throw new IllegalStateException("Test");})
            .buildRoute()
            .build();

    @Test
    void testRoute() throws IOException {

        // Test a route
        HTTPRequest<UnparsedBody> request = parseRequest(HTTPRequest.builder().GET("/some/prefix/testRoute"));
        HTTPResponse<UnparsedBody> response = parseResponse(routing.route(request));

        assertEquals(StatusCodes.OK, response.getStatusCode());
        assertEquals("Yay!", response.getBody().parseTo(BodyParsers.newStringBodyParser()));
    }

    @Test
    void testSubRoute() throws IOException {
        // Test a sub route
        var request = parseRequest(HTTPRequest.builder().GET("/some/prefix/testRoute/sub"));
        var response = parseResponse(routing.route(request));

        assertEquals(StatusCodes.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void testSlashAtTheEnd() throws IOException {
        // Test slash at the end
        var request = parseRequest(HTTPRequest.builder().GET("/some/prefix/testRoute/sub/"));
        var response = parseResponse(routing.route(request));

        assertEquals(StatusCodes.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void testNoSlashAtTheStart() throws IOException {
        // Test no slash at the start
        var request = parseRequest(HTTPRequest.builder().GET("some/prefix/testRoute/sub/"));
        var response = parseResponse(routing.route(request));

        assertEquals(StatusCodes.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void testNoSlashAtTheStartAndEnd() throws IOException {
        // Test no slash at the start
        var request = parseRequest(HTTPRequest.builder().GET("some/prefix"));
        var response = parseResponse(routing.route(request));

        assertEquals(StatusCodes.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDefaultRoute() throws IOException {
        // Test default route
        var request = parseRequest(HTTPRequest.builder().GET("/some/prefix/some/route/that/doesnt/exist"));
        var response = parseResponse(routing.route(request));

        assertEquals(StatusCodes.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testWrongPrefix() throws IOException {
        // Test wrong prefix
        var request = parseRequest(HTTPRequest.builder().GET("/wrong/prefix/some/route/that/doesnt/exist"));
        var response = parseResponse(routing.route(request));

        assertEquals(StatusCodes.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testInternalServerError() throws IOException {
        // Test internal server error
        var request = parseRequest(HTTPRequest.builder().GET("/some/prefix/exception"));
        var response = parseResponse(routing.route(request));

        assertEquals(StatusCodes.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("java.lang.IllegalStateException: Test", response.getBody().parseTo(BodyParsers.newStringBodyParser()));
    }

}