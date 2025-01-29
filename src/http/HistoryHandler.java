package http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import controllers.TaskManager;
import http.adapter.EpicConverter;
import http.adapter.SubtaskConverter;
import http.adapter.TaskConverter;
import model.Epic;
import model.Subtask;
import model.Task;

import java.io.IOException;
import java.util.List;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {
    private TaskManager tm;
    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(Task.class, new TaskConverter())
            .registerTypeAdapter(Subtask.class, new SubtaskConverter())
            .registerTypeAdapter(Epic.class, new EpicConverter())
            .create();

    public HistoryHandler(TaskManager tm) {
        this.tm = tm;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());

        switch (endpoint) {
            case GET -> handleGet(exchange);
            default -> writeResponse(exchange, "Такого эндпоинта не существует", 404);
        }
    }

    private void handleGet(HttpExchange exchange) throws IOException {
        List<Task> list = tm.getHistory();
        String responseString = gson.toJson(list);
        sendText(exchange,responseString);
    }
}
