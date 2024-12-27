package controllers;

import model.Epic;
import model.Subtask;
import model.Task;
import service.ComparatorTaskStartTime;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;


public class InMemoryTaskManager implements TaskManager {
    private int idCounter;
    private final HashMap<Integer,Task> mapTask;
    private final HashMap<Integer, Epic> mapEpic;
    private final HashMap<Integer, Subtask> mapSubtask;

    private final HistoryManager historyManager;
    private TreeSet<Task> treeTask = new TreeSet<Task>(new ComparatorTaskStartTime());

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
        Optional<Task> t = Optional.ofNullable(mapTask.get(id));
        if (t.isPresent()) {
            historyManager.add(t.get());
            return t.get();
        } else return null;
        /*
        if (mapTask.containsKey(id)) {
            Task task = mapTask.get(id);
            historyManager.add(task);
            return task;
        }
        return null;
        */
    }

    @Override
    public Subtask getSubtaskById(Integer id) {
        Optional<Subtask> s = Optional.ofNullable(mapSubtask.get(id));
        if (s.isPresent()) {
            historyManager.add(s.get());
            return s.get();
        } else return null;
        /*
        if (mapSubtask.containsKey(id)) {
            Subtask subtask = mapSubtask.get(id);
            historyManager.add(subtask);
            return subtask;
        }
        return null;
        */
    }

    @Override
    public Epic getEpicById(Integer id) {
        Optional<Epic> e = Optional.ofNullable(mapEpic.get(id));
        if (e.isPresent()) {
            historyManager.add(e.get());
            return e.get();
        } else return null;
        /*
        if (mapEpic.containsKey(id)) {
            Epic epic = mapEpic.get(id);
            historyManager.add(epic);
            return epic;
        }
        return null;
        */
    }

    @Override
    public boolean updateTask(Task task) {
        if (task != null) {
            Task copyTask = copyTask(task);
            mapTask.put(copyTask.getId(), copyTask);
            if (treeTask.contains(task)) {
                treeTask.remove(task);
            }
            validTaskInTreeSet(copyTask);
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
            validTaskInTreeSet(newSubtask);
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
        validTaskInTreeSet(task);
        return id;
    }

    private void validTaskInTreeSet(Task task) {
        if ((task.getStartTime() != null) ) {
            if (treeTask.equals(task)) {
                treeTask.remove(task);
            }
        Optional<Task> taskOptional = treeTask.stream()
                .filter(t->checkIntersects(task,t))
                .findFirst();
        taskOptional.ifPresentOrElse(x->
                        System.out.println(task.getClass().getName().substring(6) + " '" + task.getName() +
                        "'(id=" + task.getId() + ")" + " пересекает " + x.getClass().getName().substring(6) +
                        " '" + x.getName() + "'(id="+x.getId() + ") startTime=" + x.getStartTime() + ", endTime=" +
                        x.getStartTime().plus(x.getDuration())),
                () -> treeTask.add(task));
        }
    }

    boolean checkIntersects(Task t1,Task t2) {
        Duration d1 = t1.getDuration();
        Duration d2 = t2.getDuration();
        if (d1 == null) {
            d1 = Duration.ZERO;
        }
        if (d2 == null) {
            d2 = Duration.ZERO;
        }
        if (d1 == Duration.ZERO & d2 == Duration.ZERO) {
            return false;
        }
        if (d1 == Duration.ZERO &
                (t2.getStartTime().isBefore(t1.getStartTime()) & t2.getEndTime().isAfter(t1.getStartTime()))) {
            return true;
        }
        if (d2 == Duration.ZERO &
                (t1.getStartTime().isBefore(t2.getStartTime()) & t1.getEndTime().isAfter(t2.getStartTime()))) {
            return true;
        }
        return (t1.getStartTime().isBefore(t2.getStartTime()) & t1.getEndTime().isAfter(t2.getStartTime())) ||
                (t1.getStartTime().isBefore(t2.getEndTime()) & t1.getEndTime().isAfter(t2.getEndTime()));
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
        /*
        for (Subtask elem : epic.getArraySubtask()) {
            addNewSubtask(elem);
            elem.setEpicId(id);
        }
        */
        epic.getArraySubtask().forEach(elem->{addNewSubtask(elem); elem.setEpicId(id);});
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
        validTaskInTreeSet(subtask);
        return id;
    }

    @Override
    public void deleteTask(int id) {
        Task task = getTaskById(id);
        if (task != null) {
            mapTask.remove(id);
            historyManager.remove(id);
            treeTask.remove(task);
        }
    }

    @Override
    public void deleteEpic(int id) {
        Epic epic = getEpicById(id);
        if (epic != null) {
            /*
            for (Subtask elem : epic.getArraySubtask()) {
                mapSubtask.remove(elem.getId());
                historyManager.remove(elem.getId());
            }
            */
            epic.getArraySubtask().forEach(elem->{mapSubtask.remove(elem.getId());historyManager.remove(elem.getId());});
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
            treeTask.remove(subtask);
        }
    }

    @Override
    public void deleteTasks() {
        /*
        for (Task task : mapTask.values()) {
            historyManager.remove(task.getId());
        }
        */
        mapTask.values().forEach(task->historyManager.remove(task.getId()));
        mapTask.values().forEach(task->treeTask.remove(task));
        mapTask.clear();
    }

    @Override
    public void deleteEpics() {
        /*
        for (Epic epic : mapEpic.values()) {
            historyManager.remove(epic.getId());
        }
        */
        mapEpic.values().forEach(epic->historyManager.remove(epic.getId()));
        mapEpic.clear();
        /*
        for (Subtask subtask : mapSubtask.values()) {
            historyManager.remove(subtask.getId());
        }
        */
        mapSubtask.values().forEach(subtask->historyManager.remove(subtask.getId()));
        mapSubtask.values().forEach(task->treeTask.remove(task));
        mapSubtask.clear();
    }

    @Override
    public void deleteSubtasks() {
        /*
        for (Epic elem : getListEpic()) {
            elem.getArraySubtask().clear();
            updateEpic(elem);
        }
        */
        getListEpic().forEach(elem->{elem.getArraySubtask().clear(); updateEpic(elem);});
        /*
        for (Subtask subtask : mapSubtask.values()) {
            historyManager.remove(subtask.getId());
        }
        */
        mapSubtask.values().forEach(subtask->historyManager.remove(subtask.getId()));
        mapSubtask.values().forEach(subtask->treeTask.remove(subtask));
        mapSubtask.clear();
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    public List<Task> getPrioritizedTasks() {
        return treeTask.stream().collect(Collectors.toList());
    }
}
