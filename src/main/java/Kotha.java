import java.util.Scanner;

/**
 * A chatbot that echoes commands until the user exits.
 */
public class Kotha {
    /**
     * Starts the chatbot and processes commands from standard input.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        // The banner was made using Codex
        String banner = " _  __   ___  _____ _   _    _    \n"
                + "| |/ /  / _ \\|_   _| | | |  / \\   \n"
                + "| ' /  | | | | | | | |_| | / _ \\  \n"
                + "| . \\  | |_| | | | |  _  |/ ___ \\ \n"
                + "|_|\\_\\  \\___/  |_| |_| |_/_/   \\_\\\n";
        System.out.println(banner);
        System.out.println("____________________________________________________________");
        System.out.println("Hello! I'm KOTHA.");
        System.out.println("What can I do for you?");
        System.out.println("____________________________________________________________");

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String command = scanner.nextLine();
            if (command.equals("bye")) {
                System.out.println("____________________________________________________________");
                System.out.println("Bye. Hope to see you again soon!");
                System.out.println("____________________________________________________________");
                break;
            } else if (command.equals("list")) {
                stringList();
            } else if (command.matches("mark (\\d+)")) {
                changeTaskStatus(command, true);
            } else if (command.matches("unmark (\\d+)")) {
                changeTaskStatus(command, false);
            } else {
                addToList(command);
                System.out.println("____________________________________________________________");
                System.out.println("added: " + command);
                System.out.println("____________________________________________________________");
            }
        }
    }

    // Array containing the list of tasks
    private static String[] listOfTask = new String[100];
    // Records whether each task has been marked as done.
    private static boolean[] isTaskDone = new boolean[100];
    // Points to the first free slot in listOfTask (one past the last element put inside)
    private static byte listOfTaskPointer = 0;

    private static void addToList(String command) {
        listOfTask[listOfTaskPointer] = command;
        listOfTaskPointer++;
    }
    private static void stringList() {
        System.out.println("____________________________________________________________");
        System.out.println("Here are the tasks in your list:");
        for (byte pointer = 0; pointer < listOfTaskPointer; pointer++) {
            byte number = (byte) (pointer + 1);
            String status = isTaskDone[pointer] ? "[X]" : "[ ]";
            System.out.printf("%d.%s %s%n", number, status, listOfTask[pointer]);
        }
        System.out.println("____________________________________________________________");

    }

    /**
     * Inspiration taken from Codex
     * Updates the done status of a task and displays the result.
     *
     * @param command the mark or unmark command containing a one-based task number
     * @param isDone the new done status for the task
     */
    private static void changeTaskStatus(String command, boolean isDone) {
        int taskNumber = Integer.parseInt(command.substring(command.indexOf(' ') + 1));
        int taskIndex = taskNumber - 1;
        if (taskIndex < 0 || taskIndex >= listOfTaskPointer) {
            System.out.println("____________________________________________________________");
            System.out.println("That task number does not exist.");
            System.out.println("____________________________________________________________");
            return;
        }

        isTaskDone[taskIndex] = isDone;
        System.out.println("____________________________________________________________");
        if (isDone) {
            System.out.println("OK, I've marked this task as done:");
            System.out.printf("  [X] %s%n", listOfTask[taskIndex]);
        } else {
            System.out.println("OK, I've marked this task as not done yet:");
            System.out.printf("  [ ] %s%n", listOfTask[taskIndex]);
        }
        System.out.println("____________________________________________________________");
    }

}
