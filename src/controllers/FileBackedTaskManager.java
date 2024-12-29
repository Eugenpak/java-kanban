package controllers;

import exceptions.ManagerSaveException;
import model.Epic;
import model.Subtask;
import model.Task;
import service.Status;
import service.TypeTask;

import java.io.*;
import java.nio.charset.StandardCharsets;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final String filename;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(" yyyy-MM-dd HH:mm");

    public FileBackedTaskManager(String filename) {
        this.filename = filename;
        try {
            File file = new File(filename);
            if (!(new File(filename).exists())) {
                createFile(filename);
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка во время создания файла (Конструктор). Проверь путь файла " + filename);
        }
    }

    private void createFile(String filename) throws IOException {
        FileWriter writer = new FileWriter(filename, StandardCharsets.UTF_8);
        BufferedWriter bw = new BufferedWriter(writer);
        bw.write("idCounter = " + getIdCounterFile() + "\n");
        bw.write("id,type,name,status,description,epicId\n");
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

    private void save() {
        try (FileWriter writer = new FileWriter(filename, StandardCharsets.UTF_8);
             BufferedWriter bw = new BufferedWriter(writer)) {
            bw.write("idCounter = " + getIdCounterFile() + "\n");
            bw.write("id,type,name,status,description,startTime,endTime,duration,epicId\n");
            List<Task> taskList = getHistory();

            for (Task elem : getListEpic()) {
                bw.write(toString(elem) + '\n');
            }
            bw.write("id,type,name,status,description,startTime,endTime,duration,epicId\n");
            for (Task elem : taskList) {
                bw.write(toString(elem) + '\n');
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка во время записи файла.");
        }
    }

    private String toString(Task task) throws NullPointerException {
        StringBuilder strB = new StringBuilder();
        if (task instanceof Subtask) {
            Subtask subtask = (Subtask) task;
            strB.append(subtask.getId()).append(',');
            strB.append(TypeTask.SUBTASK).append(',');
            strB.append(subtask.getName()).append(',');
            strB.append(subtask.getStatus()).append(',');
            strB.append(subtask.getDescription()).append(',');
            strB.append(task.validLocalDateTime(subtask.getStartTime())).append(',');
            try {
                strB.append(task.validLocalDateTime(subtask.getEndTime())).append(',');
            } catch (NullPointerException e) {
                strB.append("null,");
            }
            strB.append(task.validDuration(subtask.getDuration())).append(',');
            strB.append(subtask.getEpicId());
        } else if (task instanceof Epic) {
            Epic epic = (Epic) task;
            strB.append(epic.getId()).append(',');
            strB.append(TypeTask.EPIC).append(',');
            strB.append(epic.getName()).append(',');
            strB.append(epic.getStatus()).append(',');
            strB.append(epic.getDescription()).append(',');
            strB.append(task.validLocalDateTime(epic.getStartTime())).append(',');
            strB.append(task.validLocalDateTime(epic.getEndTime())).append(',');
            strB.append(task.validDuration(epic.getDuration()));
        } else if (task != null) {
            strB.append(task.getId()).append(',');
            strB.append(TypeTask.TASK).append(',');
            strB.append(task.getName()).append(',');
            strB.append(task.getStatus()).append(',');
            strB.append(task.getDescription()).append(',');
            strB.append(task.validLocalDateTime(task.getStartTime())).append(',');
            try {
                strB.append(task.validLocalDateTime(task.getEndTime())).append(',');
            } catch (NullPointerException e) {
                strB.append("null,");
            }
            strB.append(task.validDuration(task.getDuration()));
        }
        return strB.toString();
    }

    Task fromString(String value) throws IOException {
        String[] str = value.split(",");
        return switch (str[1]) {
            case "TASK" -> new Task(str[2], str[4], Integer.parseInt(str[0]), fromStringStatus(str[3]),
                    fromStringLocalDateTime(str[5]),fromStringDuration(str[7]));
            case "EPIC" -> new Epic(str[2], str[4], Integer.parseInt(str[0]), fromStringStatus(str[3]));
            case "SUBTASK" -> new Subtask(str[2], str[4], Integer.parseInt(str[0]),
                    fromStringStatus(str[3]), Integer.parseInt(str[8]),fromStringLocalDateTime(str[5]),
                    fromStringDuration(str[7]));
            default -> throw new IOException("Не правильный тип (TypeTask).");
        };
    }

    private Status fromStringStatus(String value) throws IOException {
        return switch (value) {
            case "NEW" -> Status.NEW;
            case "IN_PROGRESS" -> Status.IN_PROGRESS;
            case "DONE" -> Status.DONE;
            default -> throw new IOException("Не правильный тип (Status).");
        };
    }

    private LocalDateTime fromStringLocalDateTime(String value) {
        return switch (value) {
            case "null" -> null;
            default -> LocalDateTime.parse(value,formatter);
        };
    }

    private Duration fromStringDuration(String value) {
        return switch (value) {
            case "null" -> Duration.ZERO;
            default -> Duration.ofMinutes(Long.parseLong(value));
        };
    }

    public static FileBackedTaskManager loadFromFile(File file) {
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
                if (!line.equals("id,type,name,status,description,startTime,endTime,duration,epicId")) {
                    list.add(fileBackedTaskManager.fromString(line));
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка во время чтения файла." + e.getMessage());
        }

        for (Task elem : list) {
            if (elem instanceof Subtask) {
                fileBackedTaskManager.addNewSubtask((Subtask) elem);
            } else if (elem instanceof Epic) {
                if (fileBackedTaskManager.findId(elem.getId()) == null) {
                    fileBackedTaskManager.addNewEpic((Epic) elem);
                }
            } else if (elem != null) {
                fileBackedTaskManager.addNewTask(elem);
            }
        }
        return fileBackedTaskManager;
    }

    public static void main(String[] args) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager("taskManager.CSV");
        fillManagers(fileBackedTaskManager);
        fileBackedTaskManager.getHistory();
        File file = new File("taskManager.CSV");
        FileBackedTaskManager fileBackedTaskManager2 = loadFromFile(file);

        System.out.println("Список задач до сохранения в файл.");
        List<Task> fileBTM1 = fileBackedTaskManager.getHistory();

        fileBTM1.forEach(elem -> System.out.println(elem.toString()));
        System.out.println();
        System.out.println("Список задач после восстановления из файла.");
        List<Task> fileBTM2 = fileBackedTaskManager2.getHistory();

        fileBTM2.forEach(elem -> System.out.println(elem.toString()));
    }

    private static void fillManagers(TaskManager manager) {
        LocalDateTime start = LocalDateTime.of(2024,12,1,10,0,0);
        Duration durationTest = Duration.ofMinutes(10);
        Task task = new Task("N-T0", "D-T0", Status.NEW,start,durationTest);
        manager.addNewTask(task);

        task = new Task("N-T1", "D-T1", Status.NEW, start.plus(durationTest),durationTest);
        manager.addNewTask(task);

        Epic epic = new Epic("N-E2", "D-E2");
        int epicId = manager.addNewEpic(epic);
        Subtask subtask = new Subtask("N-S3", "D-S3", Status.NEW,
                start.plus(durationTest).plus(durationTest),durationTest);
        subtask.setEpicId(epicId);
        manager.addNewSubtask(subtask);

        subtask = new Subtask("N-S4", "D-S4", Status.DONE,
                start.plus(durationTest).plus(durationTest).plus(durationTest),durationTest);
        subtask.setEpicId(epicId);
        manager.addNewSubtask(subtask);

        manager.updateEpic(epic);

        epic = new Epic("N-E5", "D-E5");
        epicId = manager.addNewEpic(epic);
        subtask = new Subtask("N-S6", "D-S6", Status.NEW,
                start.plus(durationTest).plus(durationTest).plus(durationTest).plus(durationTest),durationTest);
        subtask.setEpicId(epicId);
        manager.addNewSubtask(subtask);
        manager.updateEpic(epic);

        epic = new Epic("N-E7", "D-E7");
        manager.addNewEpic(epic);
        manager.updateEpic(epic);
    }
}
