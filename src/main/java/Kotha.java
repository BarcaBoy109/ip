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
            String command = scanner.nextLine();
            if (command.equals("bye")) {
                System.out.println("____________________________________________________________");
                System.out.println("Bye. Hope to see you again soon!");
                System.out.println("____________________________________________________________");
                break;
            } else if (command.equals("list")) {
                stringList();
            } else if (command.matches("mark (\\d+)")) {
                int taskId = Integer.parseInt(command.substring(command.indexOf(" ") + 1)) - 1;
                Task t = listOfTask[taskId];
                t.markAsDone();
            } else if (command.matches("unmark (\\d+)")) {
                int taskId = Integer.parseInt(command.substring(command.indexOf(" ") + 1)) - 1;
                Task t = listOfTask[taskId];
                t.markAsNotDone();
            } else {
                if (command.matches("todo .+")) {
                    String description = command.substring("todo ".length());
                    Task t = new ToDo(description);
                    addToList(t);
                } else if (command.matches("deadline .+")) {
                    int byIndex = command.indexOf(" /by ");
                    String description = command.substring("deadline ".length(), byIndex);
                    String by = command.substring(byIndex + " /by ".length());
                    Task t = new Deadline(description, by);
                    addToList(t);
                } else if (command.matches("event .+")) {
                    int fromIndex = command.indexOf(" /from ");
                    int toIndex = command.indexOf(" /to ");
                    String description = command.substring("event ".length(), fromIndex);
                    String from = command.substring(fromIndex + " /from ".length(), toIndex);
                    String to = command.substring(toIndex + " /to ".length());
                    Task t = new Event(description, from, to);
                    addToList(t);
                } else {
                    Task t = new Task(command);
                    addToList(t);
                }

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


}
