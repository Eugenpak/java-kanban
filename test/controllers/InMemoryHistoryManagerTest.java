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
    /*
    private final TaskManager taskManager= Managers.getDefault();
    private final HistoryManager historyManager= Managers.getDefaultHistory();
    private final List<Task> list = new ArrayList<>();
    */

    @BeforeEach
    public void beforeEach(){ /*
        //inMemoryTaskManager  = new InMemoryTaskManager();
        //inMemoryHistoryManager  = new InMemoryHistoryManager();
        Task task1 = new Task("Задача 1","T0 ", Status.NEW);
        Epic epic = new Epic("Эпик 1","E1 ");
        Subtask subtask1 = new Subtask("Подзадача 1","S2 ", Status.NEW,1);
        Task task2 = new Task("Задача 2","T3 ", Status.NEW);

        taskManager.addNewTask(task1);
        taskManager.addNewEpic(epic);
        taskManager.addNewSubtask(subtask1);
        taskManager.addNewTask(task2);

        list.add( inMemoryTaskManager.getTaskById(0));
        list.add( inMemoryTaskManager.getEpicById(1));
        list.add( inMemoryTaskManager.getSubtaskById(2));
        list.add( inMemoryTaskManager.getTaskById(3));

        taskManager.getTaskById(0);
        taskManager.getEpicById(1);
        taskManager.getSubtaskById(2);
        taskManager.getTaskById(3);
        */

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
        /*
        historyManager.getHistory().clear();
        assertEquals(0,historyManager.getHistory().size(), "Список не пустой");
        // Проверка веденного epic{id=1,name="Эпик 1",description="Описание эпик-1"...}
        historyManager.add(taskManager.getHistory().get(1));
        assertEquals(1,historyManager.getHistory().size(), "Список не пустой");
        assertEquals(1,historyManager.getHistory().get(0).getId(), "Список не пустой");
        assertEquals("class model.Epic",historyManager.getHistory().get(0).getClass().toString());
        //  */


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
        /*
        assertEquals(0,historyManager.getHistory().size(), "Список не пустой");
        // Проверка веденного task2{id=3,"Задача 2","Описание задачи-2",satus=NEW}
        historyManager.add(taskManager.getHistory().get(3));
        assertEquals(1,historyManager.getHistory().size(), "Список не пустой");
        assertEquals(3,historyManager.getHistory().get(0).getId(), "Список не пустой");
        assertEquals("class model.Task",historyManager.getHistory().get(0).getClass().toString());
        */
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
        /*
        assertEquals(0,historyManager.getHistory().size(), "Список не пустой");
        // Проверка веденного subtask{id=2,name="Подзадача 1",description="Описание подзадачи-1",satus=NEW,epicId=1}
        historyManager.add(taskManager.getHistory().get(2));
        assertEquals(1,historyManager.getHistory().size(), "Список не пустой");
        assertEquals(2,historyManager.getHistory().get(0).getId(), "Список не пустой");
        assertEquals("class model.Subtask",historyManager.getHistory().get(0).getClass().toString());
        */
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
        /*
        assertEquals(4,historyManager.getHistory().size(), "Список не пустой");
        for (int i=0;i<15;i++){
            // Проверка веденного subtask{id=2,name="Подзадача 1",description="Описание подзадачи-1",satus=NEW,epicId=1}
            historyManager.add(taskManager.getHistory().get(2));
        }
        // Проверка веденного task2{id=3,"Задача 2","Описание задачи-2",satus=NEW}
        historyManager.add(new Task("Подзадача 1","Описание подзадачи-1",3,Status.NEW));
        assertEquals(10,historyManager.getHistory().size(), "Список не пустой");
        assertEquals(3,historyManager.getHistory().get(9).getId(), "Список не пустой");
        assertEquals("class model.Task",historyManager.getHistory().get(9).getClass().toString());
        */
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

        //inMemoryHistoryManager.add(new Subtask("","",1,Status.NEW,-4));

        assertEquals(3,inMemoryHistoryManager.getHistory().size(), "Список не пустой");
        assertEquals(1,inMemoryHistoryManager.getHistory().get(1).getId(), "Список не пустой");
        assertEquals("class model.Subtask",inMemoryHistoryManager.getHistory().get(1).getClass().toString());
        assertArrayEquals(listUser.toArray(),inMemoryHistoryManager.getHistory().toArray());
        /*
        assertEquals(0,historyManager.getHistory().size(), "Список не пустой");
        // Проверка веденного subtask{id=2,name="Подзадача 1",description="Описание подзадачи-1",satus=NEW,epicId=1}
        historyManager.add(taskManager.getHistory().get(2));
        // Проверка веденного task2{id=3,"Задача 2","Описание задачи-2",satus=NEW}
        historyManager.add(taskManager.getHistory().get(3));
        List<Task> list = historyManager.getHistory();
        assertEquals(2,list.size(), "Размер список не равен 2");
        assertEquals(3,list.get(1).getId(), "id не равен 3");
        assertEquals("class model.Task",list.get(1).getClass().toString());
        */
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

        /*
        historyManager.getHistory().clear();
        assertEquals(0,historyManager.getHistory().size(), "Список не пустой");
        Task task = new Task("Задача 2","Описание задачи-2", 3, Status.NEW);
        taskManager.updateTask(task);
        assertEquals(3,historyManager.getHistory().get(3).getId(), "Список не пустой");
        task.setStatus(Status.IN_PROGRESS);
        historyManager.add(task);
        task.setStatus(Status.DONE);
        historyManager.add(task);
        assertEquals(3,historyManager.getHistory().get(5).getId(), "Список не пустой");
        taskManager.getHistory();
        */
    }
}