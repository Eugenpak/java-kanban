package http;

import com.google.gson.*;
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
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public class SubtaskHandler extends BaseHttpHandler implements HttpHandler {
    private TaskManager tm;
    private Gson gson = new GsonBuilder()
            //.registerTypeAdapter(LocalDateTime.class, localDateTimeAdapter)
            .registerTypeAdapter(Task.class, new TaskConverter())
            .registerTypeAdapter(Epic.class, new EpicConverter())
            .registerTypeAdapter(Subtask.class, new SubtaskConverter())
            .create();

    public SubtaskHandler(TaskManager tm) {
        this.tm = tm;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());
        try {
            switch (endpoint) {
                case GET -> handleGet(exchange);
                case GET_ID -> handleGetId(exchange);
                case POST -> handlePost(exchange);
                case DELETE -> handleDelete(exchange);
                default -> writeResponse(exchange, "Такого эндпоинта не существует", 404);
            }
        } catch (NullPointerException e) {
            System.out.println(e.getMessage() + " NullPointerException (SubtaskHandler)");
            e.printStackTrace(System.out);
            writeResponse(exchange, "Произошла ошибка при обработке запроса", 500);
        }
    }

    private void handleGet(HttpExchange exchange) throws IOException {
        List<Subtask> list = tm.getListSubtask();
        String responseString = gson.toJson(list);
        sendText(exchange,responseString);
    }

    private void handleGetId(HttpExchange exchange) throws IOException, NullPointerException {
        String[] splitStrings = exchange.getRequestURI().getPath().split("/");
        Optional<Integer> idOpt = getIdOpt(splitStrings[2]);

        String responseString;
        if (idOpt.isEmpty()) { // Проверка корректности идентификатор
            //rCode = 404;
            responseString = "Некорректный идентификатор Subtask";
            sendNotFound(exchange,responseString);
        } else {
            Integer taskId = idOpt.get();
            Optional<Task> taskOpt = Optional.ofNullable(tm.getSubtaskById(taskId));

            if (taskOpt.isPresent()) { // Проверка наличие задачи
                //rCode = 200;
                responseString = gson.toJson(taskOpt.get());
                sendText(exchange,responseString);
            } else {
                //rCode = 404;
                responseString = "Subtask с идентификатором " + taskId + " не найден";
                sendNotFound(exchange,responseString);
            }
        }
    }

    private void handlePost(HttpExchange exchange) throws IOException, NullPointerException {
        InputStream inputStream = exchange.getRequestBody();
        String str = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

        String responseString;

        Subtask subtask = gson.fromJson(str, Subtask.class);
        Optional<Integer> idTaskOpt = Optional.ofNullable(subtask.getId());
        System.out.println("Запрос POST " + Instant.now());

        if (idTaskOpt.isEmpty() | idTaskOpt.get() == -3) { // Проверка корректности идентификатор поста
            int createId = tm.addNewSubtask(subtask);
            if (tm.getSubtaskById(createId) == null) {
                //rCode = 406;
                responseString = "Subtask пересекает задачу, действие addNewSubtask() прервано!";
                sendHasInteractions(exchange,responseString);
            } else {
                //rCode = 201;
                responseString = "Новый Subtask добавлен, id = " + createId;
                sendCreated(exchange,responseString);
            }
        } else {
            Integer taskId = idTaskOpt.get();
            Optional<Subtask> taskOpt = Optional.ofNullable(tm.getSubtaskById(taskId));
            if (taskOpt.isPresent()) { // Проверка наличие задачи
                if (tm.updateSubtask(subtask)) {
                    //rCode = 201;
                    responseString = "Subtask c id = " + taskId + " обновлен";
                    sendCreated(exchange,responseString);
                } else {
                    //rCode = 406;
                    responseString = "Subtask пересекает задачу, действие updateSubtask() прервано!";
                    sendHasInteractions(exchange,responseString);
                }
            } else {
                //rCode = 404;
                responseString = "Subtask с идентификатором " + taskId + " не найден";
                sendNotFound(exchange,responseString);
            }
        }
    }

    private void handleDelete(HttpExchange exchange) throws IOException, NullPointerException {
        String[] splitStrings = exchange.getRequestURI().getPath().split("/");
        Optional<Integer> idOpt = getIdOpt(splitStrings[2]);

        String responseString;
        if (idOpt.isEmpty()) { // Проверка корректности идентификатор поста
            //rCode = 404;
            responseString = "Некорректный идентификатор Subtask";
            sendNotFound(exchange,responseString);
        } else {
            Integer taskId = idOpt.get();
            Optional<Task> taskOpt = Optional.ofNullable(tm.getSubtaskById(taskId));

            if (taskOpt.isPresent()) { // Проверка наличие задачи
                tm.deleteSubtask(taskId);
                //rCode = 200;
                responseString = "Subtask с идентификатором " + taskId + " удален";
                sendText(exchange,responseString);
            } else {
                //rCode = 404;
                responseString = "Subtask с идентификатором " + taskId + " не найден";
                sendNotFound(exchange,responseString);
            }
        }
    }
}
