package model;

import org.junit.jupiter.api.Test;
import service.Status;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    /*
    @Test
    void getId() {
    }

    @Test
    void setId() {
    }

    @Test
    void testToString() {
    }

    @Test
    void setStatus() {
    }

    @Test
    void getStatus() {
    }

    @Test
    void getName() {
    }

    @Test
    void setName() {
    }

    @Test
    void getDescription() {
    }

    @Test
    void setDescription() {
    }

    */

    @Test
    void test1() {
        Task task1 = new Task("name-1","des-1",1, Status.NEW);
        Task task2 = new Task("name-2","des-2",1, Status.DONE);
        assertNotSame(task1,task2,"Один и тот же объект!");
        assertEquals(task1.hashCode(),task2.hashCode());
        assertEquals(task1.getId(),task2.getId());
    }
}