package model;

import service.Status;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String name, String description, Status status, int epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, Status status) {
        super(name, description, status);
        this.epicId = -5;
    }

    public Subtask() {
    }

    public Subtask(Subtask subtask) {
        super(subtask.getName(), subtask.getDescription(), subtask.getId(),subtask.getStatus());
        this.epicId = subtask.getEpicId();
    }

    public Subtask(String name, String description, int id,Status status, int epicId) {
        super(name, description, id, status);
        this.epicId = epicId;
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
        if (!(epic == null)) {
            epic.updateStatus();
        }
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        if (super.getId() != epicId) {
            this.epicId = epicId;
        }
    }

    public Subtask copySubtask() {
        final Subtask subtask = new Subtask();
        subtask.setName(this.getName());
        subtask.setDescription(this.getDescription());
        subtask.setEpicId(this.getEpicId());
        subtask.setStatus(this.getStatus());
        subtask.setId(this.getId());
        return subtask;
    }
}
