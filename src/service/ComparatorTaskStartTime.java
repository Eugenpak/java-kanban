package service;

import model.Task;
import java.util.Comparator;

public class ComparatorTaskStartTime implements Comparator<Task> {
    @Override
    public int compare(Task a, Task b){
        if (a.getStartTime().isAfter(b.getStartTime())) {
            return 1;
        } else if (a.getStartTime().isBefore(b.getStartTime())) {
            return -1;
        }
        return 0;
    }
}
