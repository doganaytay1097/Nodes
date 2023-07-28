package ceng.estu.edu;

import java.util.*;

public class Node extends Thread{
    String name;
    ArrayList<Node> predecessors = new ArrayList<>();
    ArrayList<Node> successors = new ArrayList<>();
    boolean isCompleted = false;
    public Object lock = new Object();

    Random random = new Random();
    int numPredecessors;
    public Node(String name ) {
        this.name = name;
    }


    public String predecessorsToString(ArrayList<Node> predecessors) {
        String str = "";
        for(Node n : predecessors) {
            str += n.name + " ";
        }
        return str;
    }

    public void performOrWait() {
        while (numPredecessors > 0) {
            synchronized (lock) {
                System.out.println("Node " + this.name + " is waiting for Nodes " + this.predecessorsToString(this.predecessors));
                try {
                    lock.wait(predecessors.size() * 2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        perform();
    }

    public void unlock() {
        for (Node n : this.successors) {
            n.numPredecessors--;
            if (n.numPredecessors == 0 && !isCompleted) {
                n.lock.notify();
            }
        }
    }

    public void perform() {
        if (!this.isCompleted) {
            System.out.println("Node " + this.name + " is being started");
            try {
                Thread.sleep(random.nextInt(2000));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Node " + this.name + " is completed");
            this.isCompleted = true;
            unlock();
        }
    }

    @Override
    public void run() {
        performOrWait();
    }
}