package controllers;

import model.Epic;
import model.Subtask;
import model.Task;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import service.Status;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {

    @Test
    void updateTask() {
        try {
            File file = File.createTempFile("_test_","_updateT.csv");
            FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file.toString());
            List<Task> listTask = fileBackedTaskManager.getListTask();
            assertEquals(0,listTask.size(), "Список не пустой");

            Task task = new Task("N-T0", "D-T0",Status.NEW);
            fileBackedTaskManager.addNewTask(task);
            listTask = fileBackedTaskManager.getListTask();
            assertEquals(1,listTask.size(), "Список не пустой");
            assertNotNull(fileBackedTaskManager.findId(0), "Объект не найден.");

            Task findedTask = fileBackedTaskManager.findId(0);
            findedTask.setName("N-T0 after update");
            fileBackedTaskManager.updateTask(findedTask);

            assertNotNull(fileBackedTaskManager.findId(0), "Объект не найден.");
            assertTrue(listTask.get(0).toString().contains("Task{Id=0, name='N-T0 after update', description='D-T0'"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void updateEpic() {
        try {
            File file = File.createTempFile("_test_","_updateE.csv");
            FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file.toString());
            List<Epic> listEpic = fileBackedTaskManager.getListEpic();
            assertEquals(0,listEpic.size(), "Список не пустой");

            Epic epic = new Epic("N-E0", "D-E0",Status.NEW);
            fileBackedTaskManager.addNewEpic(epic);
            listEpic = fileBackedTaskManager.getListEpic();
            assertEquals(1,listEpic.size(), "Список не пустой");
            assertNotNull(fileBackedTaskManager.findId(0), "Объект не найден.");

            Epic findedEpic = (Epic) fileBackedTaskManager.findId(0);
            findedEpic.setName("N-E0 after update");
            fileBackedTaskManager.updateTask(findedEpic);

            assertTrue(listEpic.get(0).toString().contains("Epic{Id=0, name='N-E0 after update', description='D-E0'"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void updateSubtask() {
        try {
            File file = File.createTempFile("_test_","_updateS.csv");
            FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file.toString());
            List<Subtask> listSubtask = fileBackedTaskManager.getListSubtask();
            assertEquals(0,listSubtask.size(), "Список не пустой");

            Subtask subtask = new Subtask("N-S0", "D-S0",Status.NEW);
            fileBackedTaskManager.addNewSubtask(subtask);
            listSubtask = fileBackedTaskManager.getListSubtask();
            assertEquals(1,listSubtask.size(), "Список не пустой");
            assertNotNull(fileBackedTaskManager.findId(0), "Объект не найден.");

            Subtask findedSubtask = (Subtask) fileBackedTaskManager.findId(0);
            findedSubtask.setName("N-S0 after update");
            fileBackedTaskManager.updateTask(findedSubtask);

            assertTrue(listSubtask.get(0).toString().contains("Subtask{id=0, name='N-S0 after update', description='D-S0'"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void addNewTask() {
        try {
            File file = File.createTempFile("_test_","_addT.csv");
            FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file.toString());
            List<Task> listTask = fileBackedTaskManager.getListTask();
            assertEquals(0,listTask.size(), "Список не пустой");

            Task task = new Task("N-T0", "D-T0",Status.NEW);
            fileBackedTaskManager.addNewTask(task);

            listTask = fileBackedTaskManager.getListTask();
            assertEquals(1,listTask.size(), "Список не пустой");
            assertNotNull(fileBackedTaskManager.findId(0), "Объект не найден.");
            assertTrue(listTask.get(0).toString().contains("Task{Id=0, name='N-T0', description='D-T0'"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void addNewEpic() {
        try {
            File file = File.createTempFile("_test_","_addE.csv");
            FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file.toString());
            List<Epic> listEpic = fileBackedTaskManager.getListEpic();
            assertEquals(0,listEpic.size(), "Список не пустой");

            Epic epic = new Epic("N-E0", "D-E0",Status.NEW);
            fileBackedTaskManager.addNewEpic(epic);

            listEpic = fileBackedTaskManager.getListEpic();
            assertEquals(1,listEpic.size(), "Список не пустой");
            assertNotNull(fileBackedTaskManager.findId(0), "Объект не найден.");
            assertTrue(listEpic.get(0).toString().contains("Epic{Id=0, name='N-E0', description='D-E0'"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    @Test
    void addNewSubtask() {
        try {
            File file = File.createTempFile("_test_","_addS.csv");
            FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file.toString());
            List<Subtask> listSubtask = fileBackedTaskManager.getListSubtask();
            assertEquals(0,listSubtask.size(), "Список не пустой");

            Subtask subtask = new Subtask("N-S0", "D-S0",Status.NEW);
            fileBackedTaskManager.addNewSubtask(subtask);

            listSubtask = fileBackedTaskManager.getListSubtask();
            assertEquals(1,listSubtask.size(), "Список не пустой");
            assertNotNull(fileBackedTaskManager.findId(0), "Объект не найден.");
            assertTrue(listSubtask.get(0).toString().contains("Subtask{id=0, name='N-S0', description='D-S0', status=NEW"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void deleteTaskById() {
        try {
            File file = File.createTempFile("_test_","_getTaskById.csv");
            FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file.toString());
            List<Task> listTask = fileBackedTaskManager.getListTask();
            assertEquals(0,listTask.size(), "Список не пустой");

            Task task = new Task("N-T0", "D-T0",Status.NEW);
            fileBackedTaskManager.addNewTask(task);
            task = new Task("N-T1", "D-T1",Status.DONE);
            fileBackedTaskManager.addNewTask(task);
            task = new Task("N-T2", "D-T2",Status.NEW);
            fileBackedTaskManager.addNewTask(task);
            listTask = fileBackedTaskManager.getListTask();
            assertEquals(3,listTask.size(), "Список не пустой");
            assertNotNull(fileBackedTaskManager.findId(1), "Объект не найден.");
            fileBackedTaskManager.deleteTask(1);

            listTask = fileBackedTaskManager.getListTask();
            assertEquals(2,listTask.size(), "Список не пустой");
            assertNull(fileBackedTaskManager.findId(1), "Объект найден.");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void deleteEpicById() {
        try {
            File file = File.createTempFile("_test_","_deleteEpicById.csv");
            FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file.toString());
            List<Epic> listEpic = fileBackedTaskManager.getListEpic();
            assertEquals(0,listEpic.size(), "Список не пустой");

            Epic epic = new Epic("N-E0", "D-E0",Status.NEW);
            fileBackedTaskManager.addNewEpic(epic);
            epic = new Epic("N-E1", "D-E1",Status.NEW);
            fileBackedTaskManager.addNewEpic(epic);
            epic = new Epic("N-E2", "D-E2",Status.NEW);
            fileBackedTaskManager.addNewEpic(epic);
            listEpic = fileBackedTaskManager.getListEpic();
            assertEquals(3,listEpic.size(), "Список не пустой");
            assertNotNull(fileBackedTaskManager.findId(1), "Объект не найден.");
            fileBackedTaskManager.deleteEpic(1);

            listEpic = fileBackedTaskManager.getListEpic();
            assertEquals(2,listEpic.size(), "Список не пустой");
            assertNull(fileBackedTaskManager.findId(1), "Объект найден.");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void deleteSubtaskById() {
        try {
            File file = File.createTempFile("_test_","_deleteSubtaskById.csv");
            FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file.toString());
            List<Subtask> listSubtask = fileBackedTaskManager.getListSubtask();
            assertEquals(0,listSubtask.size(), "Список не пустой");

            Subtask subtask = new Subtask("N-S0", "D-S0",Status.NEW);
            fileBackedTaskManager.addNewSubtask(subtask);
            subtask = new Subtask("N-S1", "D-S1",Status.NEW);
            fileBackedTaskManager.addNewSubtask(subtask);
            subtask = new Subtask("N-S2", "D-S2",Status.NEW);
            fileBackedTaskManager.addNewSubtask(subtask);
            listSubtask = fileBackedTaskManager.getListSubtask();
            assertEquals(3,listSubtask.size(), "Список не пустой");
            assertNotNull(fileBackedTaskManager.findId(1), "Объект не найден.");
            fileBackedTaskManager.deleteSubtask(1);

            listSubtask = fileBackedTaskManager.getListSubtask();
            assertEquals(2,listSubtask.size(), "Список не пустой");
            assertNull(fileBackedTaskManager.findId(1), "Объект найден.");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void deleteTasks() {
        try {
            File file = File.createTempFile("_test_","_deleteTs.csv");
            FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file.toString());
            List<Task> listTask = fileBackedTaskManager.getListTask();
            assertEquals(0,listTask.size(), "Список не пустой");

            Task task = new Task("N-T0", "D-T0",Status.NEW);
            fileBackedTaskManager.addNewTask(task);
            task = new Task("N-T1", "D-T1",Status.NEW);
            fileBackedTaskManager.addNewTask(task);
            listTask = fileBackedTaskManager.getListTask();
            assertEquals(2,listTask.size(), "Список не пустой");

            fileBackedTaskManager.deleteTasks();
            listTask = fileBackedTaskManager.getListTask();
            assertEquals(0,listTask.size(), "Список не пустой");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void deleteEpics() {
        try {
            File file = File.createTempFile("_test_","_deleteEs.csv");
            FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file.toString());
            List<Epic> listEpic = fileBackedTaskManager.getListEpic();
            assertEquals(0,listEpic.size(), "Список не пустой");

            Epic epic = new Epic("N-E0", "D-E0",Status.NEW);
            fileBackedTaskManager.addNewEpic(epic);
            epic = new Epic("N-E1", "D-E1",Status.NEW);
            fileBackedTaskManager.addNewEpic(epic);
            epic = new Epic("N-E2", "D-E2",Status.NEW);
            fileBackedTaskManager.addNewEpic(epic);
            listEpic = fileBackedTaskManager.getListEpic();
            assertEquals(3,listEpic.size(), "Список не пустой");
            fileBackedTaskManager.deleteEpics();

            listEpic = fileBackedTaskManager.getListEpic();
            assertEquals(0,listEpic.size(), "Список не пустой");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void deleteSubtasks() {
        try {
            File file = File.createTempFile("_test_","_deleteSs.csv");
            FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file.toString());
            List<Subtask> listSubtask = fileBackedTaskManager.getListSubtask();
            assertEquals(0,listSubtask.size(), "Список не пустой");

            Subtask subtask = new Subtask("N-S2", "D-S2",Status.NEW);
            fileBackedTaskManager.addNewSubtask(subtask);
            subtask = new Subtask("N-S3", "D-S3",Status.NEW);
            fileBackedTaskManager.addNewSubtask(subtask);
            subtask = new Subtask("N-S4", "D-S4",Status.NEW);
            fileBackedTaskManager.addNewSubtask(subtask);
            listSubtask = fileBackedTaskManager.getListSubtask();
            assertEquals(3,listSubtask.size(), "Список не пустой");
            fileBackedTaskManager.deleteSubtasks();

            listSubtask = fileBackedTaskManager.getListSubtask();
            assertEquals(0,listSubtask.size(), "Список не пустой");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void findId() {
        try {
            File file = File.createTempFile("_test_","_findId.csv");
            FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file.toString());
            Task task = new Task("N-T0", "D-T0",Status.NEW);
            int taskId = fileBackedTaskManager.addNewTask(task);

            Task findedTask = fileBackedTaskManager.findId(taskId);

            assertEquals(0,findedTask.getId(), "Список не пустой");
            assertTrue(findedTask.toString().contains("Task{Id=0, name='N-T0', description='D-T0', status=NEW}"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void getTaskById() {
        try {
            File file = File.createTempFile("_test_","_getTaskById.csv");
            FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file.toString());
            Task task = new Task("N-T0", "D-T0",Status.NEW);
            int taskId = fileBackedTaskManager.addNewTask(task);
            Task findedTask = fileBackedTaskManager.getTaskById(taskId);
            assertEquals(0,findedTask.getId(), "Список не пустой");
            assertTrue(findedTask.toString().contains("Task{Id=0, name='N-T0', description='D-T0', status=NEW}"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void getSubtaskById() {
        try {
            File file = File.createTempFile("_test_","_getSubtaskById.csv");
            FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file.toString());
            Subtask subtask = new Subtask("N-S2", "D-S2",Status.NEW);
            int subtaskId = fileBackedTaskManager.addNewSubtask(subtask);
            Subtask findedSubtask = fileBackedTaskManager.getSubtaskById(subtaskId);
            assertEquals(0,findedSubtask.getId(), "Список не пустой");
            assertTrue(findedSubtask.toString().contains("Subtask{id=0, name='N-S2', description='D-S2', status=NEW"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void getEpicByIdReturn0() {
        try {
            File file = File.createTempFile("_test_","_getEpicByIdReturn0.csv");
            FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file.toString());
            Epic epic = new Epic("N-E2", "D-E2");
            int epicId = fileBackedTaskManager.addNewEpic(epic);
            int findEpicId = fileBackedTaskManager.getEpicById(epicId).getId();
            assertEquals(0,findEpicId, "Список не пустой");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void getEpicByIdNull() {
        try {
            File file = File.createTempFile("_test_","_getEpicByIdNull.csv");
            FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file.toString());
            Epic epic = new Epic("N-E2", "D-E2");
            int epicId = fileBackedTaskManager.addNewEpic(epic);
            int findEpicId = fileBackedTaskManager.getEpicById(epicId).getId();
            assertEquals(0,findEpicId, "Список не пустой");

            assertNull(fileBackedTaskManager.getEpicById(100), "Вернул не null!");
            //fileBackedTaskManager.getEpicById(100).getId();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void saveEmptyFileAndLoad() {
        try {
            File file = File.createTempFile("_test_","_saveEmptyFileAndLoad.csv");
            FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file.toString());
            List<Task> list1 = fileBackedTaskManager.getHistory();
            assertEquals(0,list1.size(), "Список не пустой");
            assertEquals(0,fileBackedTaskManager.getIdCounterFile(), "Список не пустой");

            assertTrue(file.exists(), "Файл не существует.");

            FileBackedTaskManager fileBackedTaskManager2 = FileBackedTaskManager.loadFromFile(file);
            List<Task> list2 = fileBackedTaskManager2.getHistory();
            assertEquals(0,list2.size(), "Список не пустой");
            assertEquals(0,fileBackedTaskManager2.getIdCounterFile(), "Список не пустой");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void saveTasks() {
        String fileSave ="";
        try {
            File file = File.createTempFile("_test_","_saveTasks.csv");
            fileSave = file.toString();
            FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file.toString());
            Task task = new Task("N-T0", "D-T0", Status.NEW);
            fileBackedTaskManager.addNewTask(task);

            Epic epic = new Epic("N-E1", "D-E1");
            int epicId = fileBackedTaskManager.addNewEpic(epic);
            Subtask subtask = new Subtask("N-S2", "D-S2", Status.IN_PROGRESS, epicId);
            fileBackedTaskManager.addNewSubtask(subtask);
            fileBackedTaskManager.updateEpic(epic);

            List<Task> list1 = fileBackedTaskManager.getHistory();
            assertEquals(3,list1.size(), "Список не пустой");
            assertEquals(3,fileBackedTaskManager.getIdCounterFile(), "Список не пустой");
            assertTrue(file.exists(), "Файл не существует.");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        String epicStr = "";
        try (FileReader reader = new FileReader(fileSave);
             BufferedReader br = new BufferedReader(reader)) {
            while (br.ready()) {
                String line = br.readLine();
                if (line.contains("EPIC")) {
                    epicStr = line;
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        assertTrue(epicStr.equals("1,EPIC,N-E1,IN_PROGRESS,D-E1"), "Строки не одинаковые.");
    }

    @Test
    void loadFromFileTasks() {
        String dataFile =
                        "idCounter = 8\n" +
                        "id,type,name,status,description,epicId\n" +
                        "2,EPIC,N-E2,IN_PROGRESS,D-E2\n" +
                        "7,EPIC,N-E7,NEW,D-E7\n" +
                        "id,type,name,status,description,epicId\n" +
                        "0,TASK,N-T0,NEW,D-T0\n" +
                        "3,SUBTASK,N-S3,NEW,D-S3,2\n" +
                        "4,SUBTASK,N-S4,DONE,D-S4,2\n" +
                        "2,EPIC,N-E2,IN_PROGRESS,D-E2\n" +
                        "7,EPIC,N-E7,NEW,D-E7\n";
        String fileSave ="";
        try {
            File file = File.createTempFile("_test_","_loadFromFileTasks.csv");
            fileSave = file.toString();

            FileWriter writer = new FileWriter(fileSave, StandardCharsets.UTF_8);
            BufferedWriter bw = new BufferedWriter(writer);
            bw.write(dataFile);
            bw.close();
            writer.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        FileBackedTaskManager fileBackedTaskManager = FileBackedTaskManager.loadFromFile(new File(fileSave));
        List<Task> list1 = fileBackedTaskManager.getHistory();
        assertEquals(5,list1.size(), "Список не пустой");
        assertEquals(8,fileBackedTaskManager.getIdCounterFile(), "Список не пустой");
        Epic epic2 = (Epic) list1.get(3);
        assertEquals(2,epic2.getId(), "id разные.");
        assertTrue(epic2.toString().contains("Epic{Id=2, name='N-E2', description='D-E2', status=IN_PROGRESS"));
    }
}