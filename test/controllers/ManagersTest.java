package controllers;

import model.Task;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    void getDefault() {
        TaskManager taskManager = Managers.getDefault();
        assertNotNull(taskManager, "Задачи не возвращаются.");
    }

    @Test
    void getDefaultHistory() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        assertNotNull(historyManager, "Задачи не возвращаются.");
    }

    @Test
    void testManagersTest(){
        Managers.testManagers();
        TaskManager taskManager = Managers.getDefault();
        List<Task> listTask = taskManager.getListTask();
        assertNotNull(listTask, "Задачи не возвращаются.");
        assertEquals(2, listTask.size(), "Неверное количество задач.");
        HistoryManager historyManager = Managers.getDefaultHistory();
        List<Task> listHistory = historyManager.getHistory();
        assertNotNull(listHistory, "Задачи не возвращаются.");
        assertEquals(taskManager.getHistory().size(), listHistory.size(), "Неверное количество задач.");
    }
}