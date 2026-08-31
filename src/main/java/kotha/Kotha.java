package kotha;

/**
 * A chatbot that echoes commands until the user exits.
 */
public class Kotha {
    private static TaskList listOfTasks = new TaskList();
    private static final Storage storage = new Storage("data/kotha.txt");
    private static final Ui ui = new Ui();
    private static final Parser parser = new Parser();
    private static TaskOperations taskOperations;

    /**
     * Starts the chatbot and processes commands from standard input.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        ui.showWelcome();

        listOfTasks = new TaskList(storage.loadTasks());
        taskOperations = new TaskOperations(listOfTasks, storage, ui);
        while (true) {
            String command = ui.readCommand();
            try {
                Parser.CommandType commandType = parser.parse(command);
                if (commandType == Parser.CommandType.BYE) {
                    ui.showGoodbye();
                    break;
                } else if (commandType == Parser.CommandType.LIST) {
                    ui.showTaskList(listOfTasks.asList());
                } else if (commandType == Parser.CommandType.MARK) {
                    taskOperations.changeStatus(command, true);
                } else if (commandType == Parser.CommandType.UNMARK) {
                    taskOperations.changeStatus(command, false);
                } else if (commandType == Parser.CommandType.TODO) {
                    taskOperations.addTodo(command);
                } else if (commandType == Parser.CommandType.DEADLINE) {
                    taskOperations.addDeadline(command);
                } else if (commandType == Parser.CommandType.EVENT) {
                    taskOperations.addEvent(command);
                } else if (commandType == Parser.CommandType.DELETE) {
                    taskOperations.delete(command);
                } else {
                    throw new KothaException("I don't recognise that command.");
                }
            } catch (KothaException e) {
                ui.showError(e.getMessage());
            }
        }
    }
}
