package org.nhnnext.handler;

import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsNotification;
import com.notnoop.apns.ApnsService;
import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.HttpServerResponse;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.logging.Logger;


public class ApnsHandler implements Handler<HttpServerRequest> {
    private final Logger logger;

    private static final String certificate = "/home/next/VeilCertificates.p12";

    private static ApnsService apnsService;

    static {
        apnsService = APNS.newService()
                .withCert(certificate, "Shltlsrud11rowjfeks^^")
                .withSandboxDestination()
//                .asPool(2)
                .build();
    }

    public ApnsHandler(Logger logger) {
        this.logger = logger;
    }

    // todo: inactive device handling
//    Map<String, Date> inactiveDevices = service.getInactiveDevices();
//    for (String deviceToken : inactiveDevices.keySet()) {
//        Date inactiveAsOf = inactiveDevices.get(deviceToken);
//        ...
//    }

    @Override
    public void handle(final HttpServerRequest httpServerRequest) {
        final HttpServerResponse httpServerResponse = httpServerRequest.response();

        try {
            httpServerRequest.bodyHandler(new Handler<Buffer>() {
                @Override
                public void handle(Buffer event) {
                    String body = event.toString();
                    logger.info("APNS: " + body);

                    JsonObject jsonObject = new JsonObject(body);

                    String token = jsonObject.getString("token");
                    String alertBody = jsonObject.getString("alert_body");

                    String threadId = "";
                    if (jsonObject.containsField("thread_id"))
                        threadId = String.valueOf(jsonObject.getInteger("thread_id"));

                    String eventDate = "";
                    if (jsonObject.containsField("event_date"))
                        eventDate = String.valueOf(jsonObject.getString("event_date"));

                    String imageUrl="";
                    if (jsonObject.containsField("image_url"))
                        imageUrl = jsonObject.getString("image_url");

                    String payload = APNS.newPayload()
                            .alertBody(alertBody)
                            .badge(1)
                            .sound("default")
                            .customField("thread_id", threadId)
                            .customField("event_date", eventDate)
                            .customField("image_url", imageUrl)
                            .build();

                    ApnsNotification notification = apnsService.push(token, payload);
                    httpServerRequest.response().end(String.valueOf(notification.getExpiry()));
                }
            });
        } catch (Exception e) {
            logger.error(e);
            httpServerRequest.response().setStatusCode(400).end("error");
        }
    }
}