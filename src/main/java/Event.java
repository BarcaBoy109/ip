package main.java;

public class Event extends Task {
    protected String from;
    protected String to;
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + "(from: " + this.from + " to: " + this.to + ")";
    }

    @Override
    public void printAddText() {
        System.out.println("Master I have added the following event:");
        System.out.println(this.toString());
    }
}
