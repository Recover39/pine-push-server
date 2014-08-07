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
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.RouteMatcher;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.platform.Verticle;

public class MainVerticle extends Verticle {
    private static final int port = 8000;
    private static final String hostname = "0.0.0.0";

    public void start() {
        final Logger logger = container.logger();
        HttpServer server = vertx.createHttpServer();

        RouteMatcher routeMatcher = new RouteMatcher();
        routeMatcher.post("/push/gcm", new GcmHandler(logger));
        server.requestHandler(routeMatcher);

        server.listen(port, hostname, new AsyncResultHandler<HttpServer>() {
            @Override
            public void handle(AsyncResult<HttpServer> asyncResult) {
                if (asyncResult.succeeded())
                    logger.info("Webserver started, listening on port: " + port);
                else
                    logger.error("Webserver start failed.");
            }
        });
    }
}
