package controllers;

import exceptions.ManagerSaveException;
import model.Epic;
import model.Subtask;
import model.Task;
import service.Status;
import service.TypeTask;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager{
    private final String filename;

    public FileBackedTaskManager(String filename) {
        this.filename = filename;
    }

    @Override
    public boolean updateTask(Task task) {
        boolean k = super.updateTask(task);
        save();
        return k;
    }

    @Override
    public boolean updateEpic(Epic epic) {
        boolean k = super.updateEpic(epic);
        save();
        return k;
    }

    @Override
    public boolean updateSubtask(Subtask subtask) {
        boolean k = super.updateSubtask(subtask);
        save();
        return k;
    }

    @Override
    public int addNewTask(Task task) throws NullPointerException {
        int k = super.addNewTask(task);
        save();
        return k;
    }

    @Override
    public int addNewEpic(Epic epic) throws NullPointerException {
        int k = super.addNewEpic(epic);
        save();
        return k;
    }

    @Override
    public int addNewSubtask(Subtask subtask) throws NullPointerException {
        int k = super.addNewSubtask(subtask);
        save();
        return k;
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
        save();
    }

    @Override
    public void deleteTasks() {
        super.deleteTasks();
        save();
    }

    @Override
    public void deleteEpics() {
        super.deleteEpics();
        save();
    }

    @Override
    public void deleteSubtasks() {
        super.deleteSubtasks();
        save();
    }

    @Override
    public Task findId(int id) {
        Task task = super.findId(id);
        save();
        return task;
    }

    @Override
    public Task getTaskById(Integer id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Subtask getSubtaskById(Integer id) {
        Subtask subtask = super.getSubtaskById(id);
        save();
        return subtask;
    }

    @Override
    public Epic getEpicById(Integer id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    void save() {
        try (FileWriter writer = new FileWriter(filename, StandardCharsets.UTF_8);
             BufferedWriter bw = new BufferedWriter(writer)) {
            bw.write("idCounter = " + getIdCounterFile() + "\n");
            bw.write("id,type,name,status,description,epicId\n");
            List<Task> taskList = getHistory();
            for (Task elem : getListEpic()) {
                bw.write(toString(elem) + '\n');
            }
            bw.write("id,type,name,status,description,epicId\n");
            for (Task elem : taskList) {
                bw.write(toString(elem) + '\n');
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка во время записи файла.");
        }
    }

    private String toString(Task task) {
        StringBuilder strB = new StringBuilder();
        if (task instanceof Subtask) {
            Subtask subtask = (Subtask) task;
            strB.append(subtask.getId());
            strB.append(',');
            strB.append(TypeTask.SUBTASK);
            strB.append(',' + subtask.getName() + ',' );
            strB.append(subtask.getStatus());
            strB.append(',' + subtask.getDescription() + ',');
            strB.append(subtask.getEpicId());
        } else if (task instanceof Epic) {
            Epic epic = (Epic) task;
            strB.append(epic.getId());
            strB.append(',');
            strB.append(TypeTask.EPIC);
            strB.append(',' + epic.getName() + ',' );
            strB.append(epic.getStatus());
            strB.append(',' + epic.getDescription());
        } else if (task instanceof Task) {
            strB.append(task.getId());
            strB.append(',');
            strB.append(TypeTask.TASK);
            strB.append(',' + task.getName() + ',' );
            strB.append(task.getStatus());
            strB.append(',' + task.getDescription());
        } else {
            strB.append("");
        }
        return strB.toString();
    }

    Task fromString(String value) {
        String[] str = value.split(",");
        Task result;
        switch (str[1]) {
            case "TASK":
                result = new Task(str[2],str[4],Integer.parseInt (str[0]),fromStringStatus(str[3]));
                break;
            case "EPIC":
                result = new Epic(str[2],str[4],Integer.parseInt (str[0]),fromStringStatus(str[3]));
                break;
            case "SUBTASK":
                result = new Subtask(str[2],str[4],Integer.parseInt (str[0]),
                        fromStringStatus(str[3]),Integer.parseInt (str[5]));
                break;
            default:
                result = null;
                break;
        }
        return result;
    }

    private Status fromStringStatus (String value) {
        if (value.equals("NEW")) {
            return Status.NEW;
        } else if (value.equals("IN_PROGRESS")) {
            return Status.IN_PROGRESS;
        } else {
            return Status.DONE;
        }
    }

    static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file.toString());
        List<Task> list = new ArrayList<>();

        try (FileReader reader = new FileReader(file,StandardCharsets.UTF_8);
             BufferedReader br = new BufferedReader(reader)) {

            if (br.ready()) {
                String[] str = br.readLine().split(" = ");
                if (str[0].equals("idCounter")) {
                    fileBackedTaskManager.setIdCounter(Integer.parseInt(str[1]));
                }
            }

            while (br.ready()) {
                String line = br.readLine();
                if (!line.equals("id,type,name,status,description,epicId")) {
                    list.add(fileBackedTaskManager.fromString(line));
                }
            }
        } catch (IOException e) {
            System.out.println("Произошла ошибка во время чтения файла.");
        }


        for (Task elem : list) {
            if (elem instanceof Subtask) {
                //Subtask tempSubtask = (Subtask) elem;
                fileBackedTaskManager.addNewSubtask((Subtask) elem);
                //fileBackedTaskManager.setEpicSubtask(tempSubtask.getEpicId(),tempSubtask.getId());
            } else if (elem instanceof Epic) {
                if (fileBackedTaskManager.findId(elem.getId()) == null) {
                    fileBackedTaskManager.addNewEpic((Epic) elem);
                }
                //fileBackedTaskManager.updateEpic((Epic) elem);
            } else if (elem instanceof Task) {
                fileBackedTaskManager.addNewTask(elem);
            }
        }
        fileBackedTaskManager.getHistory();
        return fileBackedTaskManager;
    }

    public static void main(String[] args) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager("taskManager.CSV");
        fillManagers(fileBackedTaskManager);
        fileBackedTaskManager.getHistory();
        File file = new File("awa/taskManager.CSV");
        FileBackedTaskManager fileBackedTaskManager2 = loadFromFile(file);

        List<Task> fileBTM2 = fileBackedTaskManager2.getHistory();
        List<Task> fileBTM1 = fileBackedTaskManager.getHistory();
        fileBTM1.get(0).equals(fileBTM2.get(0));
    }

    private static void fillManagers(TaskManager manager) {
        Task task = new Task("N-T0", "D-T0", Status.NEW);
        manager.addNewTask(task);

        task = new Task("N-T1", "D-T1", Status.NEW);
        manager.addNewTask(task);

        Epic epic = new Epic("N-E2", "D-E2");
        int epicId = manager.addNewEpic(epic);
        Subtask subtask = new Subtask("N-S3", "D-S3", Status.NEW, epicId);
        manager.addNewSubtask(subtask);

        subtask = new Subtask("N-S4", "D-S4", Status.DONE, epicId);
        manager.addNewSubtask(subtask);

        manager.updateEpic(epic);

        epic = new Epic("N-E5", "D-E5");
        epicId = manager.addNewEpic(epic);
        subtask = new Subtask("N-S6", "D-S6", Status.NEW, epicId);
        manager.addNewSubtask(subtask);
        manager.updateEpic(epic);

        epic = new Epic("N-E7", "D-E7");
        manager.addNewEpic(epic);
        manager.updateEpic(epic);
    }
}
