package controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import http.adapter.EpicConverter;
import http.adapter.SubtaskConverter;
import model.Epic;
import model.Subtask;
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

public class HttpTaskManagerEpicsTest {
    private HttpTaskServer taskServer;
    private TaskManager tm;

    private Gson gson;

    @AfterEach
    void tearDown() {
        taskServer.stop();
    }

    @BeforeEach
    void init() throws IOException {
        tm = Managers.getDefault();
        taskServer = new HttpTaskServer(tm);
        taskServer.start();

        gson = new GsonBuilder()
                .registerTypeAdapter(Epic.class, new EpicConverter())
                .registerTypeAdapter(Subtask.class, new SubtaskConverter())
                .create();
    }

    @Test
    void epicsDeleteEpicByIdCode200()  throws IOException, InterruptedException {
        LocalDateTime start = LocalDateTime.of(2025,1,24,8,0);
        int epicId = tm.addNewEpic(new Epic("N-E0","D-E0"));
        Subtask subtask = new Subtask("N-S1", "D-S1", Status.NEW, start, Duration.ofMinutes(10));
        subtask.setEpicId(epicId);
        tm.addNewSubtask(subtask);

        assertEquals(1, tm.getListEpic().size(), "Некорректное количество Epic");
        assertEquals("N-E0", tm.getListEpic().get(0).getName(), "Некорректное имя Epic");
        // конвертируем её в JSON

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/0");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .DELETE().build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());

        List<Epic> epicsFromManager = tm.getListEpic();
        // проверяем, что задача удалена
        assertNotNull(epicsFromManager, "Задачи возвращаются");
        assertEquals(0, epicsFromManager.size(), "Некорректное количество задач");
        assertEquals("Epic с идентификатором 0 удален", response.body());
    }

    @Test
    void epicsDeleteEpicByIdCode404()  throws IOException, InterruptedException {
        LocalDateTime start = LocalDateTime.of(2025,1,24,8,0);
        int epicId = tm.addNewEpic(new Epic("N-E0","D-E0"));
        Subtask subtask = new Subtask("N-S1", "D-S1", Status.NEW, start, Duration.ofMinutes(10));
        subtask.setEpicId(epicId);
        tm.addNewSubtask(subtask);

        assertEquals(1, tm.getListEpic().size(), "Некорректное количество Epic");
        assertEquals("N-E0", tm.getListEpic().get(0).getName(), "Некорректное имя Epic");
        // конвертируем её в JSON

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/7");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .DELETE().build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(404, response.statusCode());

        List<Epic> epicsFromManager = tm.getListEpic();
        // проверяем, что задача удалена
        assertNotNull(epicsFromManager, "Задачи возвращаются");
        assertEquals(1, epicsFromManager.size(), "Некорректное количество задач");
        assertEquals("Epic с идентификатором 7 не найден", response.body());
    }

    @Test
    void epicsGetListEpicCode200()  throws IOException, InterruptedException {
        LocalDateTime start = LocalDateTime.of(2025,1,24,8,0);
        int epicId = tm.addNewEpic(new Epic("N-E0","D-E0"));
        Subtask subtask = new Subtask("N-S1", "D-S1", Status.NEW, start, Duration.ofMinutes(10));
        subtask.setEpicId(epicId);
        tm.addNewSubtask(subtask);

        assertEquals(1, tm.getListEpic().size(), "Некорректное количество задач");
        assertEquals("N-E0", tm.getListEpic().get(0).getName(), "Некорректное имя задачи");
        // конвертируем её в JSON

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .GET().build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());
        List<Epic> list= gson.fromJson(response.body(),new EpicListTypeToken().getType());
        // проверяем, что задача удалена
        assertNotNull(list, "Задачи не возвращаются");
        assertEquals(1, list.size(), "Некорректное количество задач");
        assertEquals("N-E0", list.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    void epicsGetEpicByIdCode200()  throws IOException, InterruptedException {
        LocalDateTime start = LocalDateTime.of(2025,1,24,8,0);
        int epicId = tm.addNewEpic(new Epic("N-E0","D-E0"));
        Subtask subtask = new Subtask("N-S1", "D-S1", Status.NEW, start, Duration.ofMinutes(10));
        subtask.setEpicId(epicId);
        tm.addNewSubtask(subtask);

        assertEquals(1, tm.getListEpic().size(), "Некорректное количество задач");
        assertEquals("N-E0", tm.getListEpic().get(0).getName(), "Некорректное имя задачи");
        // конвертируем её в JSON

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/0");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .GET().build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());
        Epic epicFromJson = gson.fromJson(response.body(), Epic.class);
        // проверяем, что задача удалена
        assertNotNull(epicFromJson, "Задачи не возвращаются");
        assertEquals(0, epicFromJson.getId(), "Некорректное количество задач");
        assertEquals("N-E0", epicFromJson.getName(), "Некорректное имя задачи");
    }

    @Test
    void epicsGetEpicByIdCode404()  throws IOException, InterruptedException {
        LocalDateTime start = LocalDateTime.of(2025,1,24,8,0);
        int epicId = tm.addNewEpic(new Epic("N-E0","D-E0"));
        Subtask subtask = new Subtask("N-S1", "D-S1", Status.NEW, start, Duration.ofMinutes(10));
        subtask.setEpicId(epicId);
        tm.addNewSubtask(subtask);

        assertEquals(1, tm.getListEpic().size(), "Некорректное количество задач");
        assertEquals("N-E0", tm.getListEpic().get(0).getName(), "Некорректное имя задачи");
        // конвертируем её в JSON

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/7");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .GET().build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(404, response.statusCode());

        // проверяем, что задача удалена
        Epic epicsFromManager = tm.getEpicById(7);
        assertNull(epicsFromManager, "Задачи не возвращаются");
        assertEquals("Epic с идентификатором 7 не найден", response.body());
    }

    @Test
    void epicsGetEpicByIdException()  throws IOException, InterruptedException {
        Epic epic = new Epic();
        tm.addNewEpic(epic);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/0");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(500, response.statusCode());
        assertEquals("Произошла ошибка при обработке запроса", response.body());
    }

    @Test
    void epicsGetEpicByIdSubtasksCode200()  throws IOException, InterruptedException {
        LocalDateTime start = LocalDateTime.of(2025,1,24,8,0);
        int epicId = tm.addNewEpic(new Epic("N-E0","D-E0"));
        Subtask subtask = new Subtask("N-S1", "D-S1", Status.NEW, start, Duration.ofMinutes(10));
        subtask.setEpicId(epicId);
        tm.addNewSubtask(subtask);

        assertEquals(1, tm.getEpicSubtasks(0).size(), "Некорректное количество задач");
        assertEquals("N-S1", tm.getEpicSubtasks(0).get(0).getName(), "Некорректное имя задачи");
        // конвертируем её в JSON

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/0/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .GET().build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());

        List<Subtask> listSubtask = gson.fromJson(response.body(), new SubtaskListTypeToken().getType());
        // проверяем, что список есть
        assertNotNull(listSubtask, "Задачи не возвращаются");
        assertEquals(1, listSubtask.size(), "Некорректное количество задач");
        assertEquals("N-S1", listSubtask.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    void epicsGetEpicByIdSubtasksCode404()  throws IOException, InterruptedException {
        LocalDateTime start = LocalDateTime.of(2025,1,24,8,0);
        int epicId = tm.addNewEpic(new Epic("N-E0","D-E0"));
        Subtask subtask = new Subtask("N-S1", "D-S1", Status.NEW, start, Duration.ofMinutes(10));
        subtask.setEpicId(epicId);
        tm.addNewSubtask(subtask);

        assertEquals(1, tm.getEpicSubtasks(0).size(), "Некорректное количество задач");
        assertEquals("N-S1", tm.getEpicSubtasks(0).get(0).getName(), "Некорректное имя задачи");
        // конвертируем её в JSON

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/7/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .GET().build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(404, response.statusCode());

        // проверяем, что список есть
        Epic epicFromManager = tm.getEpicById(7);
        assertNull(epicFromManager, "Epic не null");
        assertEquals("Epic с идентификатором 7 не найден", response.body());
    }

    @Test
    void epicsPostAddNewEpicCode201()  throws IOException, InterruptedException {
        LocalDateTime start = LocalDateTime.of(2025,1,24,8,0);
        Epic epic = new Epic("N-E0","D-E0");

        // конвертируем её в JSON
        String epicJson = gson.toJson(epic);
        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(201, response.statusCode());

        // проверяем, что создалась одна задача
        List<Epic> epicFromManager = tm.getListEpic();

        assertNotNull(epicFromManager, "Задачи не возвращаются");
        assertEquals(1, epicFromManager.size(), "Некорректное количество задач");
        assertEquals("N-E0", epicFromManager.get(0).getName(), "Некорректное имя задачи");
        assertEquals("Новый Epic добавлен, id = 0", response.body());
    }

    @Test
    void epicsPostAddNewEpicCode410()  throws IOException, InterruptedException {
        LocalDateTime start = LocalDateTime.of(2025,1,24,8,0);
        Epic epic = new Epic("N-E0","D-E0");


        // конвертируем её в JSON
        String epicJson = gson.toJson(epic);
        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(201, response.statusCode());

        // проверяем, что создалась одна задача
        List<Epic> epicFromManager = tm.getListEpic();

        assertNotNull(epicFromManager, "Задачи не возвращаются");
        assertEquals(1, epicFromManager.size(), "Некорректное количество задач");
        assertEquals("N-E0", epicFromManager.get(0).getName(), "Некорректное имя задачи");
        assertEquals("Новый Epic добавлен, id = 0", response.body());
    }

    @Test
    void epicsPostAddNewEpicException()  throws IOException, InterruptedException {
        String taskJson = "{\"type\":\"EPIC\",\"description\":\"D-E0\"," +
                "\"status\":\"NEW\"}";

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(500, response.statusCode());

        // проверяем, что создалась одна задача с корректным именем
        List<Epic> epicsFromManager = tm.getListEpic();

        assertNotNull(epicsFromManager, "Задачи не возвращаются");
        assertEquals(0, epicsFromManager.size(), "Некорректное количество задач");
        assertEquals("Произошла ошибка при обработке запроса", response.body());
    }

    @Test
    void epicsPostUpdateEpicCode201()  throws IOException, InterruptedException {
        LocalDateTime start = LocalDateTime.of(2025,1,24,8,0);
        int epicId = tm.addNewEpic(new Epic("N-E0","D-E0"));
        Subtask subtask = new Subtask("N-S1", "D-S1", Status.NEW, start, Duration.ofMinutes(10));
        subtask.setEpicId(epicId);
        tm.addNewSubtask(subtask);

        Epic epic = tm.getEpicById(0);
        epic.setName("N-E0 update");

        // конвертируем её в JSON
        String epicJson = gson.toJson(epic);
        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(201, response.statusCode());

        // проверяем, что создалась одна задача
        Epic epicFromManager = tm.getEpicById(0);

        assertNotNull(epicFromManager, "Epic не null");
        assertEquals(0, epicFromManager.getId(), "Некорректное количество задач");
        assertEquals("N-E0 update", epicFromManager.getName(), "Некорректное имя задачи");
        assertEquals("Epic c id = 0 обновлен", response.body());
    }

    @Test
    void epicsPostUpdateEpicCode404()  throws IOException, InterruptedException {
        LocalDateTime start = LocalDateTime.of(2025,1,24,8,0);
        int epicId = tm.addNewEpic(new Epic("N-E0","D-E0"));
        Subtask subtask = new Subtask("N-S1", "D-S1", Status.NEW, start, Duration.ofMinutes(10));
        subtask.setEpicId(epicId);
        tm.addNewSubtask(subtask);

        Epic epic = tm.getEpicById(0);
        epic.setName("N-E0 update");
        epic.setId(7);

        // конвертируем её в JSON
        String epicJson = gson.toJson(epic);
        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(404, response.statusCode());

        // проверяем, что создалась одна задача
        Epic epicFromManager = tm.getEpicById(7);

        assertNull(epicFromManager, "Epic не null");
        assertEquals("Epic с идентификатором 7 не найден", response.body());
    }

    @Test
    void epicsPostUpdateEpicException()  throws IOException, InterruptedException {
        LocalDateTime start = LocalDateTime.of(2024,10,10,8,0,0);
        Epic epic = new Epic("N-E0", "D-E)");
        int epicId = tm.addNewEpic(epic);
        Subtask subtask = new Subtask("N-S1", "D-S1",Status.NEW, start, Duration.ofMinutes(10));
        subtask.setEpicId(epicId);
        tm.addNewSubtask(subtask);
        // конвертируем её в JSON
        String taskJson = "{\"type\":\"EPIC\",\"id\":0,\"name\":\"N-E0 ++\"}";

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(500, response.statusCode());

        // проверяем, что создалась одна задача с корректным именем
        List<Epic> epicsFromManager = tm.getListEpic();

        assertNotNull(epicsFromManager, "Задачи не возвращаются");
        assertEquals(1, epicsFromManager.size(), "Некорректное количество задач");
        assertEquals("N-E0",epicsFromManager.get(0).getName(), "Некорректное имя задачи");
        assertEquals(0, epicsFromManager.get(0).getId(), "Некорректное значение id");
        assertEquals("Произошла ошибка при обработке запроса", response.body());
    }
    //--------------------------------------------------------------------
    class EpicListTypeToken extends TypeToken<List<Epic>> {
    }

    class SubtaskListTypeToken extends TypeToken<List<Subtask>> {
    }
}
