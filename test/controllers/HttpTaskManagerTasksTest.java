package controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import http.adapter.TaskConverter;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.Status;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskManagerTasksTest {
    private HttpTaskServer taskServer;
    private TaskManager tm;

    @AfterEach
    void tearDown() {
        taskServer.stop();
    }

    @BeforeEach
    void init() throws IOException {
        tm = Managers.getDefault();
        taskServer = new HttpTaskServer(tm);
        taskServer.start();
    }

    @Test
    void tasksPostAddNewTaskCode201() throws IOException, InterruptedException {
        Task task = new Task("N-T0", "D-T0",
                Status.NEW, LocalDateTime.now(), Duration.ofMinutes(5));
        // конвертируем её в JSON
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Task.class, new TaskConverter())
                .create();
        String taskJson = gson.toJson(task);
        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(201, response.statusCode());

        // проверяем, что создалась одна задача с корректным именем
        List<Task> tasksFromManager = tm.getListTask();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("N-T0", tasksFromManager.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    void tasksPostAddNewTaskCode406()  throws IOException, InterruptedException {
        LocalDateTime start = LocalDateTime.of(2025,1,24,8,0);
        Task task = new Task("N-T0", "D-T0",
                Status.NEW, start, Duration.ofMinutes(10));
        tm.addNewTask(task);
        task = new Task("N-T1", "D-T1",
                Status.NEW, start.plusMinutes(5), Duration.ofMinutes(10));
        // конвертируем её в JSON
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Task.class, new TaskConverter())
                .create();
        String taskJson = gson.toJson(task);
        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(406, response.statusCode());

        // проверяем, что создалась одна задача с корректным именем
        List<Task> tasksFromManager = tm.getListTask();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("N-T0", tasksFromManager.get(0).getName(), "Некорректное имя задачи");

    }

    @Test
    void tasksPostAddNewTaskException_Z()  throws IOException, InterruptedException {

    }

    @Test
    void tasksPostUpdateTaskCode201()  throws IOException, InterruptedException {
        LocalDateTime start = LocalDateTime.of(2024,10,10,8,0,0);
        Task task = new Task("N-T0", "D-T0",Status.NEW, start, Duration.ofMinutes(10));
        int idFind = tm.addNewTask(task);
        // конвертируем её в JSON
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Task.class, new TaskConverter())
                .create();
        String taskJson = gson.toJson(new Task("N-T0 ++", "D-T0",idFind,Status.DONE,start.plusMinutes(5), Duration.ofMinutes(5)));

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(201, response.statusCode());

        // проверяем, что создалась одна задача с корректным именем
        List<Task> tasksFromManager = tm.getListTask();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("N-T0 ++", tasksFromManager.get(0).getName(), "Некорректное имя задачи");
        assertEquals(0, tasksFromManager.get(0).getId(), "Некорректное значение id");
    }

    @Test
    void tasksPostUpdateTaskCode406()  throws IOException, InterruptedException {
        LocalDateTime start = LocalDateTime.of(2024,10,10,8,0,0);
        Task task = new Task("N-T0", "D-T0",Status.NEW, start, Duration.ofMinutes(10));
        tm.addNewTask(task);
        task = new Task("N-T1", "D-T1",Status.NEW, start.plusMinutes(30), Duration.ofMinutes(10));
        int idFind = tm.addNewTask(task);
        // конвертируем её в JSON
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Task.class, new TaskConverter())
                .create();
        String taskJson = gson.toJson(new Task("N-T1 ++", "D-T1",idFind,Status.DONE,start.plusMinutes(5), Duration.ofMinutes(10)));

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(406, response.statusCode());

        // проверяем, что создалась одна задача с корректным именем
        List<Task> tasksFromManager = tm.getListTask();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(2, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("N-T1", tasksFromManager.get(1).getName(), "Некорректное имя задачи");
        assertEquals(1, tasksFromManager.get(1).getId(), "Некорректное значение id");
    }

    @Test
    void tasksPostUpdateTaskException_Z()  throws IOException, InterruptedException {

    }

    @Test
    void tasksGetListTasksCode200()  throws IOException, InterruptedException {
        Task task = new Task("N-T0", "D-T0",
                Status.NEW, LocalDateTime.now(), Duration.ofMinutes(5));
        tm.addNewTask(task);
        // конвертируем её в JSON
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Task.class, new TaskConverter())
                .create();
        String taskJson = gson.toJson(task);
        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());
        List<Task> list= gson.fromJson(response.body(),  new TaskListTypeToken().getType());
        // проверяем, что создалась одна задача с корректным именем

        assertNotNull(list, "Задачи не возвращаются");
        assertEquals(1, list.size(), "Некорректное количество задач");
        assertEquals("N-T0", list.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    void tasksGetTaskByIdCode200()  throws IOException, InterruptedException {
        Task task = new Task("N-T0", "D-T0",
                Status.NEW, LocalDateTime.now(), Duration.ofMinutes(5));
        tm.addNewTask(task);
        // конвертируем её в JSON
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Task.class, new TaskConverter())
                .create();
        String taskJson = gson.toJson(task);
        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/0");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());
        Task taskfromJson= gson.fromJson(response.body(), Task.class);
        // проверяем, что создалась одна задача с корректным именем

        assertNotNull(taskfromJson, "Задача не возвращается");
        assertTrue( taskfromJson.getClass().toString().equals("class model.Task"), "Некорректный тип класса");
        assertEquals("N-T0", taskfromJson.getName(), "Некорректное имя задачи");
    }

    @Test
    void tasksGetTaskByIdCode404()  throws IOException, InterruptedException {
        Task task = new Task("N-T0", "D-T0",
                Status.NEW, LocalDateTime.now(), Duration.ofMinutes(5));
        tm.addNewTask(task);
        // конвертируем её в JSON
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Task.class, new TaskConverter())
                .create();
        String taskJson = gson.toJson(task);
        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/7");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(404, response.statusCode());
        assertTrue( response.body().equals("Task с идентификатором 7 не найден"), "Некорректный тип класса");
    }

    @Test
    void tasksGetTaskByIdCode404NoNumber()  throws IOException, InterruptedException {
        Task task = new Task("N-T0", "D-T0",
                Status.NEW, LocalDateTime.now(), Duration.ofMinutes(5));
        tm.addNewTask(task);
        // конвертируем её в JSON
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Task.class, new TaskConverter())
                .create();
        String taskJson = gson.toJson(task);
        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/aaa");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(404, response.statusCode());
        assertTrue( response.body().equals("Некорректный идентификатор id = aaa"), "Некорректный тип класса");
    }

    @Test
    void tasksGetTaskByIdException_Z()  throws IOException, InterruptedException {

    }

    @Test
    void tasksDeleteTaskByIdCode200()  throws IOException, InterruptedException {
        Task task = new Task("N-T0", "D-T0",
                Status.NEW, LocalDateTime.now(), Duration.ofMinutes(5));
        tm.addNewTask(task);
        // конвертируем её в JSON
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Task.class, new TaskConverter())
                .create();
        String taskJson = gson.toJson(task);
        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/0");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());
        assertTrue( response.body().equals("Task с идентификатором 0 удален"), "Некорректный тип класса");

    }

    @Test
    void tasksDeleteTaskByIdCode404()  throws IOException, InterruptedException {
        Task task = new Task("N-T0", "D-T0",
                Status.NEW, LocalDateTime.now(), Duration.ofMinutes(5));
        tm.addNewTask(task);
        // конвертируем её в JSON
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Task.class, new TaskConverter())
                .create();
        String taskJson = gson.toJson(task);
        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(404, response.statusCode());
        assertTrue( response.body().equals("Task с идентификатором 1 не найден"), "Некорректный тип класса");
    }

    class TaskListTypeToken extends TypeToken<List<Task>> {
    }
}