package controllers;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class InMemoryTaskManager implements TaskManager {
    private static int idCounter;
    private final HashMap<Integer,Task> mapTask;
    private final HashMap<Integer, Epic> mapEpic;
    private final HashMap<Integer, Subtask> mapSubtask;

    private final HistoryManager historyManager;

    public InMemoryTaskManager() {
        mapTask = new HashMap<>();
        mapEpic = new HashMap<>();
        mapSubtask = new HashMap<>();
        historyManager= Managers.getDefaultHistory();
    }

    static int getIdCounter() {
        return idCounter++;
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
    public List<Subtask> getEpicSubtasks(int epicId){
        return new ArrayList<>(mapEpic.get(epicId).getArraySubtask());
    }

    //@Override
    public boolean setEpicSubtask(int epicId, int subtaskId){
        final Epic epic=getEpicById(epicId);
        if (epic!=null) {
            ArrayList<Subtask> arraySubtask = epic.getArraySubtask();
            if (arraySubtask!=null) {
                for (Subtask elem : arraySubtask){
                    if (elem.getId()==subtaskId) {
                        System.out.println("Подзадача с id=" + subtaskId + " уже есть!");
                        return false;
                    }
                }
            } else {
                arraySubtask = new ArrayList<>();
                epic.setArraySubtask(arraySubtask);
            }
            Subtask subtask = getSubtaskById(subtaskId);
            if (subtask!=null){
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
        if (task!=null ) {
            Task copyTask = copyTask(task);
            mapTask.put(copyTask.getId(), copyTask);
            return true;
        }
        return false;
    }
    private Task copyTask(Task task){
        if (task!=null){
            final Task newTask = new Task();
            newTask.setId(task.getId());
            newTask.setName(task.getName());
            newTask.setDescription(task.getDescription());
            newTask.setStatus(task.getStatus());
            return newTask;
        }
        return null;
    }

    @Override
    public boolean updateEpic(Epic epic) {
        if (epic!=null ) {
            final Epic newEpic = new Epic(epic);
            mapEpic.put(newEpic.getId(),newEpic);
            newEpic.updateStatus();
            return true;
        }
        return false;
    }

    @Override
    public boolean updateSubtask(Subtask subtask) {
        if (subtask!=null ) {
            final Subtask newSubtask = new Subtask(subtask);
            mapSubtask.put(newSubtask.getId(),newSubtask);
            final int idEpic = newSubtask.getEpicId();
            Epic epic= mapEpic.get(idEpic);
            if (epic!=null) {
                epic.updateStatus();
            }
            return true;
        }
        return false;
    }

    @Override
    public int addNewTask(Task task){
        //mapTask.put(task.getId(),task);
        final int id;
        if (task.getId()<0){
            id = idCounter++;
            task.setId(id);
        } else {
            id = task.getId();
        }
        mapTask.put(id, task);
        //historyManager.add(task);
        return id;
    }

    @Override
    public int addNewEpic(Epic epic) {
        final int id;
        if (epic.getId()<0) {
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
        //historyManager.add(epic);
        return id;
    }

    @Override
    public int addNewSubtask(Subtask subtask) {
        final int id;
        if (subtask.getId()<0){
            id = idCounter++;
        } else {
            id = subtask.getId();
        }
        subtask.setId(id);
        mapSubtask.put(id,subtask);
        final int epicId = subtask.getEpicId();
        Epic epic= mapEpic.get(epicId);
        if (epic!=null) {
            //epic.getArraySubtask().add(subtask);
            setEpicSubtask(epicId,id);
            //epic.updateStatus();
        }
        //historyManager.add(subtask);
        return id;
    }

    @Override
    public void deleteTask(int id) {
        if (getTaskById(id)!=null) {
            mapTask.remove(id);
        }
    }

    @Override
    public void deleteEpic(int id) {
        Epic epic = getEpicById(id);
        if (epic!=null) {
            for (Subtask elem : epic.getArraySubtask()){
                mapSubtask.remove(elem.getId());
            }
            mapEpic.remove(id);
        }
    }

    @Override
    public void deleteSubtask(int id) {
        Subtask subtask=getSubtaskById(id);
        if (subtask!=null) {
            Epic epic=getEpicById(subtask.getEpicId());
            if (epic!=null){
                epic.getArraySubtask().remove(subtask);
                epic.updateStatus();
            }
            mapSubtask.remove(id);
        }
    }

    @Override
    public void deleteTasks() {
        mapTask.clear();
    }

    @Override
    public void deleteEpics() {
        mapEpic.clear();
        mapSubtask.clear();
    }

    @Override
    public void deleteSubtasks() {
        for (Epic elem : getListEpic()){
            elem.getArraySubtask().clear();
            updateEpic(elem);
        }
        mapSubtask.clear();
    }

    @Override
    public List<Task> getHistory(){
        return historyManager.getHistory(); // внимание!
    }
}
