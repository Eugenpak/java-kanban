package controllers;

import model.Epic;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {

    @Test
    void updateTask() {
    }

    @Test
    void updateEpic() {
    }

    @Test
    void updateSubtask() {
    }

    @Test
    void addNewTask() {
    }

    @Test
    void addNewEpic() {

    }

    @Test
    void addNewSubtask() {
    }

    @Test
    void deleteTask() {
    }

    @Test
    void deleteEpic() {
    }

    @Test
    void deleteSubtask() {
    }

    @Test
    void deleteTasks() {
    }

    @Test
    void deleteEpics() {
    }

    @Test
    void deleteSubtasks() {
    }

    @Test
    void findId() {
    }

    @Test
    void getTaskById() {
    }

    @Test
    void getSubtaskById() {
    }

    @Test
    void getEpicById() {
        try {
            File file = File.createTempFile("_test_","_getEpicById.csv");
            FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file.toString());
            Epic epic = new Epic("N-E2", "D-E2");
            int epicId = fileBackedTaskManager.addNewEpic(epic);
            int findEpicId = fileBackedTaskManager.getEpicById(epicId).getId();
            assertEquals(0,findEpicId, "Список не пустой");

            assertNull(fileBackedTaskManager.getEpicById(100), "Вернул не null!");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void getEpicByIdNull() {
        try {
            File file = File.createTempFile("_test_","_fileBackedTaskManager.csv");
            FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file.toString());
            Epic epic = new Epic("N-E2", "D-E2");
            int epicId = fileBackedTaskManager.addNewEpic(epic);
            int findEpicId = fileBackedTaskManager.getEpicById(epicId).getId();
            assertEquals(0,findEpicId, "Список не пустой");

            //assertNull(fileBackedTaskManager.getEpicById(100), "Вернул не null!");
            fileBackedTaskManager.getEpicById(100).getId();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


}