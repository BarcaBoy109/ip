package kotha;

/** Performs mutations on a {@link TaskList}. */
public class TaskOperations {
    private final TaskList tasks;

    /** Creates operations backed by the supplied task list. */
    public TaskOperations(TaskList tasks) {
        this.tasks = tasks;
    }

    /** Adds a task to the list. */
    public void add(Task task) {
        tasks.add(task);
    }

    /** Removes and returns the task at the zero-based index. */
    public Task remove(int index) {
        return tasks.remove(index);
    }

    /** Marks the task at the zero-based index as done. */
    public void mark(int index) {
        tasks.get(index).markAsDone();
    }

    /** Marks the task at the zero-based index as not done. */
    public void unmark(int index) {
        tasks.get(index).markAsNotDone();
    }
}
