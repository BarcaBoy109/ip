package main.java;

/** Represents a task with a deadline. */
public class Deadline extends Task {
    protected String deadline;
    public Deadline(String description, String deadline) {
        super(description);
        this.deadline = deadline;
    }

    /** Returns the deadline text for persistent storage. */
    public String getDeadline() {
        return deadline;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + "(by: " + deadline + ")";
    }

    @Override
    public void printAddText() {
        System.out.println("Master I have added the following task:");
        System.out.println(this.toString());
    }
}
