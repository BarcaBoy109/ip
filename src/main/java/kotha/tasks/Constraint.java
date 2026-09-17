package kotha.tasks;

/** Represents a task that can only be done after a specified time or task. */
public class Constraint extends Task {
    private final String after;

    /** Creates an incomplete constraint task with the supplied trigger. */
    public Constraint(String description, String after) {
        super(description);
        this.after = after;
    }

    /** Returns the trigger text for persistent storage. */
    public String getAfter() {
        return after;
    }

    @Override
    public String toString() {
        return "[C]" + super.toString() + " (after: " + after + ")";
    }
}
