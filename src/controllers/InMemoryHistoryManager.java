package controllers;

import model.Epic;
import model.Subtask;
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
        Task copyTask = null;
        if (task instanceof Subtask){
            copyTask = new Subtask((Subtask) task);
        } else if (task instanceof Epic){
            copyTask = new Epic((Epic) task);
            //ArrayList<Subtask> listSubtask = ((Epic)copyTask).getArraySubtask();
        } else if (task instanceof Task){
            copyTask = new Task(task);
        }
        history.add(copyTask);
    }

    @Override
    public List<Task> getHistory(){
        return history;
    }

}
