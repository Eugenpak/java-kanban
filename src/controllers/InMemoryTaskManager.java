package controllers;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class InMemoryTaskManager implements TaskManager {
    private int idCounter;
    private final HashMap<Integer,Task> mapTask;
    private final HashMap<Integer, Epic> mapEpic;
    private final HashMap<Integer, Subtask> mapSubtask;

    private final HistoryManager historyManager;

    public InMemoryTaskManager() {
        mapTask = new HashMap<>();
        mapEpic = new HashMap<>();
        mapSubtask = new HashMap<>();
        historyManager = Managers.getDefaultHistory();
    }

    int getIdCounter() {
        return idCounter++;
    }

    protected void setIdCounter(int counter) {
        idCounter = counter;
    }

    protected int getIdCounterFile() {
        return idCounter;
    }

    @Override
    public String toString() {
        return "controllers.TaskManager{" +
                "mapTask=" + mapTask +
                "mapEpic=" + mapEpic +
                "mapSubtask=" + mapSubtask +
                '}';
    }

    @Override
    public List<Task> getListTask() {
        return new ArrayList<>(mapTask.values());
    }

    @Override
    public List<Epic> getListEpic() {
        return new ArrayList<>(mapEpic.values());
    }

    @Override
    public List<Subtask> getListSubtask() {
        return new ArrayList<>(mapSubtask.values());
    }

    @Override
    public List<Subtask> getEpicSubtasks(int epicId) {
        return new ArrayList<>(mapEpic.get(epicId).getArraySubtask());
    }

    @Override
    public boolean setEpicSubtask(int epicId, int subtaskId) {
        final Epic epic = getEpicById(epicId);
        if (epic != null) {
            ArrayList<Subtask> arraySubtask = epic.getArraySubtask();
            if (arraySubtask != null) {
                for (Subtask elem : arraySubtask) {
                    if (elem.getId() == subtaskId) {
                        System.out.println("Подзадача с id=" + subtaskId + " уже есть!");
                        return false;
                    }
                }
            } else {
                arraySubtask = new ArrayList<>();
                epic.setArraySubtask(arraySubtask);
            }
            Subtask subtask = getSubtaskById(subtaskId);
            if (subtask != null) {
                subtask.setEpicId(epicId);
                arraySubtask.add(subtask.copySubtask());
                subtask.update(epic);
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public Task findId(int id) {
        Task findTask;
        findTask = getTaskById(id);
        if (findTask != null) {
            return findTask;
        }
        findTask = getEpicById(id);
        if (findTask != null) {
            return findTask;
        }
        findTask = getSubtaskById(id);
        if (findTask != null) {
            return findTask;
        }
        return null;
    }

    @Override
    public Task getTaskById(Integer id) {
        if (mapTask.containsKey(id)) {
            Task task = mapTask.get(id);
            historyManager.add(task);
            return task;
        }
        return null;
    }

    @Override
    public Subtask getSubtaskById(Integer id) {
        if (mapSubtask.containsKey(id)) {
            Subtask subtask = mapSubtask.get(id);
            historyManager.add(subtask);
            return subtask;
        }
        return null;
    }

    @Override
    public Epic getEpicById(Integer id) {
        if (mapEpic.containsKey(id)) {
            Epic epic = mapEpic.get(id);
            historyManager.add(epic);
            return epic;
        }
        return null;
    }

    @Override
    public boolean updateTask(Task task) {
        if (task != null) {
            Task copyTask = copyTask(task);
            mapTask.put(copyTask.getId(), copyTask);
            return true;
        }
        return false;
    }

    private Task copyTask(Task task) {
        if (task != null) {
            final Task newTask = new Task();
            newTask.setId(task.getId());
            newTask.setName(task.getName());
            newTask.setDescription(task.getDescription());
            newTask.setStatus(task.getStatus());
            newTask.setStartTime(task.getStartTime());
            newTask.setDuration(task.getDuration());
            return newTask;
        }
        return null;
    }

    @Override
    public boolean updateEpic(Epic epic) {
        if (epic != null) {
            final Epic newEpic = new Epic(epic);
            mapEpic.put(newEpic.getId(),newEpic);
            newEpic.updateStatus();
            historyManager.add(newEpic);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateSubtask(Subtask subtask) {
        if (subtask != null) {
            final Subtask newSubtask = new Subtask(subtask);
            mapSubtask.put(newSubtask.getId(),newSubtask);
            final int idEpic = newSubtask.getEpicId();
            Epic epic = mapEpic.get(idEpic);
            if (epic != null) {
                epic.updateStatus();
            }
            return true;
        }
        return false;
    }

    @Override
    public int addNewTask(Task task) {
        final int id;
        if (checkIdAdd(task)) {
            id = getIdCounter(); //idCounter++
            task.setId(id);
        } else {
            id = task.getId();
        }
        mapTask.put(id, task);
        historyManager.add(task);
        return id;
    }

    private boolean checkIdAdd(Task task) {
        final int id = task.getId();
        final Task findedTask = findId(id);
        if (id < 0 || id >= idCounter) {
            return true; //&&  &&
        } else if (findedTask != null) {
            String findedStr = findedTask.getClass().toString();
            String str = task.getClass().toString();
            if (!findedStr.equals(str)) {
                // вернем true, когда 0<=task.getId()<=idCounter и тип задачи разные (task != findedTask)
                return true;
            }
        }
        return false;
    }

    @Override
    public int addNewEpic(Epic epic) {
        final int id;
        if (checkIdAdd(epic)) {
            id = idCounter++;
        } else {
            id = epic.getId();
        }
        epic.setId(id);
        mapEpic.put(id,epic);
        for (Subtask elem : epic.getArraySubtask()) {
            addNewSubtask(elem);
            elem.setEpicId(id);
        }
        historyManager.add(epic);
        return id;
    }

    @Override
    public int addNewSubtask(Subtask subtask) {
        final int id;
        if (checkIdAdd(subtask)) {
            id = idCounter++;
        } else {
            id = subtask.getId();
        }
        subtask.setId(id);
        mapSubtask.put(id,subtask);
        historyManager.add(subtask);
        final int epicId = subtask.getEpicId();
        Epic epic = mapEpic.get(epicId);
        if (epic != null) {
            setEpicSubtask(epicId,id);
        }
        return id;
    }

    @Override
    public void deleteTask(int id) {
        if (getTaskById(id) != null) {
            mapTask.remove(id);
            historyManager.remove(id);
        }
    }

    @Override
    public void deleteEpic(int id) {
        Epic epic = getEpicById(id);
        if (epic != null) {
            for (Subtask elem : epic.getArraySubtask()) {
                mapSubtask.remove(elem.getId());
                historyManager.remove(elem.getId());
            }
            mapEpic.remove(id);
            historyManager.remove(id);
        }
    }

    @Override
    public void deleteSubtask(int id) {
        Subtask subtask = getSubtaskById(id);
        if (subtask != null) {
            Epic epic = getEpicById(subtask.getEpicId());
            if (epic != null) {
                epic.getArraySubtask().remove(subtask);
                epic.updateStatus();
            }
            mapSubtask.remove(id);
            historyManager.remove(id);
        }
    }

    @Override
    public void deleteTasks() {
        for (Task task : mapTask.values()) {
            historyManager.remove(task.getId());
        }
        mapTask.clear();
    }

    @Override
    public void deleteEpics() {
        for (Epic epic : mapEpic.values()) {
            historyManager.remove(epic.getId());
        }
        mapEpic.clear();
        for (Subtask subtask : mapSubtask.values()) {
            historyManager.remove(subtask.getId());
        }
        mapSubtask.clear();
    }

    @Override
    public void deleteSubtasks() {
        for (Epic elem : getListEpic()) {
            elem.getArraySubtask().clear();
            updateEpic(elem);
        }
        for (Subtask subtask : mapSubtask.values()) {
            historyManager.remove(subtask.getId());
        }
        mapSubtask.clear();
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}
