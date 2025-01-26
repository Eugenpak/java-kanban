package controllers;

import com.sun.net.httpserver.HttpServer;
import http.*;
import model.Epic;
import model.Subtask;
import model.Task;
import service.Status;

import java.io.*;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private TaskManager manager;
    private HttpServer server;

    public HttpTaskServer() throws IOException {
        this(Managers.getDefault());
    }
    public HttpTaskServer(TaskManager taskManager) throws IOException {
        manager = taskManager;
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/tasks", new TaskHandler(manager));
        server.createContext("/subtasks", new SubtaskHandler(manager));
        server.createContext("/epics", new EpicHandler(manager));
        server.createContext("/history", new HistoryHandler(manager));
        server.createContext("/prioritized", new PrioritizedHandler(manager));
    }

    public static void main(String[] args) throws IOException {
        HttpTaskServer taskServer = new HttpTaskServer();
        taskServer.fillTaskManager();
        taskServer.start(); // запускаем сервер
    }

    public void start() {
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
        server.start();
    }
    public void stop() {
        server.stop(0);
    }
    public void fillTaskManager() {
        LocalDateTime start = LocalDateTime.of(2024,10,10,8,0,0);
        Duration durationT = Duration.ofMinutes(10);
        Task task = new Task("N-T0","D-T0", Status.NEW,start,durationT);
        manager.addNewTask(task);

        Task task1 = new Task("N-T1","D-T1",Status.NEW,start.plusMinutes(20),durationT);
        manager.addNewTask(task1);

        Subtask s = new Subtask("N-S2","D-S2",Status.NEW,start.plusMinutes(35),durationT);
        Epic epic = new Epic("N-E3","D-E3");
        int epicId = manager.addNewEpic(epic);
        s.setEpicId(epicId);
        manager.addNewSubtask(s);

        s = new Subtask("N-S4","D-S4",Status.NEW,start.minusMinutes(45),durationT);
        s.setEpicId(epicId);
        manager.addNewSubtask(s);
        s = new Subtask("N-S5","D-S5",Status.NEW,start.minusMinutes(50),durationT);
        s.setEpicId(-5);
        manager.addNewSubtask(s);
    }
}
