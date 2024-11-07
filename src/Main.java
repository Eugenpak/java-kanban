import controllers.*;
import model.Epic;
import model.Subtask;
import model.Task;
import service.Status;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");

        TaskManager taskManager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();

        //InMemoryHistoryManager historyMan = new InMemoryHistoryManager();
        List<Task> arrList = historyManager.getHistory();

        Task task = new Task("name 1", "description 1", Status.NEW);
        taskManager.addNewTask(task);
        //historyMan.add(task);
        task = new Task("name 2", "description 2", Status.NEW);
        taskManager.addNewTask(task);
        //historyMan.add(task);
        Epic epic = new Epic("name E1", "description E1");
        int epicId = taskManager.addNewEpic(epic);
        Subtask subtask = new Subtask("name S1", "description S1", Status.NEW, epicId);
        taskManager.addNewSubtask(subtask);
        //historyMan.add(subtask);
        subtask = new Subtask("name S2", "description S2", Status.NEW, epicId);
        taskManager.addNewSubtask(subtask);
        //historyMan.add(subtask);
        taskManager.updateEpic(epic);
        //historyMan.add(epic);
        epic = new Epic("name E2", "description E2");
        epicId = taskManager.addNewEpic(epic);
        subtask = new Subtask("name S3", "description S3", Status.NEW, epicId);
        taskManager.addNewSubtask(subtask);
        //historyMan.add(subtask);
        taskManager.updateEpic(epic);
        //historyMan.add(epic);
        historyManager.getHistory();

        printAllTasks(taskManager);
        System.out.println();
        subtask = taskManager.getSubtaskById(3);
        subtask.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubtask(subtask);
        arrList = historyManager.getHistory();
        //historyMan.add(subtask);
        subtask = taskManager.getSubtaskById(4);
        subtask.setStatus(Status.DONE);

        taskManager.updateSubtask(subtask);
        //historyMan.add(subtask);
        task=taskManager.getTaskById(1);
        task.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(task);
        //historyMan.add(subtask);
        taskManager.deleteSubtask(3);
        taskManager.deleteTask(0);
        taskManager.deleteEpic(5);
        printAllTasks(taskManager);
        Epic ep = new Epic(taskManager.getEpicById(2),true);
        taskManager.addNewEpic(ep);
        //historyMan.add(ep);
        arrList = historyManager.getHistory();;
        subtask =taskManager.getSubtaskById(8);
        subtask.setStatus(Status.IN_PROGRESS);
        //historyMan.add(subtask);
        taskManager.updateEpic(ep);
        arrList = historyManager.getHistory();
        taskManager.deleteSubtasks();
        arrList = historyManager.getHistory();
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
