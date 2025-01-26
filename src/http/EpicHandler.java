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

public class EpicHandler  extends BaseHttpHandler implements HttpHandler {
    private TaskManager tm;

    public EpicHandler(TaskManager tm) {
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
        List<Epic> list = tm.getListEpic();
        String responseString = gson.toJson(list);
        writeResponse(exchange, responseString, rCode);
    }

    private void handleGetId(HttpExchange exchange) throws IOException {
        // извлеките идентификатор поста и обработайте исключительные ситуации
        String[] splitStrings = exchange.getRequestURI().getPath().split("/");
        Optional<Integer> idOpt = getIdOpt(splitStrings[2]);
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Task.class, new TaskConverter())
                .registerTypeAdapter(Epic.class, new EpicConverter())
                .registerTypeAdapter(Subtask.class, new SubtaskConverter())
                .create();
        int rCode;
        String responseString;
        if (idOpt.isEmpty()) { // Проверка корректности идентификатор Epic
            rCode = 404;
            responseString = "Некорректный идентификатор Epic";
        } else {
            Integer taskId = idOpt.get();
            Optional<Epic> taskOpt = Optional.ofNullable(tm.getEpicById(taskId));

            if (taskOpt.isPresent()) { // Проверка наличие Epic
                rCode = 200;
                if (splitStrings.length == 4 && splitStrings[3].equals("subtasks")) {
                    responseString = gson.toJson(taskOpt.get().getArraySubtask());
                } else {
                    responseString = gson.toJson(taskOpt.get());
                }
            } else {
                rCode = 404;
                responseString = "Epic с идентификатором " + taskId + " не найден";
            }
        }
        writeResponse(exchange, responseString, rCode);
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        // извлеките идентификатор поста и обработайте исключительные ситуации
        String[] splitStrings = exchange.getRequestURI().getPath().split("/");
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Task.class, new TaskConverter())
                .registerTypeAdapter(Epic.class, new EpicConverter())
                .registerTypeAdapter(Subtask.class, new SubtaskConverter())
                .create();
        InputStream inputStream = exchange.getRequestBody();
        String str = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        Epic epic = null;
        try {
            epic = gson.fromJson(str, Epic.class);
        } catch (Exception e) {
            e.printStackTrace();
        }


        Optional<Integer> idTaskOpt = Optional.ofNullable(epic.getId());
        System.out.println("Запрос POST " + Instant.now());
        int rCode;
        String responseString;

        if (idTaskOpt.isEmpty() | idTaskOpt.get() == -3) { // Проверка корректности идентификатор поста
            int createId = tm.addNewEpic(epic);
            if (tm.getEpicById(createId) == null) {
                rCode = 410;
                responseString = "Epic не создан, addNewEpic()";
            } else {
                rCode = 201;
                responseString = "Новый Epic добавлен, id = " + createId;
            }
        } else {
            Integer taskId = idTaskOpt.get();
            Optional<Epic> taskOpt = Optional.ofNullable(tm.getEpicById(taskId));

            if (taskOpt.isPresent()) { // Проверка наличие задачи
                if (tm.updateEpic(epic)) {
                    rCode = 201;
                    responseString = "Epic c id = " + taskId + " обновлен";
                } else {
                    rCode = 410;
                    responseString = "Epic нет обработчика, updateEpic()";
                }

            } else {
                rCode = 404;
                responseString = "Epic с идентификатором " + taskId + " не найден";
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
                .registerTypeAdapter(Epic.class, new EpicConverter())
                .registerTypeAdapter(Subtask.class, new SubtaskConverter())
                .create();
        int rCode;
        String responseString;
        if (idOpt.isEmpty()) { // Проверка корректности идентификатор поста
            rCode = 404;
            responseString = "Некорректный идентификатор Epic";
        } else {
            Integer taskId = idOpt.get();
            Optional<Task> taskOpt = Optional.ofNullable(tm.getEpicById(taskId));

            if (taskOpt.isPresent()) { // Проверка наличие задачи
                tm.deleteEpic(taskId);
                rCode = 200;
                responseString = "Epic с идентификатором " + taskId + " удален";
            } else {
                rCode = 404;
                responseString = "Epic с идентификатором " + taskId + " не найден";
            }
        }
        writeResponse(exchange, responseString, rCode);
    }
}
