package model;

import org.junit.jupiter.api.Test;
import service.Status;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    /*
    @Test
    void getArraySubtask() {
    }

    @Test
    void setArraySubtask() {
    }

    @Test
    void updateStatus() {
    }
    */

    @Test
    void test2E() {
        Epic epic1 = new Epic("name-1","des-1",1, Status.NEW);
        Epic epic2 = new Epic("name-2","des-2",1, Status.DONE);
        assertNotSame(epic1,epic2,"Один и тот же объект!");
        assertEquals(epic1.hashCode(),epic2.hashCode());
        assertEquals(epic1.getId(),epic2.getId());
    }

}