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
    void testManagersTest() {
        TaskManager taskManager = Managers.getDefault();
        Managers.testManagers(taskManager);

        List<Task> listTask = taskManager.getListTask();
        assertNotNull(listTask, "Задачи не возвращаются.");
        assertEquals(2, listTask.size(), "Неверное количество задач.");

        List<Task> listHistory = taskManager.getHistory();
        assertEquals(7, listHistory.size(), "Неверное количество задач.");
        listHistory.get(5).setName("S3 ++++");
        listHistory = taskManager.getHistory();
        assertNotNull(listHistory, "Задачи не возвращаются.");
        assertEquals(7, listHistory.size(), "Неверное количество задач.");
        assertEquals("name S6", listHistory.get(5).getName(), "Неверное количество задач.");
    }
}