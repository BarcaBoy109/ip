package main.java;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * A chatbot that echoes commands until the user exits.
 */
public class Kotha {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("d/M/uuuu");
    private static final List<DateTimeFormatter> DATE_TIME_FORMATS = List.of(
            DateTimeFormatter.ofPattern("d/M/uuuu HHmm"),
            DateTimeFormatter.ofPattern("d/M/uu HHmm"));
    private static final DateTimeFormatter DAY_MONTH_FORMAT =
            new java.time.format.DateTimeFormatterBuilder()
                    .appendPattern("d/M")
                    .parseDefaulting(java.time.temporal.ChronoField.YEAR, Year.now().getValue())
                    .toFormatter();
    private static final DateTimeFormatter TIME_FORMAT =
            DateTimeFormatter.ofPattern("HHmm");
    private static ArrayList<Task> listOfTasks = new ArrayList<>();
    private static final Storage storage = new Storage();

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

        listOfTasks = storage.loadTasks();
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

    /**
     * Adds the task to the list ot be saved
     *
     * @param task the task to be saved
     */
    private static void addToList(Task task) {
        listOfTasks.add(task);
        task.printAddText();
        printNumOfTasks();
        storage.saveTasks(listOfTasks);
    }

    /**
     * Prints the current number of tasks in the list
     */
    private static void printNumOfTasks() {
        if (listOfTasks.size() == 1) {
            System.out.println("Hiee you now you have " + listOfTasks.size() + " task in your list.");
        } else {
            System.out.println("Hiee you now you have " + listOfTasks.size() + " tasks in your list.");
        }
        System.out.println("____________________________________________________________");
    }

    /**
     * Prints each task in the list
     */
    private static void stringList() {
        System.out.println("____________________________________________________________");
        System.out.println("Here are the tasks in your list:");
        for (int pointer = 0; pointer < listOfTasks.size(); pointer++) {
            int number = pointer + 1;
            System.out.printf("%d.%s%n", number, listOfTasks.get(pointer).toString());
        }
        System.out.println("____________________________________________________________");

    }

    /** Adds a todo task to the list of tasks
     *
     * @param command the string command sent to the chatbot
     */
    private static void addTodo(String command) throws KothaException {
        String description = command.substring("todo".length()).trim();
        if (description.isEmpty()) {
            throw new KothaException("Please add a description after 'todo'.");
        }
        addToList(new ToDo(description));
    }

    /** Adds a deadline task after checking its description and by date.
     *
     * @param command the string command sent to the chatbot
     */
    private static void addDeadline(String command) throws KothaException {
        int byIndex = command.indexOf(" /by ");
        if (byIndex <= "deadline".length()) {
            throw new KothaException("A deadline needs a description and a '/by' date.");
        }

        String description = command.substring("deadline".length(), byIndex).trim();
        String byText = command.substring(byIndex + " /by ".length()).trim();
        if (description.isEmpty() || byText.isEmpty()) {
            throw new KothaException("A deadline needs a description and a '/by' date.");
        }
        LocalDateTime by = parseDateTime(byText);
        addToList(new Deadline(description, by));
    }

    /** Adds an event after checking its description, start time, and end time.
     *
     * @param command the string command sent to the chatbot
     */
    private static void addEvent(String command) throws KothaException {
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
        LocalDateTime from = parseDateTime(fromText);
        LocalDateTime to = parseDateTime(toText);
        addToList(new Event(description, from, to));
    }

    /**
     * Converts a supported user date and time into a LocalDateTime.
     * Dates without a year use the current year.
     * Dates without a time uses 2359
     *
     * @param dateTimeText the date and time supplied in a command as a String object.
     * @return the corresponding date and time as a LocalDateTime object.
     * @throws KothaException if the input is not a supported date and time.
     */
    private static LocalDateTime parseDateTime(String dateTimeText) throws KothaException {
        String[] parts = dateTimeText.split("\\s+");

        try {
            // If HHmm is not mentioned, parse LocalDate + Add default HHmm 2359
            if (parts.length == 1) {
                LocalDate date = parseDate(parts[0]);
                return LocalDateTime.of(date, LocalTime.of(23, 59));
            }

            if (parts.length == 2) {
                LocalDate date = parseDate(parts[0]);
                LocalTime time = LocalTime.parse(parts[1], TIME_FORMAT);
                return LocalDateTime.of(date, time);
            }
        } catch (DateTimeParseException ignored) {
            // Invalid input
        }

        throw new KothaException(
                "Use d/M, d/M/yy, or d/M/yyyy, optionally followed by HHmm."
        );
    }

    /**
     * Converts a supported user date into a LocalDate
     *
     * @param dateText the String to be converted to LocalDate
     * @return LocalDate object of the dateText
     */
    private static LocalDate parseDate(String dateText) {
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

    /**
     * Deletes the task at a given 1-based index
     *
     * @param command the string command sent to the chatbot
     * @throws KothaException
     */
    private static void deleteTask(String command) throws KothaException {
        String taskNumberText = command.substring("delete".length()).trim();
        if (!taskNumberText.matches("\\d+")) {
            throw new KothaException("Cannot delete task number " + taskNumberText + "\nPlease enter an integer");
        }
        int taskNumber = Integer.parseInt(taskNumberText);
        int taskIndex = taskNumber - 1;
        if (taskIndex < 0 || taskIndex >= listOfTasks.size()) {
            throw new KothaException("Dont try to delete tasks outside of your range! I am watching you ⊙▃⊙");
        }
        listOfTasks.get(taskIndex).printRemoveText();
        listOfTasks.remove(taskIndex);
        printNumOfTasks();
        storage.saveTasks(listOfTasks);
    }

    /**
     * Marks or unmarks the task at the given 1-based index
     *
     * @param command the string command sent to the chatbot
     * @param isDone set to true for mark, set to false for unmark
     */
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
        if (taskIndex < 0 || taskIndex >= listOfTasks.size()) {
            throw new KothaException("That task number does not exist.");
        }

        if (isDone) {
            listOfTasks.get(taskIndex).markAsDone();
        } else {
            listOfTasks.get(taskIndex).markAsNotDone();
        }
        storage.saveTasks(listOfTasks);
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
