package model;

import org.junit.jupiter.api.Test;
import service.Status;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void getId() {
        Task task = new Task("","",3,Status.NEW);
        assertNotNull(task, "Задача не пустая.");
        assertEquals(3,task.getId(), "Нет тот id!");
    }

    @Test
    void setId() {
        Task task = new Task("","",3,Status.NEW);
        task.setId(4);
        assertNotNull(task, "Задача не пустая.");
        assertEquals(4,task.getId(), "Нет тот id!");
    }

    @Test
    void testToString() {
        Task task = new Task("T3","DT3",3,Status.NEW);
        assertNotNull(task, "Задача не пустая.");
        assertEquals("Task{Id=3, name='T3', description='DT3', status=NEW}",task.toString(), "Не верная строка!");
    }

    @Test
    void setStatus() {
        Task task = new Task("","",3,Status.NEW);
        task.setStatus(Status.IN_PROGRESS);
        assertNotNull(task, "Задача не пустая.");
        assertEquals(Status.IN_PROGRESS,task.getStatus(), "Нет тот статус!");
    }

    @Test
    void getStatus() {
        Task task = new Task("","",3,Status.DONE);
        assertNotNull(task, "Задача не пустая.");
        assertEquals(Status.DONE,task.getStatus(), "Нет тот статус!");
    }

    @Test
    void getName() {
        Task task = new Task("OldName","",3,Status.DONE);
        assertNotNull(task, "Задача не пустая.");
        assertEquals("OldName",task.getName(), "Не верное значение name!");
    }

    @Test
    void setName() {
        Task task = new Task("OldName","",3,Status.DONE);
        task.setName("NewName");
        assertNotNull(task, "Задача не пустая.");
        assertEquals("NewName",task.getName(), "Не верное значение name!");
    }

    @Test
    void getDescription() {
        Task task = new Task("Name","OldDescription",3,Status.DONE);

        assertNotNull(task, "Задача не пустая.");
        assertEquals("OldDescription",task.getDescription(), "Не верное значение описания!");
    }

    @Test
    void setDescription() {
        Task task = new Task("Name","OldDescription",3,Status.DONE);
        task.setDescription("NewDescription");
        assertNotNull(task, "Задача не пустая.");
        assertEquals("NewDescription",task.getDescription(), "Не верное значение описания!");
    }

    @Test
    void test1() {
        Task task1 = new Task("name-1","des-1",1, Status.NEW);
        Task task2 = new Task("name-2","des-2",1, Status.DONE);
        assertNotSame(task1,task2,"Один и тот же объект!");
        assertEquals(task1.hashCode(),task2.hashCode());
        assertEquals(task1.getId(),task2.getId());
    }

    @Test
    void test2() {
        Task task1 = new Task("name-1","des-1",1, Status.NEW, LocalDateTime.now(), Duration.ofMinutes(15));
        Task task2 = new Task("name-2","des-2",1, Status.DONE, LocalDateTime.now(), Duration.ofMinutes(15));
        assertNotSame(task1,task2,"Один и тот же объект!");
        assertEquals(task1.hashCode(),task2.hashCode());
        assertEquals(task1.getId(),task2.getId());
        task1.getEndTime();
        task1.getDuration();
    }
}