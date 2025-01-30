package http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import controllers.Managers;
import controllers.TaskManager;
import model.Task;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Optional;


public class TaskHandler extends BaseHttpHandler {
    private final TaskManager tm;
    private final Gson gson;

    public TaskHandler(TaskManager tm) {
        this.tm = tm;
        gson = Managers.getGson();
    }

    @Override
    protected void handleGet(HttpExchange exchange) throws IOException, NullPointerException {
        String[] splitStrings = exchange.getRequestURI().getPath().split("/");

        if (splitStrings.length == 2) handleGetTask(exchange);
        else if (splitStrings.length == 3) handleGetIdTask(exchange);
        else writeResponse(exchange, "Такого ресурса не существует", 404);
    }

    private void handleGetTask(HttpExchange exchange) throws IOException {
        String responseString = gson.toJson(tm.getListTask());
        sendText(exchange,responseString);
    }

    private void handleGetIdTask(HttpExchange exchange) throws IOException, NullPointerException {
        String[] splitStrings = exchange.getRequestURI().getPath().split("/");
        Optional<Integer> idOpt = getIdOpt(splitStrings[2]);

        String responseString;
        if (idOpt.isEmpty()) { // Проверка корректности идентификатор
            //rCode = 404;
            responseString = "Некорректный идентификатор id = " + splitStrings[2];
            sendNotFound(exchange,responseString);
        } else {
            Integer taskId = idOpt.get();
            Optional<Task> taskOpt = Optional.ofNullable(tm.getTaskById(taskId));

            if (taskOpt.isPresent()) { // Проверка наличие задачи
                //rCode = 200;
                responseString = gson.toJson(taskOpt.get());
                sendText(exchange,responseString);
            } else {
                //rCode = 404;
                responseString = "Task с идентификатором " + taskId + " не найден";
                sendNotFound(exchange,responseString);
            }
        }
    }

    @Override
    protected void handlePost(HttpExchange exchange) throws IOException, NullPointerException {
        InputStream inputStream = exchange.getRequestBody();
        String str = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

        Task task = gson.fromJson(str,Task.class);

        Optional<Integer> idTaskOpt = Optional.ofNullable(task.getId());
        System.out.println("Запрос POST " + Instant.now());
        String responseString;

        if (idTaskOpt.isEmpty() | idTaskOpt.get() == -3) { // Проверка корректности идентификатор поста
            int createId = tm.addNewTask(task);
            if (tm.getTaskById(createId) == null) {
                //rCode = 406;
                responseString = "Task пересекается существующими, действие addNewTask() прервано!";
                sendHasInteractions(exchange,responseString);
            } else {
                //rCode = 201;
                responseString = "Новый Task добавлен";
                sendCreated(exchange,responseString);
            }

        } else {
            Integer taskId = idTaskOpt.get();
            Optional<Task> taskOpt = Optional.ofNullable(tm.getTaskById(taskId));

            if (taskOpt.isPresent()) { // Проверка наличие задачи
                if (tm.updateTask(task)) {
                    //rCode = 201;
                    responseString = "Task c id = " + taskId + " обновлен";
                    sendCreated(exchange,responseString);
                } else {
                    //rCode = 406;
                    responseString = "Task пересекается существующими, действие updateTask() прервано!";
                    sendHasInteractions(exchange,responseString);
                }
            } else {
                //rCode = 404;
                responseString = "Task с идентификатором " + taskId + " не найден";
                sendNotFound(exchange,responseString);
            }
        }
    }

    @Override
    protected void handleDelete(HttpExchange exchange) throws IOException {
        String[] splitStrings = exchange.getRequestURI().getPath().split("/");
        Optional<Integer> idOpt = getIdOpt(splitStrings[2]);

        int rCode;
        String responseString;
        if (idOpt.isEmpty()) { // Проверка корректности идентификатор поста
            //rCode = 404;
            responseString = "Некорректный идентификатор Task";
            sendNotFound(exchange,responseString);
        } else {
            Integer taskId = idOpt.get();
            Optional<Task> taskOpt = Optional.ofNullable(tm.getTaskById(taskId));

            if (taskOpt.isPresent()) { // Проверка наличие задачи
                tm.deleteTask(taskId);
                //rCode = 200;
                responseString = "Task с идентификатором " + taskId + " удален";
                sendText(exchange,responseString);
            } else {
                //rCode = 404;
                responseString = "Task с идентификатором " + taskId + " не найден";
                sendNotFound(exchange,responseString);
            }
        }
    }
}
