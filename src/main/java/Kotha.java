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
            }

            System.out.println(command);
            System.out.println("____________________________________________________________");
        }
    }
}
