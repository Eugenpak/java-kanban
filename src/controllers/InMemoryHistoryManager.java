package controllers;

import model.Epic;
import model.Subtask;
import model.Task;
import service.Node;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private List<Task> history;
    private final HashMap<Integer, Node<Task>> nodeMap;

    //Указатель на последний элемент списка. Он же last
    private Node<Task> tail;
    private Node<Task> first;

    Node<Task> getValueNodeMapById(int id){
        if (nodeMap.containsKey(id)) {
            return nodeMap.get(id);
        } else {
            return new Node(null,null,null);
        }
    }

    public InMemoryHistoryManager(){
        history = new LinkedList<>();
        nodeMap = new HashMap<>();
    }

    @Override
    public void add(Task task){
        if (task == null) {
            return;
        }
        linkLast(task);
        //history.add(task);
        history = getHistory();
    }
    @Override
    public void remove(int id){
        if (nodeMap.containsKey(id)){
            Task task =nodeMap.get(id).data;
            if (task instanceof Subtask) {
                int epicId = ((Subtask)task).getEpicId();
                if (epicId>=0) {
                    ArrayList<Subtask> arraySub = ((Epic)nodeMap.get(epicId).data).getArraySubtask();
                    for (int i=0; i<arraySub.size();i++){
                        if (arraySub.get(i).getId()==id) {
                            arraySub.remove(i);
                        }
                    }
                }
                removeNode(nodeMap.get(id));
                nodeMap.remove(id);
            } else if (task instanceof Epic) {
                ArrayList<Subtask> arraySub = ((Epic)task).getArraySubtask();
                for (Subtask elem:arraySub){
                    removeNode(nodeMap.get(elem.getId()));
                    nodeMap.remove(elem.getId());
                }
                removeNode(nodeMap.get(id));
            } else if (task instanceof Task) {
                removeNode(nodeMap.get(id));
            }
            nodeMap.remove(id);
            history = getHistory();
        }
    }

     List<Task> getTasks(){
        final List<Task> copy = new LinkedList<>();
        Task copyElem;
        for (Node<Task> x = first; x != null; x = x.next) {
            if (x.data instanceof Subtask){
                copyElem= new Subtask((Subtask) x.data);
            } else if (x.data instanceof Epic){
                copyElem= new Epic((Epic) x.data);
            } else if (x.data != null){
                copyElem= new Task(x.data);
            } else {
                copyElem=null;
            }
            final Task t = copyElem;
            copy.add(t);
        }
        return copy;
    }

    @Override
    public List<Task> getHistory(){
        return getTasks();
    }


    void linkLast(Task element) {
        // Реализуйте метод
        final Node<Task> oldTail = tail;
        final Node<Task> newNode = new Node<>(oldTail, element, null);
        tail = newNode;
        if (oldTail == null){
            first = newNode;
        }
        else
            oldTail.next = newNode;

        Integer idTask = element.getId();
        if (nodeMap.containsKey(idTask)){
            final Node<Task> temp = nodeMap.get(idTask);
            // удалить узел.
            removeNode(temp);
            // обновляем положение element в nodeMap
            nodeMap.put(element.getId(),newNode);
            //temp.data = null;
        } else {
            nodeMap.put(element.getId(), newNode);
        }
    }

    Task removeNode(Node<Task> x) {
        // assert x != null;
        final Task element = x.data;
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
        return element;
    }
}
