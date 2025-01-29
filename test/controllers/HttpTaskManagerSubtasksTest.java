package controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
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

public class HttpTaskManagerSubtasksTest {
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

        gson = new GsonBuilder().registerTypeAdapter(Subtask.class, new SubtaskConverter()).create();
    }

    @Test
    void subtasksDeleteSubtaskByIdCode200()  throws IOException, InterruptedException {
        LocalDateTime start = LocalDateTime.of(2025,1,24,8,0);
        Subtask subtask = new Subtask("N-S0", "D-S0", Status.NEW, start, Duration.ofMinutes(10));
        tm.addNewSubtask(subtask);
        assertEquals(1, tm.getListSubtask().size(), "Некорректное количество задач");
        assertEquals("N-S0", tm.getListSubtask().get(0).getName(), "Некорректное имя задачи");
        // конвертируем её в JSON

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/0");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .DELETE().build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());

        // проверяем, что задача удалена
        List<Subtask> subtasksFromManager = tm.getListSubtask();
        assertNotNull(subtasksFromManager, "Задачи возвращаются");
        assertEquals(0, subtasksFromManager.size(), "Некорректное количество задач");
    }

    @Test
    void subtasksDeleteSubtaskByIdCode404()  throws IOException, InterruptedException {
        LocalDateTime start = LocalDateTime.of(2025,1,24,8,0);
        Subtask subtask = new Subtask("N-S0", "D-S0", Status.NEW, start, Duration.ofMinutes(10));
        tm.addNewSubtask(subtask);
        assertEquals(1, tm.getListSubtask().size(), "Некорректное количество задач");
        assertEquals("N-S0", tm.getListSubtask().get(0).getName(), "Некорректное имя задачи");
        // конвертируем её в JSON

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .DELETE().build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(404, response.statusCode());

        // проверяем, что задача удалена
        List<Subtask> subtasksFromManager = tm.getListSubtask();
        assertNotNull(subtasksFromManager, "Задачи возвращаются");
        assertEquals(1, subtasksFromManager.size(), "Некорректное количество задач");
        assertEquals("N-S0", subtasksFromManager.get(0).getName(), "Некорректное имя задачи");
        //client.close();
    }

    @Test
    void subtasksGetListSubtaskCode200()  throws IOException, InterruptedException {
        LocalDateTime start = LocalDateTime.of(2025,1,24,8,0);
        Subtask subtask = new Subtask("N-S0", "D-S0", Status.NEW, start, Duration.ofMinutes(10));
        tm.addNewSubtask(subtask);
        subtask = new Subtask("N-S1", "D-S1", Status.NEW, start.plusMinutes(15), Duration.ofMinutes(10));
        tm.addNewSubtask(subtask);
        assertEquals(2, tm.getListSubtask().size(), "Некорректное количество задач");
        assertEquals("N-S0", tm.getListSubtask().get(0).getName(), "Некорректное имя задачи");
        // конвертируем её в JSON

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .GET().build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());
        List<Subtask> list= gson.fromJson(response.body(),new SubtaskListTypeToken().getType());
        // проверяем, что задача удалена
        assertNotNull(list, "Задачи не возвращаются");
        assertEquals(2, list.size(), "Некорректное количество задач");
        assertEquals("N-S0", list.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    void subtasksGetSubtaskById_Code200()  throws IOException, InterruptedException {
        LocalDateTime start = LocalDateTime.of(2025,1,24,8,0);
        Subtask subtask = new Subtask("N-S0", "D-S0", Status.NEW, start, Duration.ofMinutes(10));
        tm.addNewSubtask(subtask);

        assertEquals(1, tm.getListSubtask().size(), "Некорректное количество задач");
        assertEquals("N-S0", tm.getListSubtask().get(0).getName(), "Некорректное имя задачи");
        // конвертируем её в JSON

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/0");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .GET().build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());
        Subtask sutaskFromJson = gson.fromJson(response.body(), Subtask.class);
        // проверяем, что задача удалена
        assertNotNull(sutaskFromJson, "Задача не возвращаются");
        assertEquals(0, sutaskFromJson.getId(), "Некорректное id задач");
        assertEquals("N-S0", sutaskFromJson.getName(), "Некорректное имя задачи");
        //client.close();
    }

    @Test
    void subtasksGetSubtaskById_Code404()  throws IOException, InterruptedException {
        LocalDateTime start = LocalDateTime.of(2025,1,24,8,0);
        Subtask subtask = new Subtask("N-S0", "D-S0", Status.NEW, start, Duration.ofMinutes(10));
        tm.addNewSubtask(subtask);

        assertEquals(1, tm.getListSubtask().size(), "Некорректное количество задач");
        assertEquals("N-S0", tm.getListSubtask().get(0).getName(), "Некорректное имя задачи");
        // конвертируем её в JSON

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/7");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .GET().build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(404, response.statusCode());

        // проверяем, что задача не возвращается
        assertNotNull(response.body(), "Сообщение об ошибке не возвращается");
        assertEquals("Subtask с идентификатором 7 не найден", response.body());
        //client.close();
    }

    @Test
    void subtasksGetSubtaskById_Exception()  throws IOException, InterruptedException {
        Subtask subtask = new Subtask("N-S0", "D-S0",Status.NEW);
        tm.addNewSubtask(subtask);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/0");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(500, response.statusCode());
        assertEquals("Произошла ошибка при обработке запроса", response.body());
    }

    @Test
    void subtasksPostAddNewSubtaskCode201()  throws IOException, InterruptedException {
        LocalDateTime start = LocalDateTime.of(2025,1,24,8,0);
        Subtask subtask = new Subtask("N-S0", "D-S0", Status.NEW, start, Duration.ofMinutes(10));

        // конвертируем её в JSON

        String subtaskJson = gson.toJson(subtask);
        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson)).build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(201, response.statusCode());

        // проверяем, что создалась одна задача
        List<Subtask> subtasksFromManager = tm.getListSubtask();

        assertNotNull(subtasksFromManager, "Задачи не возвращаются");
        assertEquals(1, subtasksFromManager.size(), "Некорректное количество задач");
        assertEquals("N-S0", subtasksFromManager.get(0).getName(), "Некорректное имя задачи");
        assertEquals("Новый Subtask добавлен, id = 0", response.body());
    }

    @Test
    void subtasksPostAddNewSubtaskCode406()  throws IOException, InterruptedException {
        LocalDateTime start = LocalDateTime.of(2025,1,24,8,0);
        Subtask subtask = new Subtask("N-S0", "D-S0", Status.NEW, start, Duration.ofMinutes(10));
        tm.addNewSubtask(subtask);
        subtask = new Subtask("N-S1", "D-S1", Status.NEW, start.minusMinutes(5), Duration.ofMinutes(10));

        // конвертируем её в JSON
        String subtaskJson = gson.toJson(subtask);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson)).build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(406, response.statusCode());

        // проверяем, что создалась одна задача
        List<Subtask> subtasksFromManager = tm.getListSubtask();

        assertNotNull(subtasksFromManager, "Задачи не возвращаются");
        assertEquals(1, subtasksFromManager.size(), "Некорректное количество задач");
        assertEquals("N-S0", subtasksFromManager.get(0).getName(), "Некорректное имя задачи");
        assertEquals("Subtask пересекает задачу, действие addNewSubtask() прервано!", response.body());
        //client.close();
    }

    @Test
    void subtasksPostAddNewSubtaskException()  throws IOException, InterruptedException {
        String taskJson = "{\"type\":\"SUBTASK\",\"name\":\"N-S0\",\"description\":\"D-S0\"," +
                "\"status\":\"NEW\"}";

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(500, response.statusCode());

        // проверяем, что создалась одна задача с корректным именем
        List<Subtask> subtasksFromManager = tm.getListSubtask();

        assertNotNull(subtasksFromManager, "Задачи не возвращаются");
        assertEquals(0, subtasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Произошла ошибка при обработке запроса", response.body());
    }

    @Test
    void subtasksPostUpdateSubtaskCode201()  throws IOException, InterruptedException {
        LocalDateTime start = LocalDateTime.of(2025,1,24,8,0);
        int epicId = tm.addNewEpic(new Epic("N-E0","D-E0"));
        Subtask subtask = new Subtask("N-S1", "D-S1", Status.NEW, start, Duration.ofMinutes(10));
        subtask.setEpicId(epicId);
        int idSubtask = tm.addNewSubtask(subtask);
        assertEquals(1, tm.getListSubtask().size(), "Некорректное количество задач");
        assertEquals("N-S1", tm.getListSubtask().get(0).getName(), "Некорректное имя задачи");
        assertEquals("PT10M", tm.getListSubtask().get(0).getDuration().toString(), "Некорректное значение duration");
        // конвертируем её в JSON
        subtask = new Subtask("N-S1 update", "D-S1", idSubtask,Status.NEW,epicId,
                start, Duration.ofMinutes(20));
        String taskJson = gson.toJson(subtask);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(201, response.statusCode());

        // проверяем, что обновилась одна задача
        assertEquals("Subtask c id = 1 обновлен", response.body());
        List<Subtask> subtasksFromManager = tm.getListSubtask();
        assertNotNull(subtasksFromManager, "Задачи не возвращаются");
        assertEquals(1, subtasksFromManager.size(), "Некорректное количество задач");
        assertEquals(1, subtasksFromManager.get(0).getId(), "Некорректное значение id");
        assertEquals("N-S1 update", subtasksFromManager.get(0).getName(), "Некорректное имя задачи");
        assertEquals("PT20M", subtasksFromManager.get(0).getDuration().toString(),
                "Некорректное значение duration");
        //client.close();
    }

    @Test
    void subtasksPostUpdateSubtaskCode406()  throws IOException, InterruptedException {
        LocalDateTime start = LocalDateTime.of(2025,1,24,8,0);
        int epicId = tm.addNewEpic(new Epic("N-E0","D-E0"));
        Subtask subtask = new Subtask("N-S1", "D-S1", Status.NEW, start, Duration.ofMinutes(10));
        subtask.setEpicId(epicId);
        tm.addNewSubtask(subtask);
        subtask = new Subtask("N-S2", "D-S2", Status.NEW, start.plusMinutes(20), Duration.ofMinutes(10));
        subtask.setEpicId(epicId);
        int idSubtask = tm.addNewSubtask(subtask);

        assertEquals(2, tm.getListSubtask().size(), "Некорректное количество задач");
        assertEquals("N-S1", tm.getListSubtask().get(0).getName(), "Некорректное имя задачи");
        assertEquals("2025-01-24T08:00", tm.getListSubtask().get(0).getStartTime().toString(),
                "Некорректное значение начало задачи");
        assertEquals("N-S2", tm.getListSubtask().get(1).getName(), "Некорректное имя задачи");
        // конвертируем её в JSON
        subtask = new Subtask("N-S2 update", "D-S2", idSubtask,Status.NEW,epicId,
                start.plusMinutes(5), Duration.ofMinutes(10));
        String taskJson = gson.toJson(subtask);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(406, response.statusCode());

        // проверяем, что обновилась одна задача
        List<Subtask> subtasksFromManager = tm.getListSubtask();
        assertNotNull(subtasksFromManager, "Задачи не возвращаются");
        assertEquals(2, subtasksFromManager.size(), "Некорректное количество задач");
        assertEquals(2, subtasksFromManager.get(1).getId(), "Некорректное значение id");
        assertEquals("N-S2", subtasksFromManager.get(1).getName(), "Некорректное имя задачи");
        assertEquals("2025-01-24T08:20", subtasksFromManager.get(1).getStartTime().toString(),
                "Некорректное значение duration");
        assertEquals("Subtask пересекает задачу, действие updateSubtask() прервано!", response.body());
    }

    @Test
    void subtasksPostUpdateSubtaskException()  throws IOException, InterruptedException {
        LocalDateTime start = LocalDateTime.of(2024,10,10,8,0,0);
        Subtask subtask = new Subtask("N-S0", "D-S0",Status.NEW, start, Duration.ofMinutes(10));
        tm.addNewSubtask(subtask);
        // конвертируем её в JSON
        String taskJson = "{\"type\":\"SUBTASK\",\"id\":0,\"name\":\"N-S0 ++\",\"description\":\"D-S0\"}";

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(500, response.statusCode());

        // проверяем, что создалась одна задача с корректным именем
        List<Subtask> subtasksFromManager = tm.getListSubtask();

        assertNotNull(subtasksFromManager, "Задачи не возвращаются");
        assertEquals(1, subtasksFromManager.size(), "Некорректное количество задач");
        assertEquals("N-S0",subtasksFromManager.get(0).getName(), "Некорректное имя задачи");
        assertEquals(0, subtasksFromManager.get(0).getId(), "Некорректное значение id");
        assertEquals("Произошла ошибка при обработке запроса", response.body());
    }
    //--------------------------------------------------------------------
    class SubtaskListTypeToken extends TypeToken<List<Subtask>> {
    }
}
