import service.Status;

public class Main {

    public static void main(String[] args) throws ClassNotFoundException {
        System.out.println("Поехали!");
        TaskManager taskManager = new TaskManager();
        taskManager.initTaskManager();

        taskManager.printAllTasks();
        System.out.println();
        taskManager.getSubtaskById(3).setStatus(Status.IN_PROGRESS);
        taskManager.updateSubtask((taskManager.getSubtaskById(3)));
        taskManager.getSubtaskById(4).setStatus(Status.DONE);
        taskManager.updateSubtask((taskManager.getSubtaskById(4)));
        taskManager.getTaskById(1).setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(taskManager.getTaskById(1));
        taskManager.deleteSubtask(3);
        taskManager.deleteTask(0);
        taskManager.deleteEpic(5);
        taskManager.printAllTasks();
        Epic ep = new Epic(taskManager.getEpicById(2),true);
        taskManager.addNewEpic(ep);
        taskManager.printAllTasks();
    }
}
