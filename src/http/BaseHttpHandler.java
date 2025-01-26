package http;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.sun.net.httpserver.HttpExchange;
import model.Epic;
import model.Subtask;
import model.Task;
import service.Status;
import service.TypeTask;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BaseHttpHandler {
    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected void sendNotFound(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(404, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected void sendHasInteractions(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(406, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected Endpoint getEndpoint(String requestPath, String requestMethod) {
        String[] pathParts = requestPath.split("/");

        if (requestMethod.equals("GET")) {
            if (pathParts.length == 2) return Endpoint.GET;
            else if (pathParts.length == 3) return Endpoint.GET_ID;
            else if (pathParts.length == 4 & pathParts[3].equals("subtasks")) return Endpoint.GET_ID;
        }
        if (requestMethod.equals("POST") & pathParts.length == 2) return Endpoint.POST;
        if (requestMethod.equals("DELETE")) return Endpoint.DELETE;
        return Endpoint.UNKNOWN;
    }

    protected void writeResponse(HttpExchange exchange,
                                 String responseString,
                                 int responseCode) throws IOException {
        try (OutputStream os = exchange.getResponseBody()) {
            byte[] resp = responseString.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
            exchange.sendResponseHeaders(responseCode, resp.length);
            os.write(resp);
        }
        exchange.close();
    }

    protected Optional<Integer> getIdOpt(String str) {
        try {
            return Optional.of(Integer.parseInt(str));
        } catch (NumberFormatException exception) {
            return Optional.empty();
        }
    }

    enum Endpoint {GET, GET_ID, POST, DELETE, UNKNOWN}
}






