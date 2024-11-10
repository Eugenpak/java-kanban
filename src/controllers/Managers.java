package controllers;

import model.Epic;
import model.Subtask;
import model.Task;
import service.Status;

public final class Managers {
    private final static TaskManager taskManager;// = new InMemoryTaskManager();
    private final static HistoryManager historyManager;// = new InMemoryHistoryManager();
    static {
        historyManager = new InMemoryHistoryManager();
        taskManager = new InMemoryTaskManager();
    }
    public static TaskManager getDefault() {
        return taskManager;
    }

    public static HistoryManager getDefaultHistory() {
        return historyManager;
    }

    public static void testManagers(){
        TaskManager taskManager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();
        fillManagers(taskManager);
        printAllTasks(taskManager);
    }
    private static void fillManagers(TaskManager manager) {
        Task task = new Task("name 1", "T0", Status.NEW);
        taskManager.addNewTask(task);

        task = new Task("name 2", "T1", Status.NEW);
        taskManager.addNewTask(task);

        Epic epic = new Epic("name E1", "E2");
        int epicId = taskManager.addNewEpic(epic);
        Subtask subtask = new Subtask("name S1", "S3", Status.NEW, epicId);
        taskManager.addNewSubtask(subtask);

        subtask = new Subtask("name S2", "S4", Status.NEW, epicId);
        taskManager.addNewSubtask(subtask);

        taskManager.updateEpic(epic);

        epic = new Epic("name E2", "E5");
        epicId = taskManager.addNewEpic(epic);
        subtask = new Subtask("name S3", "S6", Status.NEW, epicId);
        taskManager.addNewSubtask(subtask);

        taskManager.updateEpic(epic);
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
