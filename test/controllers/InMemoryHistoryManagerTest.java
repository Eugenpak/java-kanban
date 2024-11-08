package controllers;

import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.Status;

import java.util.ArrayList;


import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    @BeforeEach
    public void beforeEach(){
    }

    @Test
    void addShouldBe1WhenEpic(){
        InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
        assertEquals(0,inMemoryHistoryManager.getHistory().size(), "Список не пустой");

        inMemoryHistoryManager.add(new Epic("","",0,Status.NEW));
        inMemoryHistoryManager.add(new Epic("","",1,Status.NEW));
        inMemoryHistoryManager.add(new Epic("","",2,Status.NEW));
        assertEquals(3,inMemoryHistoryManager.getHistory().size(), "Список не пустой");
        assertEquals(1,inMemoryHistoryManager.getHistory().get(1).getId(), "Список не пустой");
        assertEquals("class model.Epic",inMemoryHistoryManager.getHistory().get(1).getClass().toString());
    }
    @Test
    void addShouldBe3WhenTask(){
        InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
        assertEquals(0,inMemoryHistoryManager.getHistory().size(), "Список не пустой");

        inMemoryHistoryManager.add(new Task("","",0,Status.NEW));
        inMemoryHistoryManager.add(new Task("","",1,Status.NEW));
        inMemoryHistoryManager.add(new Task("","",2,Status.NEW));
        assertEquals(3,inMemoryHistoryManager.getHistory().size(), "Список не пустой");
        assertEquals(1,inMemoryHistoryManager.getHistory().get(1).getId(), "Список не пустой");
        assertEquals("class model.Task",inMemoryHistoryManager.getHistory().get(1).getClass().toString());
    }

    @Test
    void addShouldBe2WhenSubtask(){
        InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
        assertEquals(0,inMemoryHistoryManager.getHistory().size(), "Список не пустой");

        inMemoryHistoryManager.add(new Subtask("","",0,Status.NEW,-4));
        inMemoryHistoryManager.add(new Subtask("","",1,Status.NEW,-4));
        inMemoryHistoryManager.add(new Subtask("","",2,Status.NEW,-4));
        assertEquals(3,inMemoryHistoryManager.getHistory().size(), "Список не пустой");
        assertEquals(1,inMemoryHistoryManager.getHistory().get(1).getId(), "Список не пустой");
        assertEquals("class model.Subtask",inMemoryHistoryManager.getHistory().get(1).getClass().toString());
    }

    @Test
    void addShouldBeListSize10WhenAdd12(){
        InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
        assertEquals(0,inMemoryHistoryManager.getHistory().size(), "Список не пустой");

        inMemoryHistoryManager.add(new Task("","",0,Status.NEW));
        inMemoryHistoryManager.add(new Task("","",1,Status.NEW));
        inMemoryHistoryManager.add(new Task("","",2,Status.NEW));
        inMemoryHistoryManager.add(new Epic("","",3,Status.NEW));
        inMemoryHistoryManager.add(new Subtask("","",4,Status.NEW,3));
        inMemoryHistoryManager.add(new Subtask("","",5,Status.NEW,3));
        inMemoryHistoryManager.add(new Task("","",6,Status.NEW));
        inMemoryHistoryManager.add(new Task("","",7,Status.NEW));
        inMemoryHistoryManager.add(new Task("","",8,Status.NEW));
        inMemoryHistoryManager.add(new Task("","",9,Status.NEW));
        inMemoryHistoryManager.add(new Epic("","",10,Status.NEW));
        inMemoryHistoryManager.add(new Subtask("","",11,Status.NEW,3));

        assertEquals(11,inMemoryHistoryManager.getHistory().get(9).getId(), "Список не пустой");
        assertEquals("class model.Subtask",inMemoryHistoryManager.getHistory().get(9).getClass().toString());
        assertEquals("class model.Epic",inMemoryHistoryManager.getHistory().get(1).getClass().toString());
        assertEquals(10,inMemoryHistoryManager.getHistory().size(), "Список не пустой");
    }


    @Test
    void getHistory() {
        InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
        assertEquals(0,inMemoryHistoryManager.getHistory().size(), "Список не пустой");
        ArrayList<Task> listUser = new ArrayList<>();
        listUser.add(new Subtask("","",0,Status.NEW,-4));
        listUser.add(new Subtask("","",1,Status.NEW,-4));
        listUser.add(new Subtask("","",2,Status.NEW,-4));

        for (Task elem:listUser){
            inMemoryHistoryManager.add(elem);
        }

        assertEquals(3,inMemoryHistoryManager.getHistory().size(), "Список не пустой");
        assertEquals(1,inMemoryHistoryManager.getHistory().get(1).getId(), "Список не пустой");
        assertEquals("class model.Subtask",inMemoryHistoryManager.getHistory().get(1).getClass().toString());
        assertArrayEquals(listUser.toArray(),inMemoryHistoryManager.getHistory().toArray());
    }

    @Test
    void test9(){
        InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
        assertEquals(0,inMemoryHistoryManager.getHistory().size(), "Список не пустой");
        inMemoryHistoryManager.add(new Task("","",0,Status.NEW));
        Task taskTest = new Task("","",1,Status.NEW);
        inMemoryHistoryManager.add(taskTest);
        inMemoryHistoryManager.add(new Task("","",2,Status.NEW));
        assertEquals(3,inMemoryHistoryManager.getHistory().size(), "Список не пустой");

        taskTest.setStatus(Status.IN_PROGRESS);
        inMemoryHistoryManager.add(taskTest);
        taskTest.setStatus(Status.DONE);
        inMemoryHistoryManager.add(taskTest);
        taskTest.setDescription("T1");
        inMemoryHistoryManager.add(taskTest);

        inMemoryHistoryManager.add(new Epic("","",3,Status.NEW));
        inMemoryHistoryManager.add(new Subtask("","",4,Status.NEW,3));

        assertEquals(8,inMemoryHistoryManager.getHistory().size(), "Список не пустой");
        taskTest = null;
        assertEquals(1,inMemoryHistoryManager.getHistory().get(1).getId(), "Список не пустой");
        assertEquals("class model.Task",inMemoryHistoryManager.getHistory().get(1).getClass().toString());

    }
}