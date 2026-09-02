package kotha;

/** Identifies the type of command entered by the user. */
public class Parser {
    /** The command types understood by Kotha. */
    public enum CommandType {
        BYE, LIST, FIND, MARK, UNMARK, TODO, DEADLINE, EVENT, DELETE, UNKNOWN
    }

    /**
     * Determines the command type from the first word of the input.
     *
     * @param input the String input from the user.
     */
    public CommandType parse(String input) {
        String command = input.trim();
        if (command.equals("bye")) {
            return CommandType.BYE;
        }
        if (command.equals("list")) {
            return CommandType.LIST;
        }
        if (hasCommandWord(command, "find")) {
            return CommandType.FIND;
        }
        if (hasCommandWord(command, "mark")) {
            return CommandType.MARK;
        }
        if (hasCommandWord(command, "unmark")) {
            return CommandType.UNMARK;
        }
        if (hasCommandWord(command, "todo")) {
            return CommandType.TODO;
        }
        if (hasCommandWord(command, "deadline")) {
            return CommandType.DEADLINE;
        }
        if (hasCommandWord(command, "event")) {
            return CommandType.EVENT;
        }
        if (hasCommandWord(command, "delete")) {
            return CommandType.DELETE;
        } else {
            return CommandType.UNKNOWN;
        }
    }

    private boolean hasCommandWord(String input, String commandWord) {
        return input.equals(commandWord) || input.startsWith(commandWord + " ");
    }
}
