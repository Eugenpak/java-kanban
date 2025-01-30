package http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class BaseHttpHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestMethod());
        try {
            switch (endpoint) {
                case GET -> handleGet(exchange);
                case POST -> handlePost(exchange);
                case DELETE -> handleDelete(exchange);
                default -> writeResponse(exchange, "Такого ресурса не существует", 404);
            }
        } catch (NullPointerException e) {
            System.out.println(e.getMessage() + " NullPointerException ()");
            e.printStackTrace(System.out);
            writeResponse(exchange, "Произошла ошибка при обработке запроса", 500);
        }
    }

    protected void handleGet(HttpExchange exchange) throws IOException {
        sendMethodNotAllowed(exchange,"Метод запроса GET не поддерживается");
    }

    protected void handleGetId(HttpExchange exchange) throws IOException, NullPointerException {
        sendMethodNotAllowed(exchange,"Метод запроса GET не поддерживается");
    }

    protected void handlePost(HttpExchange exchange) throws IOException, NullPointerException {
        sendMethodNotAllowed(exchange,"Метод запроса POST не поддерживается");
    }

    protected void handleDelete(HttpExchange exchange) throws IOException {
        sendMethodNotAllowed(exchange,"Метод запроса DELETE не поддерживается");
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        writeResponse(h,text,200);
    }

    protected void sendCreated(HttpExchange h, String text) throws IOException {
        writeResponse(h,text,201);
    }

    protected void sendNotFound(HttpExchange h, String text) throws IOException {
        writeResponse(h,text,404);
    }

    protected void sendMethodNotAllowed(HttpExchange h, String text) throws IOException {
        writeResponse(h,text,405);
    }

    protected void sendHasInteractions(HttpExchange h, String text) throws IOException {
        writeResponse(h,text,406);
    }

    protected Endpoint getEndpoint(String requestMethod) {
        if (requestMethod.equals("GET")) return Endpoint.GET;
        if (requestMethod.equals("POST")) return Endpoint.POST;
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

    enum Endpoint { GET, POST, DELETE, UNKNOWN }
}






