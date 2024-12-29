package controllers;

import model.Epic;
import model.Subtask;
import model.Task;
import service.ComparatorTaskStartTime;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private int idCounter;
    private final HashMap<Integer,Task> mapTask;
    private final HashMap<Integer, Epic> mapEpic;
    private final HashMap<Integer, Subtask> mapSubtask;

    private final HistoryManager historyManager;
    private TreeSet<Task> treeTask = new TreeSet<>(new ComparatorTaskStartTime());

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
    }

    @Override
    public Subtask getSubtaskById(Integer id) {
        Optional<Subtask> s = Optional.ofNullable(mapSubtask.get(id));
        if (s.isPresent()) {
            historyManager.add(s.get());
            return s.get();
        } else return null;
    }

    @Override
    public Epic getEpicById(Integer id) {
        Optional<Epic> e = Optional.ofNullable(mapEpic.get(id));
        if (e.isPresent()) {
            historyManager.add(e.get());
            return e.get();
        } else return null;
    }

    @Override
    public boolean updateTask(Task task) {
        if (task != null) {
            Task copyTask = copyTask(task);
            mapTask.put(copyTask.getId(), copyTask);
            treeTask.remove(task);
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
        if (task.getStartTime() != null) {
            if (treeTask.contains(task)) {
                treeTask.remove(task);
            }
            Optional<Task> taskOptional = treeTask.stream()
                .filter(t -> checkIntersects(task,t))
                .findFirst();
            taskOptional.ifPresentOrElse(x ->
                        System.out.println("      " + task.getClass().getName().substring(6) + " '" +
                            task.getName() + "'(id=" + task.getId() + ")[" + task.getStartTime() + " -> " +
                            task.getStartTime().plus(task.getDuration()) + "]" + " пересекает " +
                            x.getClass().getName().substring(6) + " '" + x.getName() +
                            "'(id=" + x.getId() + ")[" + x.getStartTime() + " -> " +
                            x.getStartTime().plus(x.getDuration()) + "]"),
                        () -> treeTask.add(task));
        }
    }

    boolean checkIntersects(Task t1,Task t2) {
        Duration d1 = Optional.ofNullable(t1.getDuration()).orElse(Duration.ZERO);
        Duration d2 = Optional.ofNullable(t2.getDuration()).orElse(Duration.ZERO);
        LocalDateTime s1 = t1.getStartTime();
        LocalDateTime s2 = t2.getStartTime();
        LocalDateTime e1 = s1.plus(d1);
        LocalDateTime e2 = s2.plus(d2);

        if (d1.isZero() & d2.isZero() & s2.isEqual(s1)) return false; // n-4-4 (13)
        if (d1.minus(d2).isZero() & d2.isZero() & !(s2.isEqual(s1))) return false; // n-4-5 (14)
        if (d1.minus(d2).isNegative() & d1.isZero() & s2.isEqual(s1)) return false; // n-1-3 (4)-(08:00_0)
        if (d1.isZero() & s1.isEqual(e1) & e2.isEqual(e1)) return false; // n-2-3 (7)_(08:20_0)

        if (s1.isEqual(e1) & s1.isAfter(s2) & e1.isBefore(e2)) return true; // n-3-2 (9)
        if (s1.isAfter(s2) & e1.isBefore(e2) & d1.isPositive()) return true; // n-3-1 (8)
        if (s1.isAfter(s2) & s1.isBefore(e2) & e1.isAfter(e2)) return true; // n-4-3 (12)
        if (d1.isPositive() & s2.isEqual(s1) & e2.isBefore(e1)) return true; // n-1-2-(3)
        if (d1.isPositive() & s2.isEqual(s1) & e2.isEqual(e1)) return true; // n-0-0-(1)
        if (d1.isZero() & s1.isEqual(e1) & s2.isEqual(s1)) return false; // n-1-3-(3)

        if (s1.isBefore(e2) & s1.isAfter(s2) & e2.isEqual(e1) & d1.minus(d2).isNegative()) return true; // n-2-2 (6)
        if (d1.minus(d2).isNegative() & e1.isBefore(e2) & s1.isEqual(s2)) return true; // n-1-1-(2)
        if (d1.isZero() & (s2.isBefore(s1) & e2.isAfter(s1))) return true; // n-3-2 (8)
        if ((s1.isBefore(s2) & e1.isAfter(s2)) || (s1.isBefore(e2) & e1.isAfter(e2))) return true;
        return false;
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
        epic.getArraySubtask().forEach(elem -> {
             addNewSubtask(elem); elem.setEpicId(id); });
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
            epic.getArraySubtask().forEach(elem -> {
                mapSubtask.remove(elem.getId());historyManager.remove(elem.getId()); });
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
        mapTask.values().forEach(task -> historyManager.remove(task.getId()));
        mapTask.values().forEach(task -> treeTask.remove(task));
        mapTask.clear();
    }

    @Override
    public void deleteEpics() {
        mapEpic.values().forEach(epic -> historyManager.remove(epic.getId()));
        mapEpic.clear();
        mapSubtask.values().forEach(subtask -> historyManager.remove(subtask.getId()));
        mapSubtask.values().forEach(task -> treeTask.remove(task));
        mapSubtask.clear();
    }

    @Override
    public void deleteSubtasks() {
        getListEpic().forEach(elem -> {
            elem.getArraySubtask().clear(); updateEpic(elem); });
        mapSubtask.values().forEach(subtask -> historyManager.remove(subtask.getId()));
        mapSubtask.values().forEach(subtask -> treeTask.remove(subtask));
        mapSubtask.clear();
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(treeTask);
    }
}
