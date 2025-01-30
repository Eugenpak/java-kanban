package http;

import com.google.gson.Gson;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import controllers.Managers;
import controllers.TaskManager;

import java.io.IOException;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager tm;
    private final Gson gson;

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