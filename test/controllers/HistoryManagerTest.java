package controllers;

import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;
import service.Status;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {

    @Test
    void add() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        Task task = new Task("", "", 1, Status.NEW);
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

    @Test
    void getHistory() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        assertEquals(0, historyManager.getHistory().size(), "Список не пустой");

        for (int i = 0; i < 3; i++) {
            historyManager.add(new Task("", "", i, Status.NEW));
        }
        historyManager.add(new Epic("", "", 4, Status.NEW));

        assertNotNull(historyManager, "История не пустая.");
        assertEquals(4, historyManager.getHistory().size(), "Список не пустой");
        assertEquals(1, historyManager.getHistory().get(1).getId(), "Список не пустой");
        assertEquals("class model.Task", historyManager.getHistory().get(1).getClass().toString());
    }

    @Test
    void remove() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        assertEquals(0, historyManager.getHistory().size(), "Список не пустой");

        Stream.of(0,1,2,3,4)
                .map(num -> new Task("N-T" + num, "D-T" + num, num, Status.NEW))
                .forEach(historyManager::add);
        historyManager.add(new Epic("N-E3", "D-E3", 5, Status.NEW));

        assertNotNull(historyManager, "История не пустая.");
        assertEquals(6, historyManager.getHistory().size(), "Список не пустой");
        historyManager.remove(0);

        assertNotNull(historyManager, "История не пустая.");
        assertEquals(5, historyManager.getHistory().size(), "Список не пустой");
    }

    @Test
    void getHistoryIsEmptySprint8Test3a() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        assertNotNull(historyManager, "История не пустая.");
        assertEquals(0, historyManager.getHistory().size(), "Список не пустой");
    }

    @Test
    void getHistoryDuplicateSprint8Test3b() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        assertNotNull(historyManager, "История не пустая.");
        assertEquals(0, historyManager.getHistory().size(), "Список не пустой");
        Task task = new Task("N-T0", "D-T0", Status.NEW);
        historyManager.add(task);
        task.setName("N-T0 after update");
        historyManager.add(task);
        task.setDuration(Duration.ZERO);
        historyManager.add(task);
        assertEquals(1, historyManager.getHistory().size(), "Список не пустой");
        String str = "Task{Id=-3, name='N-T0 after update', description='D-T0', status=NEW, startTime=null, duration=0}";
        assertTrue(historyManager.getHistory().get(0).toString().contains(str));
    }

    @Test
    void getHistoryRemoveSprint8Test3cStart() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        assertNotNull(historyManager, "История не пустая.");
        assertEquals(0, historyManager.getHistory().size(), "Список не пустой");
        for (int i = 0; i < 5; i++) {
            historyManager.add(new Task("N-T" + i, "D-T" + i, i, Status.NEW));
        }
        historyManager.add(new Epic("N-E5", "D-E5", 5, Status.NEW));
        for (int i = 6; i < 11; i++) {
            historyManager.add(new Subtask("N-S" + i, "D-S" + i, i, Status.NEW,5));
        }
        assertEquals(11, historyManager.getHistory().size(), "Список не пустой");
        Task removeTask = historyManager.getHistory().get(0);
        assertEquals(0, removeTask.getId(), "Список не пустой");
        historyManager.remove(0);
        assertEquals(10, historyManager.getHistory().size(), "Список не пустой");
        assertFalse(historyManager.getHistory().contains(removeTask));
    }

    @Test
    void getHistoryRemoveSprint8Test3cMiddle() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        assertNotNull(historyManager, "История не пустая.");
        assertEquals(0, historyManager.getHistory().size(), "Список не пустой");
        for (int i = 0; i < 5; i++) {
            historyManager.add(new Task("N-T" + i, "D-T" + i, i, Status.NEW));
        }
        historyManager.add(new Epic("N-E5", "D-E5", 5, Status.NEW));
        for (int i = 6; i < 11; i++) {
            historyManager.add(new Subtask("N-S" + i, "D-S" + i, i, Status.NEW,5));
        }
        assertEquals(11, historyManager.getHistory().size(), "Список не пустой");
        Task removeTask = historyManager.getHistory().get(4);
        assertEquals(4, removeTask.getId(), "Список не пустой");
        historyManager.remove(4);
        assertEquals(10, historyManager.getHistory().size(), "Список не пустой");
        assertFalse(historyManager.getHistory().contains(removeTask));
    }

    @Test
    void getHistoryRemoveSprint8Test3cEnd() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        assertNotNull(historyManager, "История не пустая.");
        assertEquals(0, historyManager.getHistory().size(), "Список не пустой");
        for (int i = 0; i < 5; i++) {
            historyManager.add(new Task("N-T" + i, "D-T" + i, i, Status.NEW));
        }
        historyManager.add(new Epic("N-E5", "D-E5", 5, Status.NEW));
        for (int i = 6; i < 11; i++) {
            historyManager.add(new Subtask("N-S" + i, "D-S" + i, i, Status.NEW,5));
        }
        assertEquals(11, historyManager.getHistory().size(), "Список не пустой");
        Task removeTask = historyManager.getHistory().get(10);
        assertEquals(10, removeTask.getId(), "Список не пустой");
        historyManager.remove(10);
        assertEquals(10, historyManager.getHistory().size(), "Список не пустой");
        assertFalse(historyManager.getHistory().contains(removeTask));
    }
}