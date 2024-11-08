package model;

import org.junit.jupiter.api.Test;
import service.Status;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {
    /*
    @Test
    void update() {
    }

    @Test
    void getEpicId() {
    }

    @Test
    void setEpicId() {
    }
    */

    @Test
    void test2S() {
        Subtask subtask1 = new Subtask("name-1","des-1",1, Status.NEW,3);
        Subtask subtask2 = new Subtask("name-2","des-2",1, Status.DONE, 4);
        assertNotSame(subtask1,subtask2,"Один и тот же объект!");
        assertEquals(subtask1.hashCode(),subtask2.hashCode());
        assertEquals(subtask1.getId(),subtask2.getId());
    }
}