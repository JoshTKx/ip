package echo.parser;

import echo.exception.EchoException;


/**
 * Handles parsing of user input commands.
 * Provides static methods to extract and validate different parts of user commands.
 */
public class Parser {

    /**
     * Extracts the command word from the user input.
     *
     * @param input The full user input string.
     * @return The first word of the input, representing the command.
     */
    public static String getCommand(String input) {
        return input.split("\\s+")[0];
    }

    /**
     * Extracts the description/arguments portion of the user input after the command word.
     *
     * @param input The full user input string.
     * @param command The command word to remove from the input.
     * @return The remaining text after the command word, or empty string if none exists.
     */
    public static String getDescription(String input, String command) {
        if (input.length() <= command.length()) {
            return "";
        }
        return input.substring(command.length()).trim();
    }

    /**
     * Parses and extracts the task number from user input for mark/unmark/delete commands.
     *
     * @param input The full user input string containing a task number.
     * @return The task number as an integer.
     * @throws EchoException If the task number is missing or not a valid integer.
     */
    public static int getTaskNumber(String input) throws EchoException {
        String[] parts = input.split("\\s+");
        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            throw new EchoException("Please provide a task number.");
        }
        try {
            return Integer.parseInt(parts[1].trim());
        } catch (NumberFormatException e) {
            throw new EchoException("Please provide a valid task number.");
        }
    }


    /**
     * Parses a deadline command to extract the task description and due date.
     * Expected format: "description /by date"
     *
     * @param description The description portion of the deadline command.
     * @return A string array with [0] = task description, [1] = due date.
     * @throws EchoException If the /by keyword is missing or if description or date is empty.
     */
    public static String[] parseDeadline(String description) throws EchoException {
        if (!description.contains("/by")) {
            throw new EchoException("Deadlines need a date! Use: deadline <task> /by <date>");
        }
        String[] parts = description.split(" /by ", 2);
        if (parts.length < 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
            throw new EchoException("Please provide both description and deadline date.");
        }
        return new String[]{parts[0].trim(), parts[1].trim()};
    }


    /**
     * Parses an event command to extract the task description, start time, and end time.
     * Expected format: "description /from start /to end"
     *
     * @param description The description portion of the event command.
     * @return A string array with [0] = task description, [1] = start time, [2] = end time.
     * @throws EchoException If /from or /to keywords are missing, or if any component is empty.
     */
    public static String[] parseEvent(String description) throws EchoException {
        if (!description.contains("/from") || !description.contains("/to")) {
            throw new EchoException("Events need start and end times! "
                    + "Use: event <task> /from <time> /to <time>");
        }
        String[] parts = description.split(" /from | /to ");
        if (parts.length < 3 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty() || parts[2].trim().isEmpty()) {
            throw new EchoException("Please provide event description, start time, and end time.");
        }
        return new String[]{parts[0].trim(), parts[1].trim(), parts[2].trim()};
    }
}