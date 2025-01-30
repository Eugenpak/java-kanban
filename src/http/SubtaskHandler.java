package http;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import controllers.Managers;
import controllers.TaskManager;
import model.Subtask;
import model.Task;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Optional;

public class SubtaskHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager tm;
    private final Gson gson;

    public SubtaskHandler(TaskManager tm) {
        this.tm = tm;
        gson = Managers.getGson();
    }

    @Override
    protected void handleGet(HttpExchange exchange) throws IOException, NullPointerException {
        String[] splitStrings = exchange.getRequestURI().getPath().split("/");

        if (splitStrings.length == 2) handleGetSubtask(exchange);
        else if (splitStrings.length == 3) handleGetIdSubtask(exchange);
        else writeResponse(exchange, "Такого ресурса не существует", 404);
    }

    private void handleGetSubtask(HttpExchange exchange) throws IOException {
        String responseString = gson.toJson(tm.getListSubtask());
        sendText(exchange,responseString);
    }

    private void handleGetIdSubtask(HttpExchange exchange) throws IOException, NullPointerException {
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

    @Override
    protected void handlePost(HttpExchange exchange) throws IOException, NullPointerException {
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

    @Override
    protected void handleDelete(HttpExchange exchange) throws IOException, NullPointerException {
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
