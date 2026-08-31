package main.java.kotha;

/** Represents a task that can be completed or left incomplete. */
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

    /** Returns the task description for persistent storage. */
    public String getDescription() {
        return description;
    }

    /** Returns whether this task has been marked as complete. */
    public boolean isDone() {
        return isDone;
    }

    /** Restores the completion status without printing a user message. */
    void setDone(boolean isDone) {
        this.isDone = isDone;
    }

    @Override
    public String toString() {
        return getStatusIcon() + " " + description;
    }

    public void printAddText() {
        System.out.println("____________________________________________________________");
        System.out.println("Got it master I have added the following task: " + this.toString());

    }

    public void printRemoveText() {
        System.out.println("____________________________________________________________");
        System.out.println("Got it master I have removed the following task: " + this.toString());
    }
}
