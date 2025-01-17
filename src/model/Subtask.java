package model;

import service.Status;
import java.time.Duration;
import java.time.LocalDateTime;

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

    public Subtask(String name, String description, Status status,LocalDateTime startTime, Duration duration) {
        super(name, description, status,startTime, duration);
        this.epicId = -5;
    }

    public Subtask() {
    }

    public Subtask(Subtask subtask) {
        super(subtask.getName(), subtask.getDescription(), subtask.getId(),subtask.getStatus(),
                subtask.getStartTime(),subtask.getDuration());
        this.epicId = subtask.getEpicId();
    }

    public Subtask(String name, String description, int id,Status status, int epicId) {
        super(name, description, id, status);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, int id, Status status, int epicId, LocalDateTime startTime, Duration duration) {
        super(name, description, id, status,startTime,duration);
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
                    ", startTime=" + validLocalDateTime(getStartTime()) +
                    ", duration=" + validDuration(getDuration()) +
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
        subtask.setStartTime(this.getStartTime());
        subtask.setDuration(this.getDuration());
        return subtask;
    }
}
