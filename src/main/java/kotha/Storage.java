package kotha;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import kotha.tasks.Deadline;
import kotha.tasks.Event;
import kotha.tasks.Task;
import kotha.tasks.ToDo;

/** Saves tasks to, and restores tasks from, a local text file. */
public class Storage {
    private static final String SEPARATOR = " | ";
    private final Path filePath;

    /** Creates storage backed by the supplied file path. */
    public Storage(String filePath) {
        this.filePath = Path.of(filePath);
    }

    /** Loads all the valid tasks stored on disk. */
    public ArrayList<Task> loadTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        if (!Files.exists(filePath)) {
            return tasks;
        }

        try {
            for (String line : Files.readAllLines(filePath)) {
                Task task = parseTask(line);
                if (task != null) {
                    tasks.add(task);
                }
            }
        } catch (IOException e) {
            System.out.println("Your Majesty, this humble butler could not load your saved tasks.");
        }
        return tasks;
    }

    /**
     * Saves the complete task list, replacing the previous saved version.
     *
     * @param tasks the list of tasks to save in disk
     */
    public void saveTasks(List<? extends Task> tasks) {
        try {
            Path parent = filePath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            List<String> lines = new ArrayList<>();
            for (Task task : tasks) {
                lines.add(formatTask(task));
            }
            Files.write(filePath, lines);
        } catch (IOException e) {
            System.out.println("Your Majesty, this humble butler could not save your tasks.");
        }
    }

    private Task parseTask(String line) {
        String[] parts = line.split(" \\| ", -1);
        if (parts.length < 3) {
            return null;
        }

        Task task;
        try {
            switch (parts[0]) {
                case "T":
                    task = new ToDo(parts[2]);
                    break;
                case "D":
                    if (parts.length != 4) {
                        return null;
                    }
                    task = new Deadline(parts[2], LocalDateTime.parse(parts[3]));
                    break;
                case "E":
                    if (parts.length != 5) {
                        return null;
                    }
                    task = new Event(parts[2], LocalDateTime.parse(parts[3]),
                            LocalDateTime.parse(parts[4]));
                    break;
                default:
                    return null;
            }
        } catch (DateTimeParseException exception) {
            return null;
        }
        task.setDone("1".equals(parts[1]));
        return task;
    }

    private String formatTask(Task task) {
        String status = task.isDone() ? "1" : "0";
        if (task instanceof Deadline) {
            Deadline deadline = (Deadline) task;
            return "D" + SEPARATOR + status + SEPARATOR + task.getDescription()
                    + SEPARATOR + deadline.getDeadline();
        }
        if (task instanceof Event) {
            Event event = (Event) task;
            return "E" + SEPARATOR + status + SEPARATOR + task.getDescription()
                    + SEPARATOR + event.getFrom() + SEPARATOR + event.getTo();
        }
        if (task instanceof ToDo) {
            return "T" + SEPARATOR + status + SEPARATOR + task.getDescription();
        }
        throw new IllegalArgumentException("Your Majesty, you have bestowed upon me an unsupported task type.");
    }
}
