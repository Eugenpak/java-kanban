import java.util.ArrayList;
import service.Status;

public class Epic extends Task{
    private ArrayList<Subtask> arraySubtask;

    public Epic() {
    }
    public Epic(String name, String description) {
        super(name, description, Status.NEW);
        arraySubtask = new ArrayList<>();
    }

    public Epic(Epic epic) {
        super(epic.getName(), epic.getDescription(), epic.getStatus());
        arraySubtask = new ArrayList<>();
    }

    public Epic(Epic epic, boolean newEpicId) {
        super(epic.getName(), epic.getDescription(), epic.getStatus());
        if (newEpicId) {
            arraySubtask = new ArrayList<>();
            for (Subtask elem : epic.getArraySubtask()){
                Subtask st = new Subtask(elem);
                st.setEpicId(epic.getId());
                arraySubtask.add(st);
            }
        } else {
            this.setId(epic.getId());
            this.setArraySubtask(epic.getArraySubtask());
        }
    }

    @Override
    public String toString() {
            return "Epic{" +
                    "Id=" + super.getId() +
                    ", name='" + super.getName() + '\'' +
                    ", description='" + super.getDescription() + '\'' +
                    ", status=" + super.getStatus() +
                    ", arraySubtask: " + arraySubtask +
                    "}";
    }

    @Override
    public Status getStatus() {
        return super.getStatus();
    }

    public ArrayList<Subtask> getArraySubtask() {
        return arraySubtask;
    }

    public void setArraySubtask(ArrayList<Subtask> arraySubtask) {
        this.arraySubtask = arraySubtask;
    }

    public Status updateStatus() {
        if (arraySubtask.size()==0) {
            super.setStatus(Status.NEW);
            return getStatus();
        }
        Status temp=Status.NEW;
        for (Subtask  subtask : arraySubtask){
            if (!(subtask.getStatus()==Status.NEW)){
                temp=subtask.getStatus();
                break;
            }
        }
        if (temp==Status.NEW) {
            setStatus(temp);
            return getStatus();
        }
        temp=Status.DONE;
        for (Subtask  subtask : arraySubtask){
            if (!(subtask.getStatus()==Status.DONE)){
                temp=subtask.getStatus();
                break;
            }
        }
        if (temp==Status.DONE) {
            setStatus(temp);
            return getStatus();
        }
        setStatus(Status.IN_PROGRESS);
        return getStatus();
    }
}
