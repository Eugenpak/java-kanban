package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import service.Status;

public class Task {
    private String name;
    private String description;
    private int id;
    private Status status;
    private LocalDateTime startTime;
    private Duration duration;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(" yyyy-MM-dd HH:mm");

    public DateTimeFormatter getFormatter() {
        return formatter;
    }


    public Task(String name, String description, Status status) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.id = -3;
    }

    public Task(String name, String description, Status status,LocalDateTime startTime, Duration duration) {
        this(name,description,status);
        this.id = -3;
        this.startTime = startTime;
        this.duration = duration;

    }

    public Task(Task task) {
        this.name = task.name;
        this.description = task.description;
        this.id = task.getId();
        this.status = task.status;
        this.startTime = task.startTime;
        this.duration = task.duration;
    }

    public Task() {
    }

    public Task(String name, String description, int id, Status status) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = status;
    }

    public Task(String name, String description, int id, Status status, LocalDateTime startTime, Duration duration) {
        this(name, description, id, status);
        this.startTime = startTime;
        this.duration = duration;
    }

    public LocalDateTime getEndTime() throws NullPointerException {
        return startTime.plus(duration);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getStartTime() throws NullPointerException {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Duration getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        return "Task{" +
                "Id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", startTime=" + validLocalDateTime(startTime) +
                ", duration=" + validDuration(duration) +
                '}';
    }

    public String validLocalDateTime(LocalDateTime value) {
        try {
            return value.format(formatter);
        } catch (NullPointerException e) {
            return null;
        }
    }

    public String validDuration(Duration value) {
        try {
            return Long.toString(value.toMinutes());
        } catch (NullPointerException e) {
            return null;
        }
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {

        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(name, task.name) &&
                Objects.equals(description, task.description) && status == task.status &&
                Objects.equals(startTime, task.startTime) &&
                Objects.equals(duration, task.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
