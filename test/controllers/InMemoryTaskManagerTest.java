package controllers;

import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.Status;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    private InMemoryTaskManager taskManager;
    @BeforeEach
    public void beforeEach(){
        taskManager = new InMemoryTaskManager();
    }

    @Test
    void testToString() {

    }

    @Test
    void getListTask() {
        Task task1 = new Task("name-1","des-1", Status.NEW);

        final int taskId = taskManager.addNewTask(task1);
        final Task savedTask = taskManager.getTaskById(taskId);
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task1, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getListTask();
        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task1, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void getListEpic() {
        Epic epic = new Epic("E-1","DE-1");

        final int epicId = taskManager.addNewEpic(epic);
        final Epic savedEpic = taskManager.getEpicById(epicId);
        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic, savedEpic, "Задачи не совпадают.");

        final List<Epic> epics = taskManager.getListEpic();
        assertNotNull(epics, "Задачи не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(epic, epics.get(0), "Задачи не совпадают.");
    }

    @Test
    void getListSubtask() {
        Subtask subtask = new Subtask("S-1","SE-1",Status.NEW);

        final int subtaskId = taskManager.addNewSubtask(subtask);
        final Subtask savedSubtask = taskManager.getSubtaskById(subtaskId);
        assertNotNull(savedSubtask, "Задача не найдена.");
        assertEquals(subtask, savedSubtask, "Задачи не совпадают.");

        final List<Subtask> subtasks = taskManager.getListSubtask();
        assertNotNull(subtasks, "Задачи не возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество задач.");
        assertEquals(subtask, subtasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void getEpicSubtasks() {
        Epic epic = new Epic("E-1","DE-1");
        Subtask subtask = new Subtask("S-1","SE-1",Status.IN_PROGRESS);
        final int epicId = taskManager.addNewEpic(epic);
        final Epic savedEpic = taskManager.getEpicById(epicId);
        assertNotNull(savedEpic, "Задача не найдена.");
        final int subtaskId = taskManager.addNewSubtask(subtask);
        final Subtask savedSubtask = taskManager.getSubtaskById(subtaskId);
        assertNotNull(savedSubtask, "Задача не найдена.");
        taskManager.setEpicSubtask(epicId,subtaskId);
        final List<Subtask> arraySubtask = taskManager.getEpicSubtasks(epicId);

        assertNotNull(arraySubtask, "Задачи не возвращаются.");
        assertEquals(1, arraySubtask.size(), "Неверное количество задач.");
        assertEquals(subtask, arraySubtask.get(0), "Задачи не совпадают.");
    }

    @Test
    void getTaskById() {
        final int idTask = 1;
        Task task1 = new Task("name-1","des-1",idTask ,Status.NEW);
        taskManager.addNewTask(task1);
        final Task savedTask = taskManager.getTaskById(1);
        assertNotNull(savedTask, "Задачи не возвращается.");
        assertEquals(idTask, savedTask.getId(), "Задачи не совпадают.");
        assertEquals("class model.Task",savedTask.getClass().toString());
    }

    @Test
    void getSubtaskById() {
        final int idSubtask = 1;
        Subtask subtask1 = new Subtask("S-1","DS-1",idSubtask ,Status.NEW,-5);
        taskManager.addNewSubtask(subtask1);
        final Subtask savedSubtask = taskManager.getSubtaskById(idSubtask);
        assertNotNull(savedSubtask, "Задачи не возвращается.");
        assertEquals(idSubtask, savedSubtask.getId(), "Задачи не совпадают.");
        assertEquals("class model.Subtask",savedSubtask.getClass().toString());
    }

    @Test
    void getEpicById() {
        final int idEpic = 1;
        Epic epic1 = new Epic("E-1","DE-1",idEpic ,Status.NEW);
        taskManager.addNewEpic(epic1);
        final Epic savedEpic = taskManager.getEpicById(idEpic);
        assertNotNull(savedEpic, "Задачи не возвращается.");
        assertEquals(idEpic, savedEpic.getId(), "Задачи не совпадают.");
        assertEquals("class model.Epic",savedEpic.getClass().toString());
    }

    @Test
    void updateTask() {
        final int idTask = 0;
        Task task1 = new Task("name-1","des-1",idTask ,Status.NEW);
        taskManager.addNewTask(task1);
        final Task savedTask1 = taskManager.getTaskById(idTask);
        assertNotNull(savedTask1, "Задачи не возвращается.");
        savedTask1.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(savedTask1);
        final Task savedTask2 = taskManager.getTaskById(idTask);
        assertNotNull(savedTask2, "Задачи не возвращается.");
        assertEquals(savedTask2.getId(), savedTask1.getId(), "Задачи не совпадают.");
        assertNotSame(savedTask2,savedTask1);
    }

    @Test
    void updateEpic() {
        final int idEpic = 0;
        Epic epic1 = new Epic("E-1","DE-1",idEpic ,Status.NEW);
        taskManager.addNewEpic(epic1);
        final Epic savedEpic1 = taskManager.getEpicById(idEpic);
        assertNotNull(savedEpic1, "Задачи не возвращается.");
        savedEpic1.setDescription(savedEpic1.getDescription()+" Update-1");
        taskManager.updateEpic(savedEpic1);

        final Epic savedEpic2 = taskManager.getEpicById(idEpic);
        savedEpic2.setDescription(savedEpic1.getDescription()+" Update-2");
        taskManager.updateEpic(savedEpic2);

        assertNotNull(savedEpic2, "Задачи не возвращается.");
        assertEquals(savedEpic2.getId(), savedEpic1.getId(), "Задачи не совпадают.");
        assertNotSame(savedEpic2,savedEpic1);
    }

    @Test
    void updateSubtask() {
        final int idSubtask = 0;
        Subtask subtask1 = new Subtask("S-1","DS-1",idSubtask ,Status.NEW,-5);
        taskManager.addNewSubtask(subtask1);
        final Subtask savedSubtask1 = taskManager.getSubtaskById(idSubtask);
        assertNotNull(savedSubtask1, "Задачи не возвращается.");
        savedSubtask1.setDescription(savedSubtask1.getDescription()+" Update-1");
        taskManager.updateSubtask(savedSubtask1);

        final Subtask savedSubtask2 = taskManager.getSubtaskById(idSubtask);
        savedSubtask2.setDescription(savedSubtask1.getDescription()+" Update-2");
        taskManager.updateSubtask(savedSubtask2);

        assertNotNull(savedSubtask2, "Задачи не возвращается.");
        assertEquals(savedSubtask2.getId(), savedSubtask1.getId(), "Задачи не совпадают.");
        assertNotSame(savedSubtask2,savedSubtask1);
    }

    @Test
    void addNewTask() {
        final Task task1 = new Task("name-1","des-1", Status.NEW);

        final int taskId = taskManager.addNewTask(task1);
        taskManager.updateTask(task1);
        final Task savedTask = taskManager.getTaskById(taskId);
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task1, savedTask, "Задачи не совпадают.");
        assertNotSame(task1, savedTask);
    }

    @Test
    void addNewTaskError() {
        final Task task1 = new Task("name-1","des-1",5, Status.NEW);
        final Task task2 = new Task("name-1","des-1",1, Status.NEW);

        final int taskId = taskManager.addNewTask(task1);
        taskManager.addNewTask(task2);
        taskManager.updateTask(task1);
        final Task savedTask = taskManager.getTaskById(taskId);
        assertNotNull(savedTask, "Задача не найдена.");
        assertNotEquals(5, savedTask.getId(), "Задачи не совпадают.");
        assertNotSame(task1, savedTask);
    }

    @Test
    void addNewTaskError2() {
        final Epic epic3 = new Epic("E-2","DE-2",2 ,Status.NEW);
        final Task task4 = new Task("T-3","DT-3",2, Status.NEW);

        taskManager.addNewEpic(epic3);
        taskManager.updateEpic(epic3);
        final int task4Id = taskManager.addNewTask(task4);
        taskManager.updateTask(task4);

        final Task savedTask = taskManager.getTaskById(task4Id);
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task4.getId(), taskManager.getTaskById(task4Id).getId());
    }

    @Test
    void test7checkId(){
        Task task = new Task("T-15","DT-15",15, Status.NEW);
        final int taskId = taskManager.addNewTask(task);
        taskManager.updateTask(task);
        final Task savedTask = taskManager.getTaskById(taskId);
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask);
    }

    @Test
    void test8checkField(){
        Task task = new Task("T-15","DT-15", Status.NEW);
        final int taskId = taskManager.addNewTask(task);
        taskManager.updateTask(task);
        final Task savedTask = taskManager.getTaskById(taskId);
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task.getName(), savedTask.getName());
        assertEquals(task.getDescription(), savedTask.getDescription());
        assertEquals(task.getStatus(), savedTask.getStatus());
        assertEquals(task.getId(), savedTask.getId());

    }

    @Test
    void addNewEpic() {
        Epic epic = new Epic("E-1","DE-1");

        final int epicId = taskManager.addNewEpic(epic);
        taskManager.updateEpic(epic);
        final Epic savedEpic = taskManager.getEpicById(epicId);
        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic, savedEpic, "Задачи не совпадают.");
        assertNotSame(epic, savedEpic);
    }

    @Test
    void addNewSubtask() {
        Subtask subtask = new Subtask("S-1","SE-1",Status.NEW);

        final int subtaskId = taskManager.addNewSubtask(subtask);
        taskManager.updateSubtask(subtask);
        final Subtask savedSubtask = taskManager.getSubtaskById(subtaskId);
        assertNotNull(savedSubtask, "Задача не найдена.");
        assertEquals(subtask, savedSubtask, "Задачи не совпадают.");
        assertNotSame(subtask, savedSubtask);
    }

    @Test
    void deleteTask() {
        final Task task1 = new Task("name-1","des-1", Status.NEW);

        final int taskId = taskManager.addNewTask(task1);
        final Task savedTask1 = taskManager.getTaskById(taskId);
        assertNotNull(savedTask1, "Задача не найдена.");
        assertEquals(taskId, savedTask1.getId(), "Задачи не совпадают.");
        taskManager.deleteTask(taskId);
        final Task savedTask2 = taskManager.getTaskById(taskId);
        assertNull(savedTask2, "Задача найдена.");
    }

    @Test
    void deleteEpic() {
        Epic epic = new Epic("E-1","DE-1");

        final int epicId = taskManager.addNewEpic(epic);
        final Epic savedEpic1 = taskManager.getEpicById(epicId);
        assertNotNull(savedEpic1, "Задача не найдена.");
        assertEquals(epicId, savedEpic1.getId(), "Задачи не совпадают.");
        taskManager.deleteEpic(epicId);
        final Epic savedEpic2 = taskManager.getEpicById(epicId);
        assertNotNull(savedEpic1, "Задача не найдена.");
        assertNull(savedEpic2, "Задача найдена.");
    }

    @Test
    void deleteSubtask() {
        Subtask subtask = new Subtask("S-1","SE-1",Status.NEW);

        final int subtaskId = taskManager.addNewSubtask(subtask);
        final Subtask savedSubtask1 = taskManager.getSubtaskById(subtaskId);
        assertNotNull(savedSubtask1, "Задача не найдена.");
        assertEquals(subtaskId, savedSubtask1.getId(), "Задачи не совпадают.");
        taskManager.deleteSubtask(subtaskId);
        final Subtask savedSubtask2 = taskManager.getSubtaskById(subtaskId);
        assertNotNull(savedSubtask1, "Задача не найдена.");
        assertNull(savedSubtask2, "Задача найдена.");
    }

    @Test
    void deleteTasks() {
        Task task1 = new Task("name-1","des-1", Status.NEW);

        final int taskId = taskManager.addNewTask(task1);
        final Task savedTask = taskManager.getTaskById(taskId);
        assertNotNull(savedTask, "Задача не найдена.");
        final List<Task> tasks = taskManager.getListTask();
        assertEquals(1, tasks.size(), "Задачи не совпадают.");
        taskManager.deleteTasks();
        assertEquals(0,taskManager.getListTask().size(), "Список не пустой.");
    }

    @Test
    void deleteEpics() {
        Epic epic = new Epic("E-1","DE-1");

        final int epicId = taskManager.addNewEpic(epic);
        final Epic savedEpic = taskManager.getEpicById(epicId);
        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic, savedEpic, "Задачи не совпадают.");

        final List<Epic> epics = taskManager.getListEpic();
        assertNotNull(epics, "Задачи не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        taskManager.deleteEpics();
        assertNotNull(epics, "Задачи не возвращаются.");
        assertEquals(0,taskManager.getListEpic().size(), "Список не пустой.");
    }

    @Test
    void deleteSubtasks() {
        Subtask subtask = new Subtask("S-1","SE-1",Status.NEW);

        final int subtaskId = taskManager.addNewSubtask(subtask);
        final Subtask savedSubtask = taskManager.getSubtaskById(subtaskId);
        assertNotNull(savedSubtask, "Задача не найдена.");
        assertEquals(subtask, savedSubtask, "Задачи не совпадают.");

        final List<Subtask> subtasks = taskManager.getListSubtask();
        assertNotNull(subtasks, "Задачи не возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество задач.");
        taskManager.deleteSubtasks();
        assertNotNull(subtasks, "Задачи не возвращаются.");
        assertEquals(0,taskManager.getListSubtask().size(), "Список не пустой.");
    }

    @Test
    void test3E(){
        Epic epic = new Epic("E-1","DE-1");

        final int epicId = taskManager.addNewEpic(epic);
        final Epic savedEpic = taskManager.getEpicById(epicId);
        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic, savedEpic, "Задачи не совпадают.");
        assertEquals(0, savedEpic.getArraySubtask().size(), "Список подзадач не пуст!");
        taskManager.setEpicSubtask(epicId,epicId);
        assertEquals(0, savedEpic.getArraySubtask().size(), "Список подзадач не пуст!");
    }

    @Test
    void test4Subtask(){
        Subtask subtask = new Subtask("S-1","SE-1",Status.NEW);

        final int subtaskId = taskManager.addNewSubtask(subtask);
        final Subtask savedSubtask = taskManager.getSubtaskById(subtaskId);
        assertNotNull(savedSubtask, "Задача не найдена.");
        assertEquals(subtask, savedSubtask, "Задачи не совпадают.");
        savedSubtask.setEpicId(subtaskId);
        assertNotEquals(subtaskId, savedSubtask.getEpicId(), "Значение поля равны!");
    }

    @Test
    void test6findTask(){
        Task task1 = new Task("name-1","des-1", Status.NEW);
        final int taskId = taskManager.addNewTask(task1);
        Epic epic = new Epic("E-1","DE-1");
        final int epicId = taskManager.addNewEpic(epic);
        Subtask subtask = new Subtask("S-1","SE-1",Status.NEW);
        final int subtaskId = taskManager.addNewSubtask(subtask);

        final Task findedTask = taskManager.findId(taskId);
        assertNotNull(findedTask, "Задача не найдена.");
        assertEquals(taskId, findedTask.getId(), "Значение поля не равны!");
    }


}