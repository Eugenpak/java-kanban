package controllers;

import model.Epic;
import model.Subtask;
import org.junit.jupiter.api.Test;
import service.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;

    @Test
    void updateStatusEpicSprint8Test1a() {
        LocalDateTime startTime = LocalDateTime.of(2024,10,10,8,0,0);
        Duration duration = Duration.ofMinutes(10);
        Epic epic = new Epic("N-E0","D-E0");
        int epicId = taskManager.addNewEpic(epic);
        for (int i = 1; i<=5;i++) {
            Subtask subtask = new Subtask("N-S"+i,"D-S"+i, Status.NEW,startTime.plusMinutes(i*10),duration);
            subtask.setEpicId(epicId);
            taskManager.addNewSubtask(subtask);
        }
        assertEquals(Status.NEW,epic.getStatus(), "Статус не одинаковый!");
        assertEquals(6,taskManager.getHistory().size(), "Статус не одинаковый!");
        Epic findedEpic = taskManager.getEpicById(0);
        assertTrue(findedEpic.toString().contains("Epic{Id=0, name='N-E0', description='D-E0', status=NEW,"));
    }

    @Test
    void updateStatusEpicSprint8Test1b() {
        LocalDateTime startTime = LocalDateTime.of(2024,10,10,8,0,0);
        Duration duration = Duration.ofMinutes(10);
        Epic epic = new Epic("N-E0","D-E0");
        int epicId = taskManager.addNewEpic(epic);
        for (int i = 1; i<=5;i++) {
            Subtask subtask = new Subtask("N-S"+i,"D-S"+i, Status.DONE,startTime.plusMinutes(i*10),duration);
            subtask.setEpicId(epicId);
            taskManager.addNewSubtask(subtask);
        }
        assertEquals(Status.DONE,epic.getStatus(), "Статус не одинаковый!");
        assertEquals(6,taskManager.getHistory().size(), "Статус не одинаковый!");
        Epic findedEpic = taskManager.getEpicById(0);
        assertTrue(findedEpic.toString().contains("Epic{Id=0, name='N-E0', description='D-E0', status=DONE,"));
    }

    @Test
    void updateStatusEpicSprint8Test1c() {
        LocalDateTime startTime = LocalDateTime.of(2024,10,10,8,0,0);
        Duration duration = Duration.ofMinutes(10);
        Epic epic = new Epic("N-E0","D-E0");
        int epicId = taskManager.addNewEpic(epic);
        for (int i = 1; i<=5;i++) {
            Status t;
            if (i%2 == 0) t = Status.NEW;
            else t = Status.DONE;
            Subtask subtask = new Subtask("N-S"+i,"D-S"+i, t,startTime.plusMinutes(i*10),duration);
            subtask.setEpicId(epicId);
            taskManager.addNewSubtask(subtask);
        }
        assertEquals(Status.IN_PROGRESS,epic.getStatus(), "Статус не одинаковый!");
        assertEquals(6,taskManager.getHistory().size(), "Статус не одинаковый!");
        Epic findedEpic = taskManager.getEpicById(0);
        assertTrue(findedEpic.toString().contains("Epic{Id=0, name='N-E0', description='D-E0', status=IN_PROGRESS,"));
    }

    @Test
    void updateStatusEpicSprint8Test1d() {
        LocalDateTime startTime = LocalDateTime.of(2024,10,10,8,0,0);
        Duration duration = Duration.ofMinutes(10);
        Epic epic = new Epic("N-E0","D-E0");
        int epicId = taskManager.addNewEpic(epic);
        for (int i = 1; i<=5;i++) {
            Subtask subtask = new Subtask("N-S"+i,"D-S"+i, Status.IN_PROGRESS,startTime.plusMinutes(i*10),duration);
            subtask.setEpicId(epicId);
            taskManager.addNewSubtask(subtask);
        }
        assertEquals(Status.IN_PROGRESS,epic.getStatus(), "Статус не одинаковый!");
        assertEquals(6,taskManager.getHistory().size(), "Статус не одинаковый!");
        Epic findedEpic = taskManager.getEpicById(0);
        assertTrue(findedEpic.toString().contains("Epic{Id=0, name='N-E0', description='D-E0', status=IN_PROGRESS,"));
    }

    @Test
    void checkSubtaskEpicIdSprint8Test2b() {
        LocalDateTime startTime = LocalDateTime.of(2024,10,10,8,0,0);
        Duration duration = Duration.ofMinutes(10);
        Epic epic = new Epic("N-E0","D-E0");
        int epicId = taskManager.addNewEpic(epic);
        Subtask subtask = new Subtask("N-S","D-S", Status.NEW,startTime,duration);
        subtask.setEpicId(epicId);
        taskManager.addNewSubtask(subtask);

        Epic findedEpic = taskManager.getEpicById(subtask.getEpicId());
        assertEquals(Status.NEW,findedEpic.getStatus(), "Статус не одинаковый!");
        assertEquals(0,findedEpic.getId(), "id Epic не одинаковый!");
    }
}
