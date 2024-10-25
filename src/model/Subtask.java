package model;

import service.Status;
public class Subtask extends Task {
    private int epicId;

    public Subtask(String name, String description, Status status, int epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }
    public Subtask() {
    }

    public Subtask(Subtask subtask) {
        super(subtask.getName(), subtask.getDescription(), subtask.getStatus());
        this.epicId = subtask.getEpicId();
    }

    @Override
    public String toString() {
            return "Subtask{" +
                    "id=" + super.getId() +
                    ", name='" + this.getName() + '\'' +
                    ", description='" + this.getDescription() + '\'' +
                    ", status=" + super.getStatus() +
                    ", epicId=" + epicId +
                    '}';
    }

    @Override
    public void setStatus(Status status) {
        super.setStatus(status);
    }

    public void update(Epic epic) {
        if (!(epic==null)) {
            epic.updateStatus();
        }
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public Subtask copySubtask() {
        Subtask subtask = new Subtask();
        subtask.setName(this.getName());
        subtask.setDescription(this.getDescription());
        subtask.setEpicId(this.getEpicId());
        subtask.setStatus(this.getStatus());
        return subtask;
    }
}
