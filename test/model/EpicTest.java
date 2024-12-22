package model;

import org.junit.jupiter.api.Test;
import service.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    @Test
    void getArraySubtask() {
        Subtask subtask1 = new Subtask("S-1","DS-1",4, Status.IN_PROGRESS,3);
        Subtask subtask2 = new Subtask("S-2","DS-2",10, Status.DONE,7);
        Epic epic = new Epic("name-1","des-1",1, Status.NEW);
        assertEquals(0,epic.getArraySubtask().size(), "Не правильный размер списка Subtask!");
        ArrayList<Subtask> arraySubtask = new ArrayList<>();
        subtask1.setEpicId(epic.getId());
        arraySubtask.add(subtask1);
        subtask2.setEpicId(epic.getId());
        arraySubtask.add(subtask2);
        epic.setArraySubtask(arraySubtask);
        epic.updateStatus();
        assertEquals(2,epic.getArraySubtask().size(), "Не правильный размер списка Subtask!");
    }

    @Test
    void setArraySubtask() {
        LocalDateTime start = LocalDateTime.of(2024,12,22,10,0,0);
        Duration durationTest = Duration.ofMinutes(15);
        Subtask subtask1 = new Subtask("S-1","DS-1",4, Status.IN_PROGRESS,3,start,durationTest);
        Subtask subtask2 = new Subtask("S-2","DS-2",10, Status.DONE,7,start.plusMinutes(20),durationTest);
        Epic epic = new Epic("name-1","des-1",1, Status.NEW);

        assertEquals(0,epic.getArraySubtask().size(), "Не правильный размер списка Subtask!");
        assertEquals(3,subtask1.getEpicId(), "Не правильный id Epic!");
        assertEquals(7,subtask2.getEpicId(), "Не правильный id Epic!");
        assertEquals(Status.NEW,epic.getStatus(), "Не правильный статус Epic!");

        ArrayList<Subtask> arraySubtask = new ArrayList<>();
        subtask1.setEpicId(epic.getId());
        arraySubtask.add(subtask1);
        subtask2.setEpicId(epic.getId());
        arraySubtask.add(subtask2);
        epic.setArraySubtask(arraySubtask);
        epic.updateStatus();

        assertEquals(2,epic.getArraySubtask().size(), "Не правильный размер списка Subtask!");
        assertEquals(1,subtask1.getEpicId(), "Не правильный id Epic!");
        assertEquals(1,subtask2.getEpicId(), "Не правильный id Epic!");
        assertEquals(Status.IN_PROGRESS,epic.getStatus(), "Не правильный статус Epic!");
    }

    @Test
    void updateStatus() {
        Subtask subtask1 = new Subtask("S-1","DS-1",4, Status.IN_PROGRESS,3);
        Epic epic = new Epic("name-1","des-1",1, Status.NEW);
        ArrayList<Subtask> arraySubtask = new ArrayList<>();
        arraySubtask.add(subtask1);
        epic.setArraySubtask(arraySubtask);
        assertEquals(Status.NEW,epic.getStatus(), "Не правильный статус Epic!");
        epic.updateStatus();
        assertEquals(Status.IN_PROGRESS,epic.getStatus(), "Не правильный статус Epic!");
    }

    @Test
    void test2E() {
        Epic epic1 = new Epic("name-1","des-1",1, Status.NEW);
        Epic epic2 = new Epic("name-2","des-2",1, Status.DONE);
        assertNotSame(epic1,epic2,"Один и тот же объект!");
        assertEquals(epic1.hashCode(),epic2.hashCode());
        assertEquals(epic1.getId(),epic2.getId());
    }

    //----------------------------------------------------------------------------------
    @Test
    void getId() {
        Epic epic = new Epic("name-1","des-1",1, Status.NEW);
        assertNotNull(epic, "Задача не пустая.");
        assertEquals(1,epic.getId(), "Нет тот id!");
    }

    @Test
    void setId() {
        Epic epic = new Epic("name-1","des-1",1, Status.NEW);
        assertNotNull(epic, "Задача не пустая.");
        assertEquals(1,epic.getId(), "Нет тот id!");
        epic.setId(4);
        assertNotNull(epic, "Задача не пустая.");
        assertEquals(4,epic.getId(), "Нет тот id!");
    }

    @Test
    void testToString() {
        Epic epic = new Epic("name-1","des-1",1, Status.NEW);
        assertNotNull(epic, "Задача не пустая.");
        String str = "Epic{Id=1, name='name-1', description='des-1', status=NEW, arraySubtask: []}";
        assertEquals(str,epic.toString(), "Не верная строка!");
    }

    @Test
    void setStatus() {
        Epic epic = new Epic("E-1","DE-1",1, Status.NEW);
        assertEquals(Status.NEW,epic.getStatus(), "Нет тот статус!");
        epic.setStatus(Status.IN_PROGRESS);
        assertNotNull(epic, "Задача не пустая.");
        assertEquals(Status.IN_PROGRESS,epic.getStatus(), "Нет тот статус!");
    }

    @Test
    void getStatus() {
        Epic epic = new Epic("E-1","DE-1",1, Status.NEW);
        assertNotNull(epic, "Задача не пустая.");
        assertEquals(Status.NEW,epic.getStatus(), "Нет тот статус!");
    }

    @Test
    void getName() {
        Epic epic = new Epic("OldName","DE-1",1, Status.DONE);
        assertNotNull(epic, "Задача не пустая.");
        assertEquals("OldName",epic.getName(), "Не верное значение name!");
    }

    @Test
    void setName() {
        Epic epic = new Epic("OldName","DE-1",1, Status.DONE);
        assertEquals("OldName",epic.getName(), "Не верное значение name!");
        epic.setName("NewName");
        assertNotNull(epic, "Задача не пустая.");
        assertEquals("NewName",epic.getName(), "Не верное значение name!");
    }

    @Test
    void getDescription() {
        Epic epic = new Epic("OldName","DE-1",1, Status.DONE);

        assertNotNull(epic, "Задача не пустая.");
        assertEquals("DE-1",epic.getDescription(), "Не верное значение описания!");
    }

    @Test
    void setDescription() {
        Epic epic = new Epic("E1","Old-Description",1, Status.DONE);
        assertNotNull(epic, "Задача не пустая.");
        assertEquals("Old-Description",epic.getDescription(), "Не верное значение описания!");
        epic.setDescription("New-Description");
        assertEquals("New-Description",epic.getDescription(), "Не верное значение описания!");
    }
}