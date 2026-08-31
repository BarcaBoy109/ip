package kotha;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/** Performs validated task operations and persists their results. */
public class TaskOperations {
    private static final DateTimeFormatter DAY_MONTH_FORMAT =
            new java.time.format.DateTimeFormatterBuilder()
                    .appendPattern("d/M")
                    .parseDefaulting(java.time.temporal.ChronoField.YEAR, Year.now().getValue())
                    .toFormatter();
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HHmm");

    private final TaskList tasks;
    private final Storage storage;
    private final Ui ui;

    /** Creates operations backed by the supplied task list and storage. */
    public TaskOperations(TaskList tasks, Storage storage, Ui ui) {
        this.tasks = tasks;
        this.storage = storage;
        this.ui = ui;
    }

    /** Adds a todo task from a user command. */
    public void addTodo(String command) throws KothaException {
        String description = command.substring("todo".length()).trim();
        if (description.isEmpty()) {
            throw new KothaException("Please add a description after 'todo'.");
        }
        add(new ToDo(description));
    }

    /** Adds a deadline task from a user command. */
    public void addDeadline(String command) throws KothaException {
        int byIndex = command.indexOf(" /by ");
        if (byIndex <= "deadline".length()) {
            throw new KothaException("A deadline needs a description and a '/by' date.");
        }
        String description = command.substring("deadline".length(), byIndex).trim();
        String byText = command.substring(byIndex + " /by ".length()).trim();
        if (description.isEmpty() || byText.isEmpty()) {
            throw new KothaException("A deadline needs a description and a '/by' date.");
        }
        add(new Deadline(description, parseDateTime(byText)));
    }

    /** Adds an event task from a user command. */
    public void addEvent(String command) throws KothaException {
        int fromIndex = command.indexOf(" /from ");
        int toIndex = command.indexOf(" /to ");
        if (fromIndex <= "event".length() || toIndex <= fromIndex) {
            throw new KothaException("An event needs a description, '/from', and '/to' time.");
        }
        String description = command.substring("event".length(), fromIndex).trim();
        String fromText = command.substring(fromIndex + " /from ".length(), toIndex).trim();
        String toText = command.substring(toIndex + " /to ".length()).trim();
        if (description.isEmpty() || fromText.isEmpty() || toText.isEmpty()) {
            throw new KothaException("An event needs a description, '/from', and '/to' time.");
        }
        add(new Event(description, parseDateTime(fromText), parseDateTime(toText)));
    }

    /** Marks or unmarks a task using its one-based number. */
    public void changeStatus(String command, boolean isDone) throws KothaException {
        String commandWord = isDone ? "mark" : "unmark";
        int taskIndex = parseTaskIndex(command, commandWord,
                "Please provide a valid task number to " + commandWord + ".");
        if (isDone) {
            mark(taskIndex);
        } else {
            unmark(taskIndex);
        }
        save();
    }

    /** Deletes a task using its one-based number. */
    public void delete(String command) throws KothaException {
        String taskNumberText = command.substring("delete".length()).trim();
        if (!taskNumberText.matches("\\d+")) {
            throw new KothaException("Cannot delete task number " + taskNumberText
                    + "\nPlease enter an integer");
        }
        int taskIndex = Integer.parseInt(taskNumberText) - 1;
        ensureTaskExists(taskIndex,
                "Dont try to delete tasks outside of your range! I am watching you ⊙▃⊙");
        tasks.get(taskIndex).printRemoveText();
        remove(taskIndex);
        ui.showTaskCount(tasks.size());
        save();
    }

    private void add(Task task) {
        tasks.add(task);
        task.printAddText();
        ui.showTaskCount(tasks.size());
        save();
    }

    private void remove(int index) {
        tasks.remove(index);
    }

    private void mark(int index) {
        tasks.get(index).markAsDone();
    }

    private void unmark(int index) {
        tasks.get(index).markAsNotDone();
    }

    private int parseTaskIndex(String command, String commandWord, String invalidMessage)
            throws KothaException {
        String taskNumberText = command.substring(commandWord.length()).trim();
        if (!taskNumberText.matches("\\d+")) {
            throw new KothaException(invalidMessage);
        }
        int taskIndex;
        try {
            taskIndex = Integer.parseInt(taskNumberText) - 1;
        } catch (NumberFormatException e) {
            throw new KothaException(invalidMessage);
        }
        ensureTaskExists(taskIndex, "That task number does not exist.");
        return taskIndex;
    }

    private void ensureTaskExists(int index, String message) throws KothaException {
        if (index < 0 || index >= tasks.size()) {
            throw new KothaException(message);
        }
    }

    private LocalDateTime parseDateTime(String dateTimeText) throws KothaException {
        String[] parts = dateTimeText.split("\\s+");
        try {
            if (parts.length == 1) {
                return LocalDateTime.of(parseDate(parts[0]), LocalTime.of(23, 59));
            }
            if (parts.length == 2) {
                return LocalDateTime.of(parseDate(parts[0]), LocalTime.parse(parts[1], TIME_FORMAT));
            }
        } catch (DateTimeParseException ignored) {
            // Invalid input
        }
        throw new KothaException(
                "Use d/M, d/M/yy, or d/M/yyyy, optionally followed by HHmm.");
    }

    private LocalDate parseDate(String dateText) {
        try {
            return LocalDate.parse(dateText, DateTimeFormatter.ofPattern("d/M/uuuu"));
        } catch (DateTimeParseException ignored) {
            // Try next format
        }
        try {
            return LocalDate.parse(dateText, DateTimeFormatter.ofPattern("d/M/uu"));
        } catch (DateTimeParseException ignored) {
            // Try d/M
        }
        return LocalDate.parse(dateText, DAY_MONTH_FORMAT);
    }

    private void save() {
        storage.saveTasks(tasks.asList());
    }

}
