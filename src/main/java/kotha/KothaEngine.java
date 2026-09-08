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
        try {
            Parser.CommandType type = parser.parse(command);
            lastResponseStyle = type == Parser.CommandType.UNKNOWN
                    ? "exception" : type.name().toLowerCase();
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
                tasks.add(Task.createTodo(command));
                save();
                return "Got it. I have added the following todo task, master:\n" + tasks.get(tasks.size() - 1);
            case DEADLINE:
                tasks.add(Task.createDeadline(command));
                save();
                return "Got it. I have added the following deadline task, master:\n" + tasks.get(tasks.size() - 1);
            case EVENT:
                tasks.add(Task.createEvent(command));
                save();
                return "Got it. I have added the following event task, master:\n" + tasks.get(tasks.size() - 1);
            case MARK:
            case UNMARK:
                int markIndex = taskIndex(command, type == Parser.CommandType.MARK ? "mark" : "unmark");
                tasks.get(markIndex).setDone(type == Parser.CommandType.MARK);
                save();
                return "Updated task:\n" + tasks.get(markIndex);
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
        } catch (KothaException exception) {
            lastResponseStyle = "exception";
            return exception.getMessage();
        }
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
        if (!number.matches("\\d+")) throw new KothaException("Please provide a valid task number.");
        int index = Integer.parseInt(number) - 1;
        if (index < 0 || index >= tasks.size()) throw new KothaException("That task number does not exist.");
        return index;
    }

    private void save() {
        storage.saveTasks(tasks.asList());
    }
}
