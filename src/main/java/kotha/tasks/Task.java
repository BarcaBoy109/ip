package kotha.tasks;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import kotha.KothaException;

/** Represents a task that can be completed or left incomplete. */
public class Task {
    private static final DateTimeFormatter DAY_MONTH_FORMAT =
            new java.time.format.DateTimeFormatterBuilder()
                    .appendPattern("d/M")
                    .parseDefaulting(java.time.temporal.ChronoField.YEAR, Year.now().getValue())
                    .toFormatter();
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HHmm");
    protected String description;
    protected boolean isDone;

    /** Creates an incomplete task with the supplied description. */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /** Creates a to-do task from a user command. */
    public static Task createTodo(String command) throws KothaException {
        String description = command.substring("todo".length()).trim();
        if (description.isEmpty()) {
            throw new KothaException("Your Majesty, a description is required after 'todo'.");
        }
        return new ToDo(description);
    }

    /** Creates a deadline task from a user command. */
    public static Task createDeadline(String command) throws KothaException {
        int byIndex = command.indexOf(" /by ");
        if (byIndex <= "deadline".length()) {
            throw new KothaException(
                    "Your Majesty, a deadline requires a description and a '/by' date.");
        }
        String description = command.substring("deadline".length(), byIndex).trim();
        String byText = command.substring(byIndex + " /by ".length()).trim();
        if (description.isEmpty() || byText.isEmpty()) {
            throw new KothaException(
                    "Your Majesty, a deadline requires a description and a '/by' date.");
        }
        return new Deadline(description, parseDateTime(byText));
    }

    /** Creates an event task from a user command. */
    public static Task createEvent(String command) throws KothaException {
        int fromIndex = command.indexOf(" /from ");
        int toIndex = command.indexOf(" /to ");
        if (fromIndex <= "event".length() || toIndex <= fromIndex) {
            throw new KothaException(
                    "Your Majesty, an event requires a description, '/from', and '/to' time.");
        }
        String description = command.substring("event".length(), fromIndex).trim();
        String fromText = command.substring(fromIndex + " /from ".length(), toIndex).trim();
        String toText = command.substring(toIndex + " /to ".length()).trim();
        if (description.isEmpty() || fromText.isEmpty() || toText.isEmpty()) {
            throw new KothaException(
                    "Your Majesty, an event requires a description, '/from', and '/to' time.");
        }
        return new Event(description, parseDateTime(fromText), parseDateTime(toText));
    }

    /** Returns the status icon for this task. */
    public String getStatusIcon() {
        return isDone ? "[X]" : "[ ]";
    }

    /** Marks the task as <b>done</b> and prints the updated status of that task. */
    public void markAsDone() {
        isDone = true;
        System.out.println("I have marked the following task as done:");
        System.out.println(this.toString());
    }

    /** Marks the task as <b>not done</b> and prints the updated status of that task. */
    public void markAsNotDone() {
        isDone = false;
        System.out.println("I have marked the following task as not done:");
        System.out.println(this.toString());
    }

    /** Returns the date and time represented by supported user input. */
    private static LocalDateTime parseDateTime(String dateTimeText) throws KothaException {
        String[] parts = dateTimeText.split("\\s+");
        try {
            if (parts.length == 1) {
                return LocalDateTime.of(parseDate(parts[0]), LocalTime.of(23, 59));
            }
            if (parts.length == 2) {
                return LocalDateTime.of(parseDate(parts[0]), LocalTime.parse(parts[1], TIME_FORMAT));
            }
        } catch (DateTimeParseException ignored) {
            // Invalid input.
        }
        throw new KothaException(
                "Your Majesty, use d/M, d/M/yy, or d/M/yyyy, optionally followed by HHmm.");
    }

    /** Returns the date represented by supported user input. */
    private static LocalDate parseDate(String dateText) {
        try {
            return LocalDate.parse(dateText, DateTimeFormatter.ofPattern("d/M/uuuu"));
        } catch (DateTimeParseException ignored) {
            // Try the next format.
        }
        try {
            return LocalDate.parse(dateText, DateTimeFormatter.ofPattern("d/M/uu"));
        } catch (DateTimeParseException ignored) {
            // Try the day/month format.
        }
        return LocalDate.parse(dateText, DAY_MONTH_FORMAT);
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
    public void setDone(boolean isDone) {
        this.isDone = isDone;
    }

    @Override
    public String toString() {
        return getStatusIcon() + " " + description;
    }

    /** Prints a confirmation that this task was added. */
    public void printAddText() {
        System.out.println("____________________________________________________________");
        System.out.println("Got it master I have added the following task: " + this.toString());

    }

    /** Prints a confirmation that this task was removed. */
    public void printRemoveText() {
        System.out.println("____________________________________________________________");
        System.out.println("Got it master I have removed the following task: " + this.toString());
    }
}
