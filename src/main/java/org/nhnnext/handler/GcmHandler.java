package org.nhnnext.handler;

import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;
import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.http.HttpServerRequest;

import org.vertx.java.core.http.HttpServerResponse;
import org.vertx.java.core.logging.Logger;

import java.io.IOException;
import java.util.concurrent.Future;

public class GcmHandler implements Handler<HttpServerRequest> {
    private static final String GCM_URL = "https://android.googleapis.com/gcm/send";
    private static final String AUTH_KEY = "key=AIzaSyCSXLo9AP5tFKGGGSZ7GXst8_PWCKlKN_k";

    private final Logger logger;

    public GcmHandler(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void handle(final HttpServerRequest httpServerRequest) {
        final HttpServerResponse httpServerResponse = httpServerRequest.response();

        try {
            httpServerRequest.bodyHandler(new Handler<Buffer>() {
                @Override
                public void handle(Buffer event) {
                    String body = event.toString();

                    AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                    try {
                        Future<Response> future = asyncHttpClient.preparePost(GCM_URL)
                                .setHeader("Content-Type", "application/json")
                                .setHeader("Authorization", AUTH_KEY)
                                .setBody(body)
                                .execute(new AsyncCompletionHandler<Response>() {
                            @Override
                            public Response onCompleted(Response response) throws Exception {
//                                logger.info(response.getResponseBody());
                                httpServerRequest.response().end("success");
                                return null;
                            }
                        });
                    } catch (IOException e) {
                        logger.error(e.toString());
                        httpServerRequest.response().setStatusCode(400).end(e.toString());
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            httpServerRequest.response().setStatusCode(400).end("error");
        }
    }
}