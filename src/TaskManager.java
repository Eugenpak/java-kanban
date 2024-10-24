import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import service.Status;

public class TaskManager {
    private static int idCounter;
    private HashMap<Integer,Task> mapTask;
    private HashMap<Integer,Epic> mapEpic;
    private HashMap<Integer,Subtask> mapSubtask;

    public TaskManager() {
        mapTask = new HashMap<>();
        mapEpic = new HashMap<>();
        mapSubtask = new HashMap<>();
    }

    static int getIdCounter() {
        return idCounter++;
    }

    @Override
    public String toString() {
        return "TaskManager{" +
                "mapTask=" + //mapTask +
                '}';
    }

    public List<Task> getListTask() {
        List<Task> list = new ArrayList<>(mapTask.values());
        return list;
    }

    public List<Epic> getListEpic() {
        List<Epic> list = new ArrayList<>(mapEpic.values());
        return list;
    }

    public List<Subtask> getListSubtask() {
        List<Subtask> list = new ArrayList<>(mapSubtask.values());
        return list;
    }

    public List<Subtask> getEpicSubtasks(int epicId){
        List<Subtask> list = new ArrayList<>(mapEpic.get(epicId).getArraySubtask());
        return list;
    }

    public Task getTaskById(Integer id) {
        if (mapTask.containsKey(id)) {
            return mapTask.get(id);
        }
        return null;
    }
    public Subtask getSubtaskById(Integer id) {
        if (mapSubtask.containsKey(id)) {
            return mapSubtask.get(id);
        }
        return null;
    }

    public Epic getEpicById(Integer id) {
        if (mapEpic.containsKey(id)) {
            return mapEpic.get(id);
        }
        return null;
    }

    public boolean updateTask(Task task) throws ClassNotFoundException {
        if (task!=null ) {
            mapTask.put(task.getId(), task);
            return true;
        }
        return false;
    }

    public boolean updateEpic(Epic epic) throws ClassNotFoundException {
        if (epic!=null ) {
            mapEpic.put(epic.getId(),epic);
            epic.updateStatus();
            return true;
        }
        return false;
    }

    public boolean updateSubtask(Subtask subtask) throws ClassNotFoundException {
        if (subtask!=null ) {
            mapSubtask.put(subtask.getId(),subtask);
            Epic epic= mapEpic.get(subtask.getEpicId());
            epic.updateStatus();
            return true;
        }
        return false;
    }

    public void printEpicSubtask(Integer idEpic) throws ClassNotFoundException {
        Epic epic = getEpicById(idEpic);

        System.out.println("Epic{id=" + epic.getId() + ", name='" + epic.getName() +
                "', description.length =" + epic.getDescription().length() +
                ", status='" + epic.getStatus() + "' ArraySubtask[" + epic.getArraySubtask().size() + "]}");
        for (Subtask elem : getEpicSubtasks(idEpic)){
            System.out.println(" --> " + elem);
        }
    }
    public int addNewTask(Task task){
        mapTask.put(task.getId(),task);
        return task.getId();
    }

    public int addNewEpic(Epic epic) throws ClassNotFoundException {
        mapEpic.put(epic.getId(),epic);
        for (Subtask elem : epic.getArraySubtask()) {
            mapSubtask.put(elem.getId(),elem);
        }
        return epic.getId();
    }

    public int addNewSubtask(Subtask subtask) throws ClassNotFoundException {
        mapSubtask.put(subtask.getId(),subtask);
        Epic epic= mapEpic.get(subtask.getEpicId());
        if (epic!=null) {
            epic.getArraySubtask().add(subtask);
            epic.updateStatus();
        }
        return subtask.getId();
    }

    public void printAllTasks() throws ClassNotFoundException {
        System.out.println("Печать полного списка задач");
        for (Task elem : getListTask()){
            System.out.println(elem);
        }
        for (Epic elem : getListEpic()){
            printEpicSubtask(elem.getId());
        }
        for (Subtask elem : getListSubtask()){
            System.out.println(elem);
        }
        System.out.println();
    }

    public void clearTaskManager() {
        mapTask.clear();
        mapSubtask.clear();
        mapEpic.clear();
    }

    public void deleteTask(int id) {
        if (getTaskById(id)!=null) {
            mapTask.remove(id);
        }
    }

    public void deleteEpic(int id) {
        Epic epic = getEpicById(id);
        if (epic!=null) {
            for (Subtask elem : epic.getArraySubtask()){
                mapSubtask.remove(elem.getId());
            }
            mapEpic.remove(id);
        }
    }

    public void deleteSubtask(int id) throws ClassNotFoundException {
        Subtask subtask=getSubtaskById(id);
        if (subtask!=null) {
            Epic epic=getEpicById(subtask.getEpicId());
            epic.getArraySubtask().remove(subtask);
            mapSubtask.remove(id);
            epic.updateStatus();
        }
    }

    public void deleteTasks() {
        mapTask.clear();
    }

    public void deleteEpics() {
        mapEpic.clear();
    }

    public void deleteSubtasks() {
        mapSubtask.clear();
    }

}
