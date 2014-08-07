package org.nhnnext;
/*
 * Copyright 2013 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 */

import org.nhnnext.handler.GcmHandler;
import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.AsyncResultHandler;
import org.vertx.java.core.Handler;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.RouteMatcher;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.platform.Verticle;

public class MainVerticle extends Verticle {
    private static final int port = 8000;
    private static final String hostname = "127.0.0.1";

    public void start() {
        HttpServer server = vertx.createHttpServer();

        RouteMatcher routeMatcher = new RouteMatcher();
        routeMatcher.post("/push/gcm", new GcmHandler());
        server.requestHandler(routeMatcher);

        final Logger logger = container.logger();
        server.listen(port, new AsyncResultHandler<HttpServer>() {
            @Override
            public void handle(AsyncResult<HttpServer> asyncResult) {
                if (asyncResult.succeeded())
                    logger.info("Webserver started, listening on port: " + port);
                else
                    logger.error("Webserver start failed.");
            }
        });

//        EventBus eb = vertx.eventBus();
//
//        Handler<Message> myHandler = new Handler<Message>() {
//            public void handle(Message message) {
//                System.out.println("I received a message " + message.body);
//            }
//        };
//
//        eb.registerHandler("test.address", myHandler);

//        server.requestHandler(new Handler<HttpServerRequest>() {
//            @Override
//            public void handle(HttpServerRequest httpServerRequest) {
//                httpServerRequest.response().end("Hello smartjava");
//            }
//        }).listen(8888);
    }
}
