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
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public class EpicHandler  extends BaseHttpHandler implements HttpHandler {
    private TaskManager tm;
    private Gson gson = new GsonBuilder()
            //.registerTypeAdapter(LocalDateTime.class, localDateTimeAdapter)
            .registerTypeAdapter(Task.class, new TaskConverter())
            .registerTypeAdapter(Epic.class, new EpicConverter())
            .registerTypeAdapter(Subtask.class, new SubtaskConverter())
            .create();

    public EpicHandler(TaskManager tm) {
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
            System.out.println(e.getMessage() + " NullPointerException (EpicHandler)");
            e.printStackTrace(System.out);
            writeResponse(exchange, "Произошла ошибка при обработке запроса", 500);
        }
    }

    private void handleGet(HttpExchange exchange) throws IOException {
        List<Epic> list = tm.getListEpic();
        String responseString = gson.toJson(list);
        //rCode = 200;
        sendText(exchange,responseString);
    }

    private void handleGetId(HttpExchange exchange) throws IOException, NullPointerException {
        String[] splitStrings = exchange.getRequestURI().getPath().split("/");
        Optional<Integer> idOpt = getIdOpt(splitStrings[2]);

        String responseString;
        if (idOpt.isEmpty()) { // Проверка корректности идентификатор Epic
            //rCode = 404;
            responseString = "Некорректный идентификатор Epic";
            sendNotFound(exchange,responseString);
        } else {
            Integer taskId = idOpt.get();
            Optional<Epic> taskOpt = Optional.ofNullable(tm.getEpicById(taskId));

            if (taskOpt.isPresent()) { // Проверка наличие Epic
                //rCode = 200;
                if (splitStrings.length == 4 && splitStrings[3].equals("subtasks")) {
                    responseString = gson.toJson(taskOpt.get().getArraySubtask());
                } else {
                    responseString = gson.toJson(taskOpt.get());
                }
                sendText(exchange,responseString);
            } else {
                //rCode = 404;
                responseString = "Epic с идентификатором " + taskId + " не найден";
                sendNotFound(exchange,responseString);
            }
        }
    }

    private void handlePost(HttpExchange exchange) throws IOException, NullPointerException {
        InputStream inputStream = exchange.getRequestBody();
        String str = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        Epic epic = gson.fromJson(str, Epic.class);

        Optional<Integer> idTaskOpt = Optional.ofNullable(epic.getId());
        System.out.println("Запрос POST " + Instant.now());

        String responseString;

        if (idTaskOpt.isEmpty() | idTaskOpt.get() == -3) { // Проверка корректности идентификатор
            int createId = tm.addNewEpic(epic);
            if (tm.getEpicById(createId) == null) {
                //rCode = 410;
                responseString = "Epic не создан, addNewEpic()";
                writeResponse(exchange,responseString,410);
            } else {
                //rCode = 201;
                responseString = "Новый Epic добавлен, id = " + createId;
                sendCreated(exchange,responseString);
            }
        } else {
            Integer taskId = idTaskOpt.get();
            Optional<Epic> taskOpt = Optional.ofNullable(tm.getEpicById(taskId));

            if (taskOpt.isPresent()) { // Проверка наличие задачи
                if (tm.updateEpic(epic)) {
                    //rCode = 201;
                    responseString = "Epic c id = " + taskId + " обновлен";
                    sendCreated(exchange,responseString);
                } else {
                    //rCode = 410;
                    responseString = "Epic нет обработчика, updateEpic()";
                    writeResponse(exchange,responseString,410);
                }
            } else {
                //rCode = 404;
                responseString = "Epic с идентификатором " + taskId + " не найден";
                sendNotFound(exchange,responseString);
            }
        }
    }

    private void handleDelete(HttpExchange exchange) throws IOException {
        String[] splitStrings = exchange.getRequestURI().getPath().split("/");
        Optional<Integer> idOpt = getIdOpt(splitStrings[2]);

        String responseString;
        if (idOpt.isEmpty()) { // Проверка корректности идентификатор
            //rCode = 404;
            responseString = "Некорректный идентификатор Epic";
            sendNotFound(exchange,responseString);
        } else {
            Integer taskId = idOpt.get();
            Optional<Task> taskOpt = Optional.ofNullable(tm.getEpicById(taskId));

            if (taskOpt.isPresent()) { // Проверка наличие задачи
                tm.deleteEpic(taskId);
                //rCode = 200;
                responseString = "Epic с идентификатором " + taskId + " удален";
                sendText(exchange,responseString);
            } else {
                //rCode = 404;
                responseString = "Epic с идентификатором " + taskId + " не найден";
                sendNotFound(exchange,responseString);
            }
        }
    }
}
