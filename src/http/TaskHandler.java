package http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import controllers.TaskManager;
import http.adapter.LocalDateTimeAdapter;
import http.adapter.TaskConverter;
import model.Task;


import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {
    private TaskManager tm;

    public TaskHandler(TaskManager tm) {
        this.tm = tm;
    }
    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(Task.class, new TaskConverter())
            .create();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());

        switch (endpoint) {
            case GET -> handleGet(exchange);
            case GET_ID -> handleGetId(exchange);
            case POST -> handlePost(exchange);
            case DELETE -> handleDelete(exchange);
            default -> writeResponse(exchange, "Такого эндпоинта не существует", 404);
        }
    }

    private void handleGet(HttpExchange exchange) throws IOException {
        //Gson gson = new Gson();
        /*
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Task.class, new TaskConverter())
                .create(); */

        String[] splitStrings = exchange.getRequestURI().getPath().split("/");
        List<Task> list = tm.getListTask();
        String responseString = gson.toJson(list);

        sendText(exchange,responseString);
    }
    private void handleGetId(HttpExchange exchange) throws IOException {
        // извлеките идентификатор поста и обработайте исключительные ситуации
        String[] splitStrings = exchange.getRequestURI().getPath().split("/");
        Optional<Integer> idOpt = getIdOpt(splitStrings[2]);
        /*
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Task.class, new TaskConverter())
                .create();
        */
        int rCode;
        String responseString;
        if (idOpt.isEmpty()) { // Проверка корректности идентификатор поста
            rCode = 404;
            responseString = "Некорректный идентификатор id = "+ splitStrings[2];
        } else {
            Integer taskId = idOpt.get();
            Optional<Task> taskOpt = Optional.ofNullable(tm.getTaskById(taskId));

            if (taskOpt.isPresent()) { // Проверка наличие задачи
                rCode = 200;
                responseString = gson.toJson(taskOpt.get());
            } else {
                rCode = 404;
                responseString = "Task с идентификатором " + taskId + " не найден";
            }
        }
        writeResponse(exchange, responseString,rCode);
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        // извлеките идентификатор поста и обработайте исключительные ситуации
        String[] splitStrings = exchange.getRequestURI().getPath().split("/");
        /*
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Task.class, new TaskConverter())
                .create();
        */
        InputStream inputStream = exchange.getRequestBody();
        String str = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

        Task task = gson.fromJson(str,Task.class);

        Optional<Integer> idTaskOpt = Optional.ofNullable(task.getId());
        System.out.println("Запрос POST " + Instant.now());
        int rCode;
        String responseString;

        if (idTaskOpt.isEmpty() | idTaskOpt.get() == -3) { // Проверка корректности идентификатор поста
            int createId = tm.addNewTask(task);
            if (tm.getTaskById(createId) == null) {
                rCode = 406;
                responseString = "Task пересекается существующими, действие addNewTask() прервано!";
            } else {
                rCode = 201;
                responseString = "Новый Task добавлен";
            }

        } else {
            Integer taskId = idTaskOpt.get();
            Optional<Task> taskOpt = Optional.ofNullable(tm.getTaskById(taskId));

            if (taskOpt.isPresent()) { // Проверка наличие задачи
                if (tm.updateTask(task)) {
                    rCode = 201;
                    responseString = "Task c id = " + taskId + " обновлен";
                } else {
                    rCode = 406;
                    responseString = "Task пересекается существующими, действие updateTask() прервано!";
                }

            } else {
                rCode = 404;
                responseString = "Task с идентификатором " + taskId + " не найден";
            }
        }

        writeResponse(exchange, responseString,rCode);
    }

    private void handleDelete(HttpExchange exchange) throws IOException {
        // извлеките идентификатор поста и обработайте исключительные ситуации
        String[] splitStrings = exchange.getRequestURI().getPath().split("/");
        Optional<Integer> idOpt = getIdOpt(splitStrings[2]);
        /*
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Task.class, new TaskConverter())
                .create();
        */
        int rCode;
        String responseString;
        if (idOpt.isEmpty()) { // Проверка корректности идентификатор поста
            rCode = 404;
            responseString = "Некорректный идентификатор Task";
        } else {
            Integer taskId = idOpt.get();
            Optional<Task> taskOpt = Optional.ofNullable(tm.getTaskById(taskId));

            if (taskOpt.isPresent()) { // Проверка наличие задачи
                tm.deleteTask(taskId);
                rCode = 200;
                responseString = "Task с идентификатором " + taskId + " удален";
            } else {
                rCode = 404;
                responseString = "Task с идентификатором " + taskId + " не найден";
            }
        }
        writeResponse(exchange, responseString,rCode);
    }

    class TaskListTypeToken extends TypeToken<List<Task>> {
    }
}
