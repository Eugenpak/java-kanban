package controllers;

import model.Task;

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
