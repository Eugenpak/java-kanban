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
                .create();
        //Gson gson = new Gson();


        // извлеките идентификатор поста и обработайте исключительные ситуации
        String[] splitStrings = exchange.getRequestURI().getPath().split("/");

        int rCode = 200;
        List<Task> list = tm.getListTask();
        String responseString = list.stream()
                .map(Task::toString)
                .collect(Collectors.joining("\n"));   //gson.toJson(list);
        //System.out.println(responseString);
              /*  .stream()
                .map(gson::toJson)
                .collect(Collectors.joining("\n")); */
        responseString = gson.toJson(list);
        writeResponse(exchange, responseString,rCode);
    }
    private void handleGetId(HttpExchange exchange) throws IOException {
        // извлеките идентификатор поста и обработайте исключительные ситуации
        String[] splitStrings = exchange.getRequestURI().getPath().split("/");
        Optional<Integer> idOpt = getIdOpt(splitStrings[2]);
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Task.class, new TaskConverter())
                .create();
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
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Task.class, new TaskConverter())
                .create();
        InputStream inputStream = exchange.getRequestBody();
        String str = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

        Task task = gson.fromJson(str,Task.class);
        /*
        Task t = tm.getTaskById(0);
        String json = gson.toJson(t);

        Task x;
        try {
            x = gson.fromJson(json, Task.class);
        } catch (JsonParseException e) {
            System.out.println(e);
            x = null;
        }

        int rCode = 277;
        String responseString = x.toString();
        */
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
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Task.class, new TaskConverter())
                .create();
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
