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
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class SubtaskHandler extends BaseHttpHandler implements HttpHandler {
    private TaskManager tm;

    public SubtaskHandler(TaskManager tm) {
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
            case GET_ID -> {
                handleGetId(exchange);
                break;
            }
            case POST -> {
                handlePost(exchange);
                break;
            }
            case DELETE -> {
                handleDelete(exchange);
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
                .registerTypeAdapter(LocalDateTime.class, localDateTimeAdapter)
                .registerTypeAdapter(Task.class, new TaskConverter())
                .registerTypeAdapter(Epic.class, new EpicConverter())
                .registerTypeAdapter(Subtask.class, new SubtaskConverter())
                .create();
        //Gson gson = new Gson();


        // извлеките идентификатор поста и обработайте исключительные ситуации
        String[] splitStrings = exchange.getRequestURI().getPath().split("/");

        int rCode = 200;
        List<Subtask> list = tm.getListSubtask();
        String responseString = gson.toJson(list);
        writeResponse(exchange, responseString, rCode);
    }

    private void handleGetId(HttpExchange exchange) throws IOException {
        // извлеките идентификатор поста и обработайте исключительные ситуации
        String[] splitStrings = exchange.getRequestURI().getPath().split("/");
        Optional<Integer> idOpt = getIdOpt(splitStrings[2]);
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Task.class, new TaskConverter())
                .registerTypeAdapter(Subtask.class, new SubtaskConverter())
                .create();
        int rCode;
        String responseString;
        if (idOpt.isEmpty()) { // Проверка корректности идентификатор поста
            rCode = 404;
            responseString = "Некорректный идентификатор подзадачи";
        } else {
            Integer taskId = idOpt.get();
            Optional<Task> taskOpt = Optional.ofNullable(tm.getSubtaskById(taskId));

            if (taskOpt.isPresent()) { // Проверка наличие задачи
                rCode = 200;
                responseString = gson.toJson(taskOpt.get());
            } else {
                rCode = 404;
                responseString = "Task с идентификатором " + taskId + " не найден";
            }
        }
        writeResponse(exchange, responseString, rCode);
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        // извлеките идентификатор поста и обработайте исключительные ситуации
        String[] splitStrings = exchange.getRequestURI().getPath().split("/");
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Task.class, new TaskConverter())
                .registerTypeAdapter(Subtask.class, new SubtaskConverter())
                .create();
        InputStream inputStream = exchange.getRequestBody();
        String str = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

        Subtask subtask = gson.fromJson(str, Subtask.class);

        Optional<Integer> idTaskOpt = Optional.ofNullable(subtask.getId());
        System.out.println("Запрос POST " + Instant.now());
        int rCode;
        String responseString;

        if (idTaskOpt.isEmpty() | idTaskOpt.get() == -3) { // Проверка корректности идентификатор поста
            int createId = tm.addNewSubtask(subtask);
            if (tm.getSubtaskById(createId) == null) {
                rCode = 406;
                responseString = "Subtask пересекает задачу, действие addNewSubtask() прервано!";
            } else {
                rCode = 201;
                responseString = "Новый Subtask добавлен, id = " + createId;
            }

        } else {
            Integer taskId = idTaskOpt.get();
            Optional<Subtask> taskOpt = Optional.ofNullable(tm.getSubtaskById(taskId));

            if (taskOpt.isPresent()) { // Проверка наличие задачи
                if (tm.updateSubtask(subtask)) {
                    rCode = 201;
                    responseString = "Subtask c id = " + taskId + " обновлен";
                } else {
                    rCode = 406;
                    responseString = "Subtask пересекает задачу, действие updateSubtask() прервано!";
                }

            } else {
                rCode = 404;
                responseString = "Subtask с идентификатором " + taskId + " не найден";
            }
        }

        writeResponse(exchange, responseString, rCode);
    }

    private void handleDelete(HttpExchange exchange) throws IOException {
        // извлеките идентификатор поста и обработайте исключительные ситуации
        String[] splitStrings = exchange.getRequestURI().getPath().split("/");
        Optional<Integer> idOpt = getIdOpt(splitStrings[2]);
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Task.class, new TaskConverter())
                .registerTypeAdapter(Subtask.class, new SubtaskConverter())
                .create();
        int rCode;
        String responseString;
        if (idOpt.isEmpty()) { // Проверка корректности идентификатор поста
            rCode = 404;
            responseString = "Некорректный идентификатор Task";
        } else {
            Integer taskId = idOpt.get();
            Optional<Task> taskOpt = Optional.ofNullable(tm.getSubtaskById(taskId));

            if (taskOpt.isPresent()) { // Проверка наличие задачи
                tm.deleteSubtask(taskId);
                rCode = 200;
                responseString = "Subtask с идентификатором " + taskId + " удален";
            } else {
                rCode = 404;
                responseString = "Subtask с идентификатором " + taskId + " не найден";
            }
        }
        writeResponse(exchange, responseString, rCode);
    }
}
