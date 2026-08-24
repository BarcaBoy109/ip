package main.java;

public class Task {

    protected String description;
    protected boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public String getStatusIcon() {
        return isDone ? "[X]" : "[ ]";
    }

    public void markAsDone() {
        isDone = true;
        System.out.println("I have marked the following task as done:");
        System.out.println(this.toString());
    }

    public void markAsNotDone() {
        isDone = false;
        System.out.println("I have marked the following task as not done:");
        System.out.println(this.toString());
    }

    @Override
    public String toString() {
        return getStatusIcon() + " " + description;
    }

    public void printAddText() {
        System.out.println("____________________________________________________________");
        System.out.println("Got it master I have added the following task: " + this.toString());

    }
}
