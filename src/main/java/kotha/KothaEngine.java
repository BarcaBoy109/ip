package kotha;

import java.util.List;

import kotha.tasks.Task;
import kotha.tasks.TaskList;

/** Executes Kotha commands independently of the user interface. */
public class KothaEngine {
    private final Storage storage;
    private final TaskList tasks;
    private final Parser parser = new Parser();
    private String lastResponseStyle = "list";

    /** Creates an engine backed by the normal Kotha data file. */
    public KothaEngine() {
        storage = new Storage("data/kotha.txt");
        tasks = new TaskList(storage.loadTasks());
    }

    /** Executes one command and returns the text that an interface should display. */
    public String processCommand(String command) {
        assert command != null : "The engine must process a non-null command";
        try {
            Parser.CommandType type = parser.parse(command);
            lastResponseStyle = type == Parser.CommandType.UNKNOWN
                    ? "exception" : type.name().toLowerCase();
            return executeCommand(type, command);
        } catch (KothaException exception) {
            lastResponseStyle = "exception";
            return exception.getMessage();
        }
    }

    /** Executes a command after it has been parsed and records its effect. */
    private String executeCommand(Parser.CommandType type, String command) throws KothaException {
        switch (type) {
            case LIST:
                return formatTasks(tasks.asList(), "Here are the tasks in your list:");
            case FIND:
                String keyword = command.substring("find".length()).trim();
                if (keyword.isEmpty()) {
                    throw new KothaException("Master please provide a keyword to find.");
                }
                return formatTasks(tasks.find(keyword), "Master here are the matching tasks in your list:");
            case TODO:
                Task todo = Task.createTodo(command);
                assert todo != null : "Successful todo creation must produce a task";
                return addTask(todo, "Got it. I have added the following todo task, master:");
            case DEADLINE:
                Task deadline = Task.createDeadline(command);
                assert deadline != null : "Successful deadline creation must produce a task";
                return addTask(deadline, "Got it. I have added the following deadline task, master:");
            case EVENT:
                Task event = Task.createEvent(command);
                assert event != null : "Successful event creation must produce a task";
                return addTask(event, "Got it. I have added the following event task, master:");
            case CONSTRAINT:
                Task constraint = Task.createConstraint(command);
                assert constraint != null : "Successful constraint creation must produce a task";
                return addTask(constraint, "Got it. I have added the following constraint task, master:");
            case MARK:
            case UNMARK:
                return updateTaskStatus(command, type == Parser.CommandType.MARK);
            case DELETE:
                int deleteIndex = taskIndex(command, "delete");
                Task removed = tasks.remove(deleteIndex);
                save();
                return "I have deleted the following task from the face of this planet:\n" + removed;
            case BYE:
                return "Bye. Hope to not see you again soon!";
            default:
                return "I do not recognise whatever you have written up there";
        }
    }

    /** Adds a newly created task, persists it, and formats the confirmation response. */
    private String addTask(Task task, String responsePrefix) {
        tasks.add(task);
        save();
        return responsePrefix + "\n" + tasks.get(tasks.size() - 1);
    }

    /** Updates a task's completion status, persists it, and formats the response. */
    private String updateTaskStatus(String command, boolean isDone) throws KothaException {
        String commandWord = isDone ? "mark" : "unmark";
        int taskIndex = taskIndex(command, commandWord);
        tasks.get(taskIndex).setDone(isDone);
        save();
        return "Updated task:\n" + tasks.get(taskIndex);
    }

    /** Returns the style category for the most recently processed response. */
    public String getLastResponseStyle() {
        return lastResponseStyle;
    }

    private String formatTasks(List<Task> selected, String heading) {
        if (selected.isEmpty()) {
            return "You got nothing to do.";
        }
        StringBuilder result = new StringBuilder(heading).append("\n");
        for (int index = 0; index < selected.size(); index++) {
            result.append(index + 1).append(". ").append(selected.get(index)).append("\n");
        }
        return result.toString().trim();
    }

    private int taskIndex(String command, String keyword) throws KothaException {
        String number = command.substring(keyword.length()).trim();
        if (!number.matches("\\d+")) {
            throw new KothaException("Please provide a valid task number.");
        }
        int index = Integer.parseInt(number) - 1;
        if (index < 0 || index >= tasks.size()) {
            throw new KothaException("That task number does not exist.");
        }
        assert index >= 0 && index < tasks.size()
                : "A validated task number must resolve to an existing task";
        return index;
    }

    private void save() {
        storage.saveTasks(tasks.asList());
    }
}
