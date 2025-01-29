package http;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class BaseHttpHandler {
    protected void sendText(HttpExchange h, String text) throws IOException {
        writeResponse(h,text,200);
    }

    protected void sendCreated(HttpExchange h, String text) throws IOException {
        writeResponse(h,text,201);
    }

    protected void sendNotFound(HttpExchange h, String text) throws IOException {
        writeResponse(h,text,404);
    }

    protected void sendHasInteractions(HttpExchange h, String text) throws IOException {
        writeResponse(h,text,406);
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

    enum Endpoint { GET, GET_ID, POST, DELETE, UNKNOWN }
}






