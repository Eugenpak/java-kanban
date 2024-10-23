import java.util.ArrayList;
import service.Status;

public class Epic extends Task{
    private ArrayList<Subtask> arraySubtask;

    public Epic() {
    }
    public Epic(String name, String description, Status status) {
        super(name, description, status);
        arraySubtask = new ArrayList<>();
    }

    public Epic(Epic epic) throws ClassNotFoundException {
        super(epic.getName(), epic.getDescription(), epic.getStatus());
        ArrayList<Subtask> newArraySubtask = new ArrayList<>();
        arraySubtask = newArraySubtask;
    }

    public Epic(Epic epic, boolean newEpicId) throws ClassNotFoundException {
        super(epic.getName(), epic.getDescription(), epic.getStatus());
        if (newEpicId) {
            ArrayList<Subtask> newArraySubtask = new ArrayList<Subtask>();
            for (Subtask elem : epic.getArraySubtask()){
                Subtask st;
                newArraySubtask.add(st = new Subtask(elem));
                //Subtask st = newArraySubtask.getLast();
                st.setEpicId(this.getId());
            }
            arraySubtask = newArraySubtask;
        } else {
            this.setId(epic.getId());
            this.setArraySubtask(epic.getArraySubtask());
        }
    } //*/



    @Override
    public String toString() {
        try {
            return "Epic{" +
                    "Id=" + super.getId() +
                    ", name='" + super.getName() + '\'' +
                    ", description='" + super.getDescription() + '\'' +
                    ", status=" + super.getStatus() +
                    ", arraySubtask: " + arraySubtask +
                    "}";
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Status getStatus() throws ClassNotFoundException {
        return super.getStatus();
    }

    @Override
    public void setStatus(Status status) throws ClassNotFoundException {
        super.setStatus(status);
    }

    public ArrayList<Subtask> getArraySubtask() {
        return arraySubtask;
    }

    public void setArraySubtask(ArrayList<Subtask> arraySubtask) {
        this.arraySubtask = arraySubtask;
    }

    public Status updateStatus() throws ClassNotFoundException {
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
