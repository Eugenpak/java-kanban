package controllers;

import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    public void beforeEach() {
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
        LocalDateTime start = LocalDateTime.of(2024,9,12,8,0,0);
        Epic epic = new Epic("E-1","DE-1");
        Subtask subtask = new Subtask("S-1","SE-1",Status.IN_PROGRESS,start,Duration.ofMinutes(15));
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
        Task task1 = new Task("name-1","des-1",2 ,Status.NEW);
        final int idTask = taskManager.addNewTask(task1);
        final Task savedTask = taskManager.getTaskById(idTask);
        assertNotNull(savedTask, "Задачи не возвращается.");
        assertEquals(0, savedTask.getId(), "Задачи не совпадают.");
        assertEquals("class model.Task",savedTask.getClass().toString());
    }

    @Test
    void getSubtaskById() {
        Subtask subtask1 = new Subtask("S-1","DS-1",1,Status.NEW,-5);
        final int idSubtask = taskManager.addNewSubtask(subtask1);
        final Subtask savedSubtask = taskManager.getSubtaskById(idSubtask);
        assertNotNull(savedSubtask, "Задачи не возвращается.");
        assertEquals(0, savedSubtask.getId(), "Задачи не совпадают.");
        assertEquals("class model.Subtask",savedSubtask.getClass().toString());
    }

    @Test
    void getEpicById() {
        Epic epic1 = new Epic("E-1","DE-1",1 ,Status.NEW);
        final int idEpic = taskManager.addNewEpic(epic1);
        final Epic savedEpic = taskManager.getEpicById(idEpic);
        assertNotNull(savedEpic, "Задачи не возвращается.");
        assertEquals(0, savedEpic.getId(), "Задачи не совпадают.");
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
        savedEpic1.setDescription(savedEpic1.getDescription() + " Update-1");
        taskManager.updateEpic(savedEpic1);

        final Epic savedEpic2 = taskManager.getEpicById(idEpic);
        savedEpic2.setDescription(savedEpic1.getDescription() + " Update-2");
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
        savedSubtask1.setDescription(savedSubtask1.getDescription() + " Update-1");
        taskManager.updateSubtask(savedSubtask1);

        final Subtask savedSubtask2 = taskManager.getSubtaskById(idSubtask);
        savedSubtask2.setDescription(savedSubtask1.getDescription() + " Update-2");
        taskManager.updateSubtask(savedSubtask2);

        assertNotNull(savedSubtask2, "Задачи не возвращается.");
        assertEquals(savedSubtask2.getId(), savedSubtask1.getId(), "Задачи не совпадают.");
        assertNotSame(savedSubtask2,savedSubtask1);
    }

    @Test
    void addNewTask() {
        LocalDateTime start = LocalDateTime.of(2024,9,12,8,0,0);
        final Task task1 = new Task("name-1","des-1", Status.NEW,start,Duration.ZERO);

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
    void test7checkId() {
        LocalDateTime start = LocalDateTime.of(2024,9,12,8,0,0);
        Task task = new Task("T-15","DT-15",15, Status.NEW,start,Duration.ofMinutes(20));
        final int taskId = taskManager.addNewTask(task);
        taskManager.updateTask(task);
        final Task savedTask = taskManager.getTaskById(taskId);
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask);
    }

    @Test
    void test8checkField() {
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
        LocalDateTime start = LocalDateTime.of(2024,9,12,8,0,0);
        Epic epic = new Epic("E-1","DE-1",start,Duration.ZERO);

        final int epicId = taskManager.addNewEpic(epic);
        taskManager.updateEpic(epic);
        final Epic savedEpic = taskManager.getEpicById(epicId);
        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic, savedEpic, "Задачи не совпадают.");
        assertNotSame(epic, savedEpic);
    }

    @Test
    void addNewSubtask() {
        LocalDateTime start = LocalDateTime.of(2024,9,12,8,0,0);
        Subtask subtask = new Subtask("S-1","SE-1",Status.NEW,start,Duration.ZERO);

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
    void test3E() {
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
    void test4Subtask() {
        Subtask subtask = new Subtask("S-1","SE-1",Status.NEW);

        final int subtaskId = taskManager.addNewSubtask(subtask);
        final Subtask savedSubtask = taskManager.getSubtaskById(subtaskId);
        assertNotNull(savedSubtask, "Задача не найдена.");
        assertEquals(subtask, savedSubtask, "Задачи не совпадают.");
        savedSubtask.setEpicId(subtaskId);
        assertNotEquals(subtaskId, savedSubtask.getEpicId(), "Значение поля равны!");
    }

    @Test
    void test6findTask() {
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

    @Test
    void removeSubtaskFromGetHistory() {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        assertEquals(0,inMemoryTaskManager.getHistory().size(), "Список не пустой");
        Epic epic1 = new Epic("E0","DE0",0,Status.NEW);
        inMemoryTaskManager.addNewEpic(epic1);
        ArrayList<Subtask> arraySubtask = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Subtask subtask = new Subtask("S" + i,"DS" + i,i,Status.NEW,0);
            inMemoryTaskManager.addNewSubtask(subtask);
            arraySubtask.add(subtask);
        }
        epic1.setArraySubtask(arraySubtask);
        inMemoryTaskManager.addNewEpic(epic1);
        epic1 = new Epic("E6","DE6",6,Status.NEW);
        inMemoryTaskManager.addNewEpic(epic1);
        List<Task> listHistory = inMemoryTaskManager.getHistory();
        Subtask subtaskAct = (Subtask) listHistory.get(0);
        assertEquals(7,listHistory.size(), "Список не пустой");
        assertEquals(1,subtaskAct.getId(), "Список не пустой");
        assertTrue(listHistory.contains(subtaskAct));

        inMemoryTaskManager.deleteSubtask(1);
        listHistory = inMemoryTaskManager.getHistory();
        assertEquals(6,listHistory.size(), "Список не пустой");
        assertFalse(listHistory.contains(subtaskAct));
    }

    @Test
    void removeEpicFromGetHistory() {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        assertEquals(0,inMemoryTaskManager.getHistory().size(), "Список не пустой");
        Epic epic1 = new Epic("E0","DE0",0,Status.NEW);
        inMemoryTaskManager.addNewEpic(epic1);
        for (int i = 1; i <= 5; i++) {
            Subtask subtask = new Subtask("S" + i,"DS" + i,i,Status.NEW,0);
            inMemoryTaskManager.addNewSubtask(subtask);
        }

        inMemoryTaskManager.addNewEpic(epic1);
        epic1 = new Epic("E6","DE6",6,Status.NEW);
        inMemoryTaskManager.addNewEpic(epic1);

        inMemoryTaskManager.deleteEpic(0);
        List<Task> listHistory = inMemoryTaskManager.getHistory();
        assertEquals(1,listHistory.size(), "Список не пустой");
        assertEquals(6,listHistory.get(0).getId(), "Список не пустой");
    }

    @Test
    void addNewTaskMoreIdCounter() {
        final Task task1 = new Task("name-1","des-1", 10,Status.NEW);

        final int taskId = taskManager.addNewTask(task1);
        taskManager.updateTask(task1);
        final Task savedTask = taskManager.getTaskById(taskId);
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(0, savedTask.getId(), "Задачи не совпадают.");
        assertNotEquals(10, taskId);
    }

    @Test
    void addNewTaskLessIdCounter() {
        final Task task1 = new Task("name-1","des-1", -10,Status.NEW);

        final int taskId = taskManager.addNewTask(task1);
        taskManager.updateTask(task1);
        final Task savedTask = taskManager.getTaskById(taskId);
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(0, savedTask.getId(), "Задачи не совпадают.");
        assertNotEquals(-10, taskId);
    }

    @Test
    void addNewTaskIdEpic() {
        final Epic epic1 = new Epic("N-E0","D-E0");
        final int epicId = taskManager.addNewEpic(epic1);

        final Task task1 = new Task("name-1","des-1", 0,Status.NEW);
        final int taskId = taskManager.addNewTask(task1);

        taskManager.updateTask(task1);
        final Task savedTask = taskManager.getTaskById(taskId);
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(1, savedTask.getId(), "Задачи не совпадают.");
        assertNotEquals(0, taskId);
    }

    @Test
    void addTaskListPrioritized() {
        LocalDateTime start = LocalDateTime.of(2024,10,10,8,0,0);
        Duration durationT = Duration.ofMinutes(10);
        Task task = new Task("N-T0","D-T0",Status.NEW,start,durationT);
        taskManager.addNewTask(task);

        Task task1 = new Task("N-T1","D-T1",Status.IN_PROGRESS,start.plusMinutes(20),durationT);
        taskManager.addNewTask(task1);

        Subtask subtask = new Subtask("N-S2","D-S2",Status.NEW,start.plusMinutes(29),durationT);
        taskManager.addNewSubtask(subtask);

        Subtask subtask1 = new Subtask("N-S3","D-S3",Status.DONE,start.plusMinutes(30),durationT);
        taskManager.addNewSubtask(subtask1);

        Task task2 = new Task("N-T4","D-T4",Status.NEW,start.plusMinutes(50),Duration.ZERO);
        taskManager.addNewTask(task2);
        LocalDateTime startBug = LocalDateTime.parse(" 2024-10-10 08:29",task.getFormatter());
        Duration durationBug = Duration.ZERO;
        Task task3 = new Task("N-T5","D-T5",Status.NEW, startBug,durationBug);
        taskManager.addNewTask(task3);

        subtask.setName("N-S2 after update");
        taskManager.updateSubtask(subtask);
        task1.setStartTime(start.minusMinutes(30));
        taskManager.updateTask(task1);

        subtask1.setStartTime(subtask1.getStartTime().minusMinutes(30));
        subtask1.setDuration(Duration.ofMinutes(10));
        taskManager.addNewSubtask(subtask1);
        task1.setStartTime(start.plusMinutes(1));
        task1.setDuration(Duration.ofMinutes(15));
        taskManager.updateTask(task1);
        System.out.println();
        taskManager.getPrioritizedTasks().forEach(System.out::println);
        assertEquals(3,taskManager.getPrioritizedTasks().size(), "Список не пустой");
    }

    @Test
    void updateTaskListPrioritized() {
        LocalDateTime start = LocalDateTime.of(2024,10,10,8,0,0);
        Duration durationT = Duration.ofMinutes(10);
        Task task = new Task("N-T0","D-T0",Status.NEW,start,durationT);
        taskManager.addNewTask(task);
        Task task1 = new Task("N-T1","D-T1",Status.IN_PROGRESS,start.plusMinutes(20),durationT);
        taskManager.addNewTask(task1);
        Subtask subtask = new Subtask("N-S2","D-S2",Status.NEW,start.plusMinutes(35),durationT);
        taskManager.addNewSubtask(subtask);
        Subtask subtask1 = new Subtask("N-S3","D-S3",Status.DONE,start.plusMinutes(40),durationT);
        taskManager.addNewSubtask(subtask1);

        subtask.setName("N-S2 after update");
        taskManager.updateSubtask(subtask);
        task1.setStartTime(taskManager.getTaskById(1).getStartTime().minusMinutes(13));
        taskManager.updateTask(task1);

        subtask1.setStartTime(subtask1.getStartTime().plusMinutes(7));
        subtask1.setDuration(Duration.ofMinutes(10));
        taskManager.updateSubtask(subtask1);
        task1.setStartTime(start.plusMinutes(30));
        task1.setDuration(Duration.ofMinutes(15));
        taskManager.updateTask(task1);

        System.out.println();
        taskManager.getPrioritizedTasks().forEach(System.out::println);
        assertEquals(3,taskManager.getPrioritizedTasks().size(), "Список не пустой");
    }

    @Test
    void checkIntersectsFalse() {
        LocalDateTime start = LocalDateTime.of(2024,10,10,8,0,0);
        Duration durationT = Duration.ofMinutes(10);
        List<Task> listPrioritizedTasks = taskManager.getPrioritizedTasks();
        assertEquals(0,listPrioritizedTasks.size(), "Список не пустой");

        Task task = new Task("N-T0","D-T0",Status.NEW,start,durationT);
        taskManager.addNewTask(task);
        Task task1 = new Task("N-T1","D-T1",Status.IN_PROGRESS,start.plusMinutes(20),durationT);
        taskManager.addNewTask(task1);

        listPrioritizedTasks = taskManager.getPrioritizedTasks();
        assertEquals(2,listPrioritizedTasks.size(), "Список не пустой");
        String str = "Task{Id=0, name='N-T0', description='D-T0', status=NEW, startTime= 2024-10-10 08:00, duration=10}";
        assertTrue(listPrioritizedTasks.get(0).toString().contains(str));
    }

    @Test
    void checkIntersectsTrue() {
        LocalDateTime start = LocalDateTime.of(2024,10,10,8,0,0);
        Duration durationT = Duration.ofMinutes(10);
        List<Task> listPrioritizedTasks = taskManager.getPrioritizedTasks();
        assertEquals(0,listPrioritizedTasks.size(), "Список не пустой");

        Task task = new Task("N-T0","D-T0",Status.NEW,start,durationT);
        taskManager.addNewTask(task);
        Task task1 = new Task("N-T1","D-T1",Status.IN_PROGRESS,start.plusMinutes(5),durationT);
        taskManager.addNewTask(task1);

        listPrioritizedTasks = taskManager.getPrioritizedTasks();
        assertEquals(1,listPrioritizedTasks.size(), "Список не пустой");
        String str = "Task{Id=0, name='N-T0', description='D-T0', status=NEW, startTime= 2024-10-10 08:00, duration=10}";
        assertTrue(listPrioritizedTasks.get(0).toString().contains(str));
    }

    @Test
    void checkIntersectsFalseBorderline() {
        LocalDateTime start = LocalDateTime.of(2024,10,10,8,0,0);
        Duration durationT = Duration.ofMinutes(10);
        List<Task> listPrioritizedTasks = taskManager.getPrioritizedTasks();
        assertEquals(0,listPrioritizedTasks.size(), "Список не пустой");

        Task task = new Task("N-T0","D-T0",Status.NEW,start,durationT);
        taskManager.addNewTask(task);
        Task task1 = new Task("N-T1","D-T1",Status.IN_PROGRESS,start.plusMinutes(10),durationT);
        taskManager.addNewTask(task1);

        listPrioritizedTasks = taskManager.getPrioritizedTasks();
        assertEquals(2,listPrioritizedTasks.size(), "Список не пустой");
        String str = "Task{Id=0, name='N-T0', description='D-T0', status=NEW, startTime= 2024-10-10 08:00, duration=10}";
        assertTrue(listPrioritizedTasks.get(0).toString().contains(str));
    }

    @Test
    void checkIntersectsTrueDuration0() { //Borderline
        LocalDateTime start = LocalDateTime.of(2024,10,10,8,0,0);
        Duration durationT = Duration.ofMinutes(10);
        List<Task> listPrioritizedTasks = taskManager.getPrioritizedTasks();
        assertEquals(0,listPrioritizedTasks.size(), "Список не пустой");

        Task task = new Task("N-T0","D-T0",Status.NEW,start,durationT);
        taskManager.addNewTask(task);
        Task task1 = new Task("N-T1","D-T1",Status.IN_PROGRESS,start.plusMinutes(5),Duration.ZERO);
        taskManager.addNewTask(task1);

        listPrioritizedTasks = taskManager.getPrioritizedTasks();
        assertEquals(1,listPrioritizedTasks.size(), "Список не пустой");
        String str = "Task{Id=0, name='N-T0', description='D-T0', status=NEW, startTime= 2024-10-10 08:00, duration=10}";
        assertTrue(listPrioritizedTasks.get(0).toString().contains(str));
    }

    @Test
    void checkIntersectsFalseDuration0BorderlineEndTime() { //Borderline
        LocalDateTime start = LocalDateTime.of(2024,10,10,8,0,0);
        Duration durationT = Duration.ofMinutes(10);
        List<Task> listPrioritizedTasks = taskManager.getPrioritizedTasks();
        assertEquals(0,listPrioritizedTasks.size(), "Список не пустой");

        Task task = new Task("N-T0","D-T0",Status.NEW,start,durationT);
        taskManager.addNewTask(task);
        Task task1 = new Task("N-T1","D-T1",Status.IN_PROGRESS,start.plusMinutes(10),Duration.ZERO);
        taskManager.addNewTask(task1);

        listPrioritizedTasks = taskManager.getPrioritizedTasks();
        assertEquals(2,listPrioritizedTasks.size(), "Список не пустой");
        String str = "Task{Id=0, name='N-T0', description='D-T0', status=NEW, startTime= 2024-10-10 08:00, duration=10}";
        assertTrue(listPrioritizedTasks.get(0).toString().contains(str));
    }

    @Test
    void checkIntersectsTrueDuration0BorderlineStartTime() { //Borderline
        LocalDateTime start = LocalDateTime.of(2024,10,10,8,0,0);
        Duration durationT = Duration.ofMinutes(10);
        List<Task> listPrioritizedTasks = taskManager.getPrioritizedTasks();
        assertEquals(0,listPrioritizedTasks.size(), "Список не пустой");

        Task task = new Task("N-T0","D-T0",Status.NEW,start,durationT);
        taskManager.addNewTask(task);
        Task task1 = new Task("N-T1","D-T1",Status.IN_PROGRESS,start.plusMinutes(10),Duration.ofMinutes(5));
        taskManager.addNewTask(task1);

        listPrioritizedTasks = taskManager.getPrioritizedTasks();
        assertEquals(2,listPrioritizedTasks.size(), "Список не пустой");
        String str = "Task{Id=0, name='N-T0', description='D-T0', status=NEW, startTime= 2024-10-10 08:00, duration=10}";
        assertTrue(listPrioritizedTasks.get(0).toString().contains(str));
    }

    @Test
    void addTaskListPrioritized_T1() {

        LocalDateTime start = LocalDateTime.of(2024,10,10,8,0,0);
        Duration durationT = Duration.ofMinutes(10);
        assertEquals(0,taskManager.getPrioritizedTasks().size(), "Список не пустой");
        Task task = new Task("N-T0","D-T0",Status.NEW,start,durationT);
        taskManager.addNewTask(task);

        Task task1 = new Task("N-T1","D-T1",Status.NEW,start.plusMinutes(20),durationT);
        taskManager.addNewTask(task1);
        assertEquals(2,taskManager.getPrioritizedTasks().size(), "Список не пустой");
        String str = "Task{Id=0, name='N-T0', description='D-T0', status=NEW, startTime= 2024-10-10 08:00, duration=10}";
        assertTrue(taskManager.getPrioritizedTasks().get(0).toString().contains(str));
        str = "Task{Id=1, name='N-T1', description='D-T1', status=NEW, startTime= 2024-10-10 08:20, duration=10}";
        assertTrue(taskManager.getPrioritizedTasks().get(1).toString().contains(str));
        System.out.println();
        taskManager.getPrioritizedTasks().forEach(System.out::println);
    }

    @Test
    void addTaskListPrioritized_T2() {

        LocalDateTime start = LocalDateTime.of(2024,10,10,8,0,0);
        Duration durationT = Duration.ofMinutes(10);
        assertEquals(0,taskManager.getPrioritizedTasks().size(), "Список не пустой");
        Task task = new Task("N-T0","D-T0",Status.NEW,start,durationT);
        taskManager.addNewTask(task);

        Task task1 = new Task("N-T1","D-T1",Status.NEW,start.plusMinutes(0),durationT);
        taskManager.addNewTask(task1);
        assertEquals(1,taskManager.getPrioritizedTasks().size(), "Список не пустой");
        String str = "Task{Id=0, name='N-T0', description='D-T0', status=NEW, startTime= 2024-10-10 08:00, duration=10}";
        assertTrue(taskManager.getPrioritizedTasks().get(0).toString().contains(str));
        str = "Task{Id=0, name='N-T0', description='D-T0', status=NEW, startTime= 2024-10-10 08:00, duration=10}";
        assertTrue(taskManager.getPrioritizedTasks().get(0).toString().contains(str));
        System.out.println();
        taskManager.getPrioritizedTasks().forEach(System.out::println);
    }

    @Test
    void addTaskListPrioritized_T3() {
        Consumer<Task> printTask = taskPr -> System.out.print(taskPr.getClass().getName().substring(6) +
                " '" + taskPr.getName() + "'" + "[" + taskPr.getStartTime() + " -> " +
                taskPr.getStartTime().plus(taskPr.getDuration()) + "]" + " | ");
        String[] dataTest = {
                "n-0-0, 2024-12-01 08:00,20, 2024-12-01 08:00,20, s1=s2 - e1=e2 - d1=d2>0 - ,Пер",
                "n-1-1, 2024-12-01 08:00,20, 2024-12-01 08:00,15, s1=s2 - e1>e2 - d1>d2 - ,Пер",
                "n-1-2, 2024-12-01 08:00,20, 2024-12-01 08:00,25, s1=s2 - e1<e2 - d1<d2 - ,Пер",
                "n-1-3, 2024-12-01 08:00,20, 2024-12-01 08:00,0, s1=s2=e2 - e1>e2 - d1>d2 - d2=0, ",
                "n-2-1, 2024-12-01 08:00,20, 2024-12-01 07:55,25, s1>s2 - e1=e2 - d1>d2 - ,Пер",
                "n-2-2, 2024-12-01 08:00,20, 2024-12-01 08:05,15, s1<s2 - e1=e2 - d1<d2 - ,Пер",
                "n-2-3, 2024-12-01 08:00,20, 2024-12-01 08:20,0, s1<s2 - e1=e2=s2 - d1>d2 - d2=0, ",
                "n-3-1, 2024-12-01 08:00,20, 2024-12-01 08:05,10, s1<s2 - e1>e2 - d1>d2 - ,Пер",
                "n-3-2, 2024-12-01 08:00,20, 2024-12-01 08:05,0, s1<s2 - e1>e2 - d1>d2 - d2=0 - ,Пер",
                "n-4-1, 2024-12-01 08:00,20, 2024-12-01 07:55,30, s1>s2 - e1<e2 - d1<d2 - ,Пер",
                "n-4-2, 2024-12-01 08:00,20, 2024-12-01 07:55,20, s1>s2 - e1>e2 - d1=d2 - ,Пер",
                "n-4-3, 2024-12-01 08:00,20, 2024-12-01 08:05,20, s1<s2 - e1<e2 - d1=d2 - ,Пер",
                "n-4-4, 2024-12-01 08:00,0, 2024-12-01 08:00,0, s1=s2=e1=e2 - d1=d2=0 - , ",
                "n-4-5, 2024-12-01 08:00,0, 2024-12-01 08:10,0, s1=e1 - s2=e2 - d1=d2=0 - s1 != s2, "
                };
        String protol = "Пер-Пер-Пер-0-Пер-Пер-0-Пер-Пер-Пер-Пер-Пер-0-0-|";
        StringBuilder result = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(" yyyy-MM-dd HH:mm");

        for (String elemTest : dataTest) {
            String[] inputArg = elemTest.split(",");
            LocalDateTime start0 = LocalDateTime.parse(inputArg[1], formatter);
            Duration duration0 = Duration.ofMinutes(Long.parseLong(inputArg[2]));
            LocalDateTime start1 = LocalDateTime.parse(inputArg[3], formatter);
            Duration duration1 = Duration.ofMinutes(Long.parseLong(inputArg[4]));
            assertEquals(0, taskManager.getPrioritizedTasks().size(), "Список не пустой");

            Task task = new Task("N-T0", "D-T0", Status.NEW, start0, duration0);
            taskManager.addNewTask(task);
            Task task1 = new Task("N-T1", "D-T1", Status.NEW, start1, duration1);
            taskManager.addNewTask(task1);
            List<Task> taskList = taskManager.getPrioritizedTasks();
            taskList.forEach(printTask);

            if (taskList.size()==2) {
                result.append("0-");
                boolean flag = " ".equals(inputArg[6]);
                if (flag) {
                    System.out.println("| " + Boolean.toString(flag));
                } else {
                    System.out.println(inputArg[0] + " ->| " + Boolean.toString(flag));
                }
            }
            if (taskList.size()==1) {
                result.append("Пер-");
                boolean flag = "Пер".equals(inputArg[6]);
                if (flag) {
                    System.out.println("| " + Boolean.toString(flag));
                } else {
                    System.out.println(inputArg[0] + " формула " + inputArg[5] + inputArg[6] + " | " +
                            Boolean.toString(flag));
                }
            }
            taskManager.deleteTasks();
        }
        result.append("|");
        assertTrue(result.toString().equals(protol));
    }

    @Test
    void addTaskListPrioritized_T4() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(" yyyy-MM-dd HH:mm");
        LocalDateTime start0 = LocalDateTime.parse(" 2024-12-01 08:00", formatter);
        //Duration duration1 = Duration.ofMinutes(Long.parseLong(inputArg[4]));
        assertEquals(0, taskManager.getPrioritizedTasks().size(), "Список не пустой");

        Task task = new Task("N-T0", "D-T0", Status.NEW, start0, null);
        taskManager.addNewTask(task);
        Task task1 = new Task("N-T1", "D-T1", Status.NEW, start0, Duration.ofMinutes(7));
        taskManager.addNewTask(task1);
        List<Task> taskList = taskManager.getHistory();
        assertEquals(0, taskList.get(0).getDuration().toMinutes());
        assertEquals(7, taskList.get(1).getDuration().toMinutes());
    }

}