import service.Status;

public class Main {

    public static void main(String[] args) throws ClassNotFoundException {
        System.out.println("Поехали!");
        TaskManager taskManager = new TaskManager();
        //taskManager.initTaskManager();
        taskManager.addNewTask(new Task("name 1", "description 1", Status.NEW));
        taskManager.addNewTask(new Task("name 2", "description 2", Status.NEW));
        Epic epic = new Epic("name E1", "description E1", Status.NEW);
        int epicId = taskManager.addNewEpic(epic);
        taskManager.addNewSubtask(new Subtask("name S1", "description S1", Status.NEW, epicId));
        taskManager.addNewSubtask(new Subtask("name S2", "description S2", Status.NEW, epicId));
        taskManager.updateEpic(epic);
        epic = new Epic("name E2", "description E2", Status.NEW);
        epicId = taskManager.addNewEpic(epic);
        taskManager.addNewSubtask(new Subtask("name S3", "description S3", Status.NEW, epicId));
        taskManager.updateEpic(epic);




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
