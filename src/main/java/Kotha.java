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
            }
            else {
                addToList(command);
                System.out.println("____________________________________________________________");
                System.out.println("added: " + command);
                System.out.println("____________________________________________________________");
            }
        }
    }

    // Array containing the list of tasks
    private static String[] listOfTask = new String[100];
    // Points to the first free slot in listOfTask (one past the last element put inside)
    private static byte listOfTaskPointer = 0;
    private static void addToList(String command) {
        listOfTask[listOfTaskPointer] = command;
        listOfTaskPointer++;
    }
    private static void stringList() {
        System.out.println("List");
        System.out.println("____________________________________________________________");
        for (byte pointer = 0; pointer < listOfTaskPointer; pointer++) {
            byte number = (byte) (pointer + 1);
            System.out.printf("%d. %s%n", number, listOfTask[pointer]);
        }
        System.out.println("____________________________________________________________");

    }

}
