package controllers;

import model.Epic;
import model.Subtask;
import model.Task;
import service.Status;

public final class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static void testManagers(TaskManager taskManager) {
        fillManagers(taskManager);
        printAllTasks(taskManager);
    }

    private static void fillManagers(TaskManager manager) {
        Task task = new Task("name T0", "T0", Status.NEW);
        manager.addNewTask(task);

        task = new Task("name T1", "T1", Status.NEW);
        manager.addNewTask(task);

        Epic epic = new Epic("name E2", "E2");
        int epicId = manager.addNewEpic(epic);
        Subtask subtask = new Subtask("name S3", "S3", Status.NEW, epicId);
        manager.addNewSubtask(subtask);

        subtask = new Subtask("name S4", "S4", Status.NEW, epicId);
        manager.addNewSubtask(subtask);

        manager.updateEpic(epic);

        epic = new Epic("name E5", "E5");
        epicId = manager.addNewEpic(epic);
        subtask = new Subtask("name S6", "S6", Status.NEW, epicId);
        manager.addNewSubtask(subtask);

        manager.updateEpic(epic);
    }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getListTask()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Task epic : manager.getListEpic()) {
            System.out.println(epic);

            for (Task task : manager.getEpicSubtasks(epic.getId())) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getListSubtask()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }
}
