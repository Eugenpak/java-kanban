package controllers;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final HashMap<Integer, Node<Task>> nodeMap;

    //Указатель на последний элемент списка. Он же last
    private Node<Task> tail;
    private Node<Task> first;

    class Node<E> {
        public E data;
        public Node<E> next;
        public Node<E> prev;

        public Node(Node<E> prev, E data, Node<E> next) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }

    Node<Task> getLastNode() {
        if (tail != null) {
            return tail;
        } else {
            return null;
        }
    }


    public InMemoryHistoryManager() {
        nodeMap = new HashMap<>();
    }

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        final int id = task.getId();
        Node<Task> nodeTask = nodeMap.get(id);
        removeNode(nodeTask);
        linkLast(task);
        nodeMap.put(id, tail);
    }

    @Override
    public void remove(int id) {
        if (nodeMap.containsKey(id)) {
            Task task = nodeMap.get(id).data;
            if (task instanceof Subtask) {
                //Удаление Subtask в Epic-ке
                int epicId = ((Subtask)task).getEpicId();
                if (epicId >= 0) {
                    ArrayList<Subtask> arraySub = ((Epic)nodeMap.get(epicId).data).getArraySubtask();
                    for (int i = 0; i < arraySub.size();i++) {
                        if (arraySub.get(i).getId() == id) {
                            arraySub.remove(i);
                        }
                    }
                }
            } else if (task instanceof Epic) {
                //Удаление всех Subtask, которые привязаны к Epic-ку
                ArrayList<Subtask> arraySub = ((Epic)task).getArraySubtask();
                for (Subtask elem:arraySub) {
                    removeNode(nodeMap.get(elem.getId()));
                }
            }
            removeNode(nodeMap.get(id));
        }
    }

     List<Task> getTasks() {
        final List<Task> copy = new LinkedList<>();
        Task copyElem;
        for (Node<Task> x = first; x != null; x = x.next) {
            if (x.data instanceof Subtask) {
                copyElem = new Subtask((Subtask) x.data);
            } else if (x.data instanceof Epic) {
                copyElem = new Epic((Epic) x.data);
            } else if (x.data != null) {
                copyElem = new Task(x.data);
            } else {
                copyElem = null;
            }
            final Task t = copyElem;
            copy.add(t);
        }
        return copy;
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    void linkLast(Task element) {
        final Node<Task> node = new Node<>(tail, element, null);
        if (first == null) { //Список пустой-в начало
            first = node;
        } else {  //Список не пустой-прикрепляем в конец
            tail.next = node;
        }
        tail = node; //Сдвинуть указатель в любом случае
    }

    void removeNode(Node<Task> x) {
        if (x == null) {
            return;
        }
        final int id = x.data.getId();
        nodeMap.remove(id);
        final Node<Task> next = x.next;
        final Node<Task> prev = x.prev;

        if (prev == null) {
            first = next;
        } else {
            prev.next = next;
            x.prev = null;
        }

        if (next == null) {
            tail = prev;
        } else {
            next.prev = prev;
            x.next = null;
        }

        x.data = null;
    }
}
