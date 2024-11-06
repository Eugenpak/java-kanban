package controllers;

public final class Managers {
    private static TaskManager taskManager = new InMemoryTaskManager();

    public static TaskManager getDefault() {
        return taskManager;
    }

    public static HistoryManager getDefaultHistory() {
        return (HistoryManager) taskManager.getHistory();
    }

}
