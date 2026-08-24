package main.java;

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
                } else {
                    throw new KothaException("I don't recognise that command.");
                }
            } catch (KothaException e) {
                printError(e.getMessage());
            }
        }
    }

    // Array containing the list of tasks
    private static Task[] listOfTask = new Task[100];
    // Points to the first free slot in listOfTask (one past the last element put inside)
    private static byte listOfTaskPointer = 0;

    private static void addToList(Task task) {
        listOfTask[listOfTaskPointer] = task;
        task.printAddText();
        listOfTaskPointer++;
        if (listOfTaskPointer == 1) {
            System.out.println("Hiee you now you have " + listOfTaskPointer + " task in your list.");
        } else {
            System.out.println("Hiee you now you have " + listOfTaskPointer + " tasks in your list.");
        }
        System.out.println("____________________________________________________________");
    }
    private static void stringList() {
        System.out.println("____________________________________________________________");
        System.out.println("Here are the tasks in your list:");
        for (byte pointer = 0; pointer < listOfTaskPointer; pointer++) {
            byte number = (byte) (pointer + 1);
            System.out.printf("%d.%s%n", number, listOfTask[pointer].toString());
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
        if (taskIndex < 0 || taskIndex >= listOfTaskPointer) {
            throw new KothaException("That task number does not exist.");
        }

        if (isDone) {
            listOfTask[taskIndex].markAsDone();
        } else {
            listOfTask[taskIndex].markAsNotDone();
        }
    }

    /** Displays a consistent error message for invalid commands. */
    private static void printError(String message) {
        System.out.println("____________________________________________________________");
        System.out.println("Oops! " + message);
        System.out.println("____________________________________________________________");
    }

    /** Represents an error caused by invalid Kotha command input. */
    private static class KothaException extends Exception {
        KothaException(String message) {
            super(message);
        }
    }
}
