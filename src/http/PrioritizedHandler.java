package http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import controllers.TaskManager;
import http.adapter.EpicConverter;
import http.adapter.LocalDateTimeAdapter;
import http.adapter.SubtaskConverter;
import http.adapter.TaskConverter;
import model.Epic;
import model.Subtask;
import model.Task;

import java.io.IOException;
import java.util.List;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {
    private TaskManager tm;

    public PrioritizedHandler(TaskManager tm) {
        this.tm = tm;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());

        switch (endpoint) {
            case GET -> {
                handleGet(exchange);
                break;
            }
            default -> writeResponse(exchange, "Такого эндпоинта не существует", 404);
        }
    }

    private void handleGet(HttpExchange exchange) throws IOException {
        // адаптер для преобразования типа LocalTime в String в формате субтитров
        LocalDateTimeAdapter localDateTimeAdapter = new LocalDateTimeAdapter();
        // реализуйте обработку запроса на добавление комментария

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Task.class, new TaskConverter())
                .registerTypeAdapter(Subtask.class, new SubtaskConverter())
                .registerTypeAdapter(Epic.class, new EpicConverter())
                .create();
        // извлеките идентификатор поста и обработайте исключительные ситуации
        String[] splitStrings = exchange.getRequestURI().getPath().split("/");

        int rCode = 200;
        List<Task> list = tm.getPrioritizedTasks();
        String responseString = gson.toJson(list);
        writeResponse(exchange, responseString, rCode);
    }
}