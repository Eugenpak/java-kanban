import controllers.*;
import model.Epic;
import model.Subtask;
import model.Task;
import service.Status;



public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager taskManager = Managers.getDefault();

        fillManagers(taskManager);
        printAllEpicsSubtask(taskManager);

        //Запрос задач в порядке 1
        System.out.println();
        System.out.println("Запрос задач в порядке 1");
        taskManager.getEpicById(4);
        taskManager.getSubtaskById(3);
        taskManager.getSubtaskById(2);
        taskManager.getEpicById(0);
        taskManager.getSubtaskById(1);
        printAllHistory(taskManager);
        System.out.println();
        System.out.println("Запрос задач в порядке 2");
        taskManager.getSubtaskById(1);
        taskManager.getEpicById(0);
        taskManager.getEpicById(4);
        taskManager.getSubtaskById(2);
        taskManager.getSubtaskById(3);
        printAllHistory(taskManager);
        System.out.println();
        System.out.println("Удаление Epic с id=4.");
        taskManager.deleteEpic(4);
        printAllHistory(taskManager);
        System.out.println();
        System.out.println("Удаление Subtask с id=1.");
        taskManager.deleteSubtask(1);
        printAllHistory(taskManager);
        System.out.println();
        System.out.println("Удаление Epic с id=0.");
        taskManager.deleteEpic(0);
        printAllHistory(taskManager);
    }

    private static void printAllHistory(TaskManager manager) {
        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }

    private static void printAllEpicsSubtask(TaskManager manager) {
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

    private static void fillManagers(TaskManager manager) {

        Epic epic = new Epic("E-0","DE-0");
        int epicId0 = manager.addNewEpic(epic);
        Subtask subtask =new Subtask("S-1","SE-1",Status.NEW);
        int subtaskId = manager.addNewSubtask(subtask);
        manager.setEpicSubtask(epicId0,subtaskId);
        subtask =new Subtask("S-2","SE-2",Status.NEW);
        subtaskId = manager.addNewSubtask(subtask);
        manager.setEpicSubtask(epicId0,subtaskId);
        subtask =new Subtask("S-3","SE-3",Status.IN_PROGRESS);
        subtaskId = manager.addNewSubtask(subtask);
        manager.setEpicSubtask(epicId0,subtaskId);
        manager.updateEpic(manager.getEpicById(epicId0));
        epic = new Epic("E-4","DE-4");
        int epicId1 = manager.addNewEpic(epic);
        manager.updateEpic(manager.getEpicById(epicId1));
    }




    public static void userScenarioOld(){
        TaskManager taskManager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();

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
        historyManager.getHistory();
        printAllTasks(taskManager);
        System.out.println();
        subtask = taskManager.getSubtaskById(3);
        subtask.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubtask(subtask);
        subtask = taskManager.getSubtaskById(4);
        subtask.setStatus(Status.DONE);
        taskManager.updateSubtask(subtask);
        task=taskManager.getTaskById(1);
        task.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(task);
        taskManager.deleteSubtask(3);
        taskManager.deleteTask(0);
        taskManager.deleteEpic(5);
        printAllTasks(taskManager);
        Epic ep = new Epic(taskManager.getEpicById(2),true);
        taskManager.addNewEpic(ep);
        subtask =taskManager.getSubtaskById(8);
        subtask.setStatus(Status.IN_PROGRESS);
        taskManager.updateEpic(ep);
        taskManager.deleteSubtasks();
        printAllTasks(taskManager);
        taskManager.deleteEpics();
        taskManager.deleteTasks();
        printAllTasks(taskManager);
    }

    public static void printAllTasks(TaskManager taskManager) {
        System.out.println("Печать полного списка задач");
        for (Task elem : taskManager.getListTask()){
            System.out.println(elem);
        }
        for (Epic elem : taskManager.getListEpic()){
            printEpicSubtask(taskManager,elem.getId());
        }
        for (Subtask elem : taskManager.getListSubtask()){
            System.out.println(elem);
        }
        System.out.println();
    }

    public static void printEpicSubtask(TaskManager taskManager, Integer idEpic) {
        Epic epic = taskManager.getEpicById(idEpic);

        System.out.println("Epic{id=" + epic.getId() + ", name='" + epic.getName() +
                "', description.length =" + epic.getDescription().length() +
                ", status='" + epic.getStatus() + "' ArraySubtask[" + epic.getArraySubtask().size() + "]}");
        for (Subtask elem : taskManager.getEpicSubtasks(idEpic)){
            System.out.println(" --> " + elem);
        }
    }
}
