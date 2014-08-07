package org.nhnnext.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.vertx.java.core.Handler;
import org.vertx.java.core.http.HttpServerRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.vertx.java.core.http.HttpServerResponse;

import java.util.HashMap;
import java.util.Map;

public class GcmHandler implements Handler<HttpServerRequest> {
    @Override
    public void handle(HttpServerRequest httpServerRequest) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> map = new HashMap<String, String>();
        map.put("result", "pine");
        map.put("message", "");

        HttpServerResponse response = httpServerRequest.response();
        response.setStatusCode(200);

        String json = null;
        try {
            json = mapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        httpServerRequest.response().end(json);
    }
}