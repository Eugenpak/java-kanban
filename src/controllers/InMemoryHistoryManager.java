package controllers;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private ArrayList<Task> history;

    public InMemoryHistoryManager(){
        history = new ArrayList<>(10);
    }

    @Override
    public void add(Task task){
        if (history.size()>=10) {
            history.remove(0);
        }
        history.add(task);
    }

    @Override
    public List<Task> getHistory(){
        return history;
    }

}
