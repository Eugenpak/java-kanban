package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import service.ComparatorTaskStartTime;
import service.Status;

public class Epic extends Task {
    private final ArrayList<Subtask> arraySubtask = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic() {
    }

    public Epic(String name, String description) {
        super(name, description, Status.NEW);
    }
    public Epic(String name, String description,LocalDateTime startTime, Duration duration) {
        super(name, description, Status.NEW,startTime,duration);
    }

    public Epic(Epic epic) {
        super(epic.getName(), epic.getDescription(), epic.getId(),epic.getStatus());
        setStartTime(epic.getStartTime());
        setEndTime(epic.getEndTime());
        setDuration(epic.getDuration());
        if (epic.getArraySubtask() != null) {
            /*
            for (Subtask elem : epic.getArraySubtask()) {
                this.arraySubtask.add(elem);
            }
            */
            //epic.getArraySubtask().forEach(elem->arraySubtask.add(elem));
            arraySubtask.addAll(epic.getArraySubtask());
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
                    ", startTime=" + validLocalDateTime(getStartTime()) +
                    ", duration=" + getDuration() +
                    ", endTime=" + validLocalDateTime(getEndTime()) +
                    ", arraySubtask: " + arraySubtask +
                    "}";

    }



    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
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
        /*
        for (Subtask elem : arraySubtask) {
            this.arraySubtask.add(elem.copySubtask());
        }
        */
        arraySubtask.forEach(elem->this.arraySubtask.add(elem.copySubtask()));
    }

    public Status updateStatus() {
        if (arraySubtask.size() == 0) {
            super.setStatus(Status.NEW);
            return getStatus();
        }
        updateLocalDateTime();
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

    void updateLocalDateTime() {
        List<Subtask> result =arraySubtask
                .stream()
                .filter(s->s.getStartTime() != null)
                .sorted((Subtask a, Subtask b)->{
                    if (a.getStartTime().isAfter(b.getStartTime())) {
                        return 1;
                    } else if (a.getStartTime().isBefore(b.getStartTime())) {
                        return -1;
                    }
                     return 0;})
                .collect(Collectors.toList());
        if (!result.isEmpty()) {
            setStartTime(result.get(0).getStartTime());
            setEndTime(result.get(result.size()-1).getEndTime());
            setDuration(Duration.between(getStartTime(),getEndTime()));
        }
    }
}
