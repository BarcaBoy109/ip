package main.java;

import java.util.ArrayList;
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
            String command = scanner.nextLine().trim();
            try {
                if (command.equals("bye")) {
                    System.out.println("____________________________________________________________");
                    System.out.println("Bye. Hope to see you again soon!");
                    System.out.println("____________________________________________________________");
                    break;
                } else if (command.equals("list")) {
                    stringList();
                } else if (command.equals("mark") || command.startsWith("mark ")) {
                    changeTaskStatus(command, true);
                } else if (command.equals("unmark") || command.startsWith("unmark ")) {
                    changeTaskStatus(command, false);
                } else if (command.equals("todo") || command.startsWith("todo ")) {
                    addTodo(command);
                } else if (command.equals("deadline") || command.startsWith("deadline ")) {
                    addDeadline(command);
                } else if (command.equals("event") || command.startsWith("event ")) {
                    addEvent(command);
                } else if (command.equals("delete") || command.startsWith("delete ")) {
                    deleteTask(command);
                } else {
                    throw new KothaException("I don't recognise that command.");
                }
            } catch (KothaException e) {
                printError(e.getMessage());
            }
        }
    }

    // Array containing the list of tasks
    private static ArrayList<Task> listOfTask = new ArrayList<>();

    private static void addToList(Task task) {
        listOfTask.add(task);
        task.printAddText();
        printNumOfTasks();
    }
    private static void printNumOfTasks() {
        if (listOfTask.size() == 1) {
            System.out.println("Hiee you now you have " + listOfTask.size() + " task in your list.");
        } else {
            System.out.println("Hiee you now you have " + listOfTask.size() + " tasks in your list.");
        }
        System.out.println("____________________________________________________________");
    }
    private static void removeFromList(int index) {
        listOfTask.remove(index);
        printNumOfTasks();
    }

    private static void stringList() {
        System.out.println("____________________________________________________________");
        System.out.println("Here are the tasks in your list:");
        for (int pointer = 0; pointer < listOfTask.size(); pointer++) {
            int number = pointer + 1;
            System.out.printf("%d.%s%n", number, listOfTask.get(pointer).toString());
        }
        System.out.println("____________________________________________________________");

    }

    /** Adds a todo after checking that it has a description. */
    private static void addTodo(String command) throws KothaException {
        String description = command.substring("todo".length()).trim();
        if (description.isEmpty()) {
            throw new KothaException("Please add a description after 'todo'.");
        }
        addToList(new ToDo(description));
    }

    /** Adds a deadline after checking its description and by date. */
    private static void addDeadline(String command) throws KothaException {
        int byIndex = command.indexOf(" /by ");
        if (byIndex <= "deadline".length()) {
            throw new KothaException("A deadline needs a description and a '/by' date.");
        }

        String description = command.substring("deadline".length(), byIndex).trim();
        String by = command.substring(byIndex + " /by ".length()).trim();
        if (description.isEmpty() || by.isEmpty()) {
            throw new KothaException("A deadline needs a description and a '/by' date.");
        }
        addToList(new Deadline(description, by));
    }

    /** Adds an event after checking its description, start time, and end time. */
    private static void addEvent(String command) throws KothaException {
        int fromIndex = command.indexOf(" /from ");
        int toIndex = command.indexOf(" /to ");
        if (fromIndex <= "event".length() || toIndex <= fromIndex) {
            throw new KothaException("An event needs a description, '/from', and '/to' time.");
        }

        String description = command.substring("event".length(), fromIndex).trim();
        String from = command.substring(fromIndex + " /from ".length(), toIndex).trim();
        String to = command.substring(toIndex + " /to ".length()).trim();
        if (description.isEmpty() || from.isEmpty() || to.isEmpty()) {
            throw new KothaException("An event needs a description, '/from', and '/to' time.");
        }
        addToList(new Event(description, from, to));
    }

    private static void deleteTask(String command) throws KothaException {
        String taskNumberText = command.substring("delete".length()).trim();
        if (!taskNumberText.matches("\\d+")) {
            throw new KothaException("Cannot delete task number " + taskNumberText + "\nPlease enter an integer");
        }
        int taskNumber = Integer.parseInt(taskNumberText);
        int taskIndex = taskNumber - 1;
        if (taskIndex < 0 || taskIndex >= listOfTask.size()) {
            throw new KothaException("Dont try to delete tasks outside of your range! I am watching you ⊙▃⊙");
        }
        listOfTask.get(taskIndex).printRemoveText();
        listOfTask.remove(taskIndex);
        printNumOfTasks();
    }

    /** Marks or unmarks a task after checking its one-based task number. */
    private static void changeTaskStatus(String command, boolean isDone) throws KothaException {
        String commandWord = isDone ? "mark" : "unmark";
        String taskNumberText = command.substring(commandWord.length()).trim();
        if (!taskNumberText.matches("\\d+")) {
            throw new KothaException("Please provide a valid task number to " + commandWord + ".");
        }

        int taskIndex;
        try {
            taskIndex = Integer.parseInt(taskNumberText) - 1;
        } catch (NumberFormatException e) {
            throw new KothaException("Please provide a valid task number to " + commandWord + ".");
        }
        if (taskIndex < 0 || taskIndex >= listOfTask.size()) {
            throw new KothaException("That task number does not exist.");
        }

        if (isDone) {
            listOfTask.get(taskIndex).markAsDone();
        } else {
            listOfTask.get(taskIndex).markAsNotDone();
        }
    }

    /** Displays a consistent error message for invalid commands. */
    private static void printError(String message) {
        System.out.println("____________________________________________________________");
        System.out.println("Master thou has committed a misprision! " + message);
        System.out.println("____________________________________________________________");
    }

    /** Represents an error caused by invalid Kotha command input. */
    private static class KothaException extends Exception {
        KothaException(String message) {
            super(message);
        }
    }
}
