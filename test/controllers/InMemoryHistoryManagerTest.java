package controllers;

import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.Status;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    protected InMemoryTaskManager inMemoryTaskManager;
    protected InMemoryHistoryManager inMemoryHistoryManager;
    @BeforeEach
    public void beforeEach(){
        inMemoryTaskManager  = new InMemoryTaskManager();
        inMemoryHistoryManager  = new InMemoryHistoryManager();
        Task task1 = new Task("Задача 1","Описание задачи-1", 0, Status.NEW);
        Epic epic = new Epic("Эпик 1","Описание эпик-1", 1, Status.DONE);
        Subtask subtask1 = new Subtask("Подзадача 1","Описание подзадачи-1", 2, Status.NEW,1);
        Task task2 = new Task("Задача 2","Описание задачи-2", 3, Status.NEW);
        inMemoryTaskManager.addNewTask(task1);
        inMemoryTaskManager.addNewEpic(epic);
        inMemoryTaskManager.addNewSubtask(subtask1);
        inMemoryTaskManager.addNewTask(task2);
        inMemoryTaskManager.getTaskById(0);
        inMemoryTaskManager.getEpicById(1);
        inMemoryTaskManager.getSubtaskById(2);
        inMemoryTaskManager.getTaskById(3);
    }

    @Test
    void addShouldBe1WhenEpic(){
        assertEquals(0,inMemoryHistoryManager.getHistory().size(), "Список не пустой");
        // Проверка веденного epic{id=1,name="Эпик 1",description="Описание эпик-1"...}
        inMemoryHistoryManager.add(inMemoryTaskManager.getHistory().get(1));
        assertEquals(1,inMemoryHistoryManager.getHistory().size(), "Список не пустой");
        assertEquals(1,inMemoryHistoryManager.getHistory().get(0).getId(), "Список не пустой");
        assertEquals("class model.Epic",inMemoryHistoryManager.getHistory().get(0).getClass().toString());
    }
    @Test
    void addShouldBe3WhenTask(){
        assertEquals(0,inMemoryHistoryManager.getHistory().size(), "Список не пустой");
        // Проверка веденного task2{id=3,"Задача 2","Описание задачи-2",satus=NEW}
        inMemoryHistoryManager.add(inMemoryTaskManager.getHistory().get(3));
        assertEquals(1,inMemoryHistoryManager.getHistory().size(), "Список не пустой");
        assertEquals(3,inMemoryHistoryManager.getHistory().get(0).getId(), "Список не пустой");
        assertEquals("class model.Task",inMemoryHistoryManager.getHistory().get(0).getClass().toString());
    }

    @Test
    void addShouldBe2WhenSubtask(){
        assertEquals(0,inMemoryHistoryManager.getHistory().size(), "Список не пустой");
        // Проверка веденного subtask{id=2,name="Подзадача 1",description="Описание подзадачи-1",satus=NEW,epicId=1}
        inMemoryHistoryManager.add(inMemoryTaskManager.getHistory().get(2));
        assertEquals(1,inMemoryHistoryManager.getHistory().size(), "Список не пустой");
        assertEquals(2,inMemoryHistoryManager.getHistory().get(0).getId(), "Список не пустой");
        assertEquals("class model.Subtask",inMemoryHistoryManager.getHistory().get(0).getClass().toString());
    }

    @Test
    void addShouldBeListSize10WhenAdd15(){
        assertEquals(0,inMemoryHistoryManager.getHistory().size(), "Список не пустой");
        for (int i=0;i<15;i++){
            // Проверка веденного subtask{id=2,name="Подзадача 1",description="Описание подзадачи-1",satus=NEW,epicId=1}
            inMemoryHistoryManager.add(inMemoryTaskManager.getHistory().get(2));
        }
        // Проверка веденного task2{id=3,"Задача 2","Описание задачи-2",satus=NEW}
        inMemoryHistoryManager.add(inMemoryTaskManager.getHistory().get(3));
        assertEquals(10,inMemoryHistoryManager.getHistory().size(), "Список не пустой");
        assertEquals(3,inMemoryHistoryManager.getHistory().get(9).getId(), "Список не пустой");
        assertEquals("class model.Task",inMemoryHistoryManager.getHistory().get(9).getClass().toString());
    }


    @Test
    void getHistory() {
        assertEquals(0,inMemoryHistoryManager.getHistory().size(), "Список не пустой");
        // Проверка веденного subtask{id=2,name="Подзадача 1",description="Описание подзадачи-1",satus=NEW,epicId=1}
        inMemoryHistoryManager.add(inMemoryTaskManager.getHistory().get(2));
        // Проверка веденного task2{id=3,"Задача 2","Описание задачи-2",satus=NEW}
        inMemoryHistoryManager.add(inMemoryTaskManager.getHistory().get(3));
        List<Task> list = inMemoryHistoryManager.getHistory();
        assertEquals(2,list.size(), "Размер список не равен 2");
        assertEquals(3,list.get(1).getId(), "id не равен 3");
        assertEquals("class model.Task",list.get(1).getClass().toString());
    }
}