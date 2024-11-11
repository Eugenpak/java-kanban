package controllers;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final ArrayList<Task> history;

    public InMemoryHistoryManager(){
        history = new ArrayList<>(10);
    }

    @Override
    public void add(Task task){
        if (task == null) {
            return;
        }
        if (history.size()>=10) {
            history.remove(0);
        }
        history.add(task);
    }

    @Override
    public List<Task> getHistory(){
        return new ArrayList<>(history);
    }

}
