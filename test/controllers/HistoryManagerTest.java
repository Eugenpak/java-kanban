package controllers;

import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;
import service.Status;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {

    @Test
    void add() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        Task task = new Task("","",1, Status.NEW);
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

    @Test
    void getHistory() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        assertEquals(0,historyManager.getHistory().size(), "Список не пустой");

        for (int i = 0; i < 3; i++) {
            historyManager.add(new Task("","",i,Status.NEW));
        }
        historyManager.add(new Epic("","",4,Status.NEW));

        assertNotNull(historyManager, "История не пустая.");
        assertEquals(4,historyManager.getHistory().size(), "Список не пустой");
        assertEquals(1,historyManager.getHistory().get(1).getId(), "Список не пустой");
        assertEquals("class model.Task",historyManager.getHistory().get(1).getClass().toString());
    }
}