package model;

import java.util.ArrayList;
import service.Status;

public class Epic extends Task {
    private final ArrayList<Subtask> arraySubtask = new ArrayList<>();

    public Epic() {
    }

    public Epic(String name, String description) {
        super(name, description, Status.NEW);
    }

    public Epic(Epic epic) {
        super(epic.getName(), epic.getDescription(), epic.getId(),epic.getStatus());
        if (epic.getArraySubtask() == null) {
            //
        } else {
            for (Subtask elem : epic.getArraySubtask()) {
                this.arraySubtask.add(elem);
            }
        }
    }

    public Epic(Epic epic, boolean newEpicId) {
        super(epic.getName(), epic.getDescription(), epic.getStatus());
        if (newEpicId) {
            for (Subtask elem : epic.getArraySubtask()) {
                Subtask st = new Subtask(elem.getName(),elem.getDescription(),elem.getStatus());
                st.setEpicId(epic.getId()); // ???
                arraySubtask.add(st);
            }
        } else {
            this.setId(epic.getId());
            this.setArraySubtask(epic.getArraySubtask());
        }
    }

    public Epic(String name, String description,int id, Status status) {
        super(name, description, id,Status.NEW);
    }

    public Epic(String name, String description, Status status) {
        super(name, description,status);
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
        this.arraySubtask.clear();
        for (Subtask elem : arraySubtask) {
            this.arraySubtask.add(elem.copySubtask());
        }
    }

    public Status updateStatus() {
        if (arraySubtask.size() == 0) {
            super.setStatus(Status.NEW);
            return getStatus();
        }
        Status temp = Status.NEW;
        for (Subtask subtask : arraySubtask) {
            if (!(subtask.getStatus() == Status.NEW)) {
                temp = subtask.getStatus();
                break;
            }
        }
        if (temp == Status.NEW) {
            setStatus(temp);
            return getStatus();
        }
        temp = Status.DONE;
        for (Subtask subtask : arraySubtask) {
            if (!(subtask.getStatus() == Status.DONE)) {
                temp = subtask.getStatus();
                break;
            }
        }
        if (temp == Status.DONE) {
            setStatus(temp);
            return getStatus();
        }
        setStatus(Status.IN_PROGRESS);
        return getStatus();
    }
}
