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
        final List<Task> copy = new ArrayList<>();
        Task copyElem;
        for (Task elem : history){
            if (elem instanceof Subtask){
                copyElem= new Subtask((Subtask) elem);
            } else if (elem instanceof Epic){
                copyElem= new Epic((Epic) elem);
            } else if (elem instanceof Task){
                copyElem= new Task(elem);
            } else {
                copyElem=null;
            }
            copy.add(copyElem);
        }
        return copy;
        //return List.copyOf(history);
    }

}
