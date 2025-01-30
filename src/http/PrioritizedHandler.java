package http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import controllers.Managers;
import controllers.TaskManager;
import http.adapter.EpicConverter;
import http.adapter.SubtaskConverter;
import http.adapter.TaskConverter;
import model.Epic;
import model.Subtask;
import model.Task;

import java.io.IOException;
import java.util.List;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager tm;
    private Gson gson;

    public PrioritizedHandler(TaskManager tm) {
        this.tm = tm;
        gson = Managers.getGson();
    }

    @Override
    protected void handleGet(HttpExchange exchange) throws IOException {
        String responseString = gson.toJson(tm.getPrioritizedTasks());
        sendText(exchange,responseString);
    }
}