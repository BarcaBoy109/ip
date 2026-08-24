package main.java;

public class ToDo extends Task {
    public ToDo(String description) {
        super(description);
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }

    @Override
    public void printAddText() {
        System.out.println("Master I have added the following task:");
        System.out.println(this.toString());
    }
}
