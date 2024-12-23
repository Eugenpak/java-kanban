package model;

import org.junit.jupiter.api.Test;
import service.Status;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {

    @Test
    void update() {
        Epic epic = new Epic("","",4,Status.NEW);
        Subtask subtask1 = new Subtask("name-1","des-1",1, Status.IN_PROGRESS,3);
        subtask1.setEpicId(epic.getId());
        epic.getArraySubtask().add(subtask1);
        subtask1.update(epic);
        assertNotNull(subtask1, "Подзадача не пустая.");
        assertEquals(4,subtask1.getEpicId(), "Нет тот id Epic!");
        assertEquals(Status.IN_PROGRESS,epic.getStatus(), "Нет тот статус!");
    }

    @Test
    void getEpicId() {
        Subtask subtask1 = new Subtask("name-1","des-1",1, Status.NEW,3);
        assertNotNull(subtask1, "Подзадача не пустая.");
        assertEquals(3,subtask1.getEpicId(), "Нет тот статус!");
    }

    @Test
    void setEpicId() {
        Epic epic = new Epic("","",4,Status.NEW);
        Subtask subtask1 = new Subtask("name-1","des-1",1, Status.IN_PROGRESS,3);

        assertEquals(3,subtask1.getEpicId(), "Нет тот id Epic!");
        subtask1.setEpicId(epic.getId());
        assertNotNull(subtask1, "Подзадача не пустая.");
        assertEquals(4,subtask1.getEpicId(), "Нет тот id Epic!");
    }
    @Test
    void copySubtask() {
        Subtask subtask1 = new Subtask("name-1","des-1",1, Status.NEW,3);
        Subtask subtask2 = subtask1.copySubtask();
        assertNotSame(subtask1,subtask2,"Один и тот же объект!");
        assertEquals(subtask1.hashCode(),subtask2.hashCode());
        assertEquals(subtask1.getId(),subtask2.getId());
    }

    @Test
    void test2S() {
        Subtask subtask1 = new Subtask("name-1","des-1",1, Status.NEW,3);
        Subtask subtask2 = new Subtask("name-2","des-2",1, Status.DONE, 4);
        assertNotSame(subtask1,subtask2,"Один и тот же объект!");
        assertEquals(subtask1.hashCode(),subtask2.hashCode());
        assertEquals(subtask1.getId(),subtask2.getId());
    }

    //------------------------------------------------------------
    @Test
    void getId() {
        Subtask subtask = new Subtask("","",7,Status.NEW,3);
        assertNotNull(subtask, "Подзадача не пустая.");
        assertEquals(7,subtask.getId(), "Нет тот id!");
    }

    @Test
    void setId() {
        Subtask subtask = new Subtask("","",7,Status.NEW,3);
        assertEquals(7,subtask.getId(), "Нет тот id!");
        subtask.setId(5);
        assertNotNull(subtask, "Подзадача не пустая.");
        assertEquals(5,subtask.getId(), "Нет тот id!");
    }

    @Test
    void testToString() {
        Subtask subtask = new Subtask("S7","DS7",7,Status.NEW,3);
        String str="Subtask{id=7, name='S7', description='DS7', status=NEW, epicId=3, startTime=null, duration=null}";

        assertNotNull(subtask, "Подзадача не пустая.");
        assertEquals(str,subtask.toString(), "Нет эта строка!");
    }

    @Test
    void setStatus() {
        Subtask subtask = new Subtask("S7","DS7",7,Status.NEW,3);
        assertNotNull(subtask, "Задача не пустая.");
        assertEquals(Status.NEW,subtask.getStatus(), "Нет тот статус!");
        subtask.setStatus(Status.IN_PROGRESS);
        assertEquals(Status.IN_PROGRESS,subtask.getStatus(), "Нет тот статус!");
    }

    @Test
    void getStatus() {
        Subtask subtask = new Subtask("S7","DS7",7,Status.NEW,3);
        assertNotNull(subtask, "Задача не пустая.");
        assertEquals(Status.NEW,subtask.getStatus(), "Нет тот статус!");
    }

    @Test
    void getName() {
        Subtask subtask = new Subtask("OldName","DS7",7,Status.NEW,3);
        assertNotNull(subtask, "Задача не пустая.");
        assertEquals("OldName",subtask.getName(), "Не верное значение name!");
    }

    @Test
    void setName() {
        Subtask subtask = new Subtask("OldName","DS7",7,Status.NEW,3);
        assertNotNull(subtask, "Задача не пустая.");
        assertEquals("OldName",subtask.getName(), "Не верное значение name!");
        subtask.setName("NewName");
        assertEquals("NewName",subtask.getName(), "Не верное значение name!");
    }

    @Test
    void getDescription() {
        Subtask subtask = new Subtask("S1","OldDescription",7,Status.NEW,3);
        assertNotNull(subtask, "Задача не пустая.");
        assertEquals("OldDescription",subtask.getDescription(), "Не верное значение описания!");
    }

    @Test
    void setDescription() {
        Subtask subtask = new Subtask("S1","OldDescription",7,Status.NEW,3);
        assertNotNull(subtask, "Задача не пустая.");
        assertEquals("OldDescription",subtask.getDescription(), "Не верное значение описания!");
        subtask.setDescription("New-Description");
        assertEquals("New-Description",subtask.getDescription(), "Не верное значение описания!");
    }
}