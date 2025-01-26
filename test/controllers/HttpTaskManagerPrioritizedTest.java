package controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import http.adapter.EpicConverter;
import http.adapter.SubtaskConverter;
import http.adapter.TaskConverter;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HttpTaskManagerPrioritizedTest {
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
    void prioritizedGetListPrioritizedCode200()  throws IOException, InterruptedException {
        LocalDateTime start = LocalDateTime.of(2025,1,24,8,0);
        Task task = new Task("N-T0", "D-T0", Status.NEW, start, Duration.ofMinutes(10));
        tm.addNewTask(task);
        task = new Task("N-T1", "D-T1",Status.NEW, start.plusMinutes(5), Duration.ofMinutes(10));
        tm.addNewTask(task);
        Epic epic = new Epic("N-E2","D-E2");
        int epicId = tm.addNewEpic(epic);
        Subtask subtask = new Subtask("N-S3","D-S3",Status.DONE,start.minusMinutes(15),Duration.ofMinutes(10));
        subtask.setEpicId(epicId);
        tm.addNewSubtask(subtask);
        subtask = new Subtask("N-S4","D-S4",Status.DONE,start.plusMinutes(20),Duration.ofMinutes(10));
        subtask.setEpicId(epicId);
        tm.addNewSubtask(subtask);
        // конвертируем её в JSON
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Task.class, new TaskConverter())
                .registerTypeAdapter(Subtask.class, new SubtaskConverter())
                .registerTypeAdapter(Epic.class, new EpicConverter())
                .create();
        String taskJson = gson.toJson(task);
        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());
        List<Task> list= gson.fromJson(response.body(),new TaskListTypeToken().getType());
        // проверяем, что создалась одна задача с корректным именем

        assertNotNull(list, "Задачи не возвращаются");
        assertEquals(3, list.size(), "Некорректное количество задач");
        assertEquals("N-S3", list.get(0).getName(), "Некорректное имя задачи");
        assertEquals("N-T0", list.get(1).getName(), "Некорректное имя задачи");
        assertEquals("N-S4", list.get(2).getName(), "Некорректное имя задачи");
    }

    class TaskListTypeToken extends TypeToken<List<Task>> {
    }
}
