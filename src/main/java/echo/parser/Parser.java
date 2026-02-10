package echo.parser;

import echo.exception.EchoException;

/**
 * Handles parsing of user input commands.
 * Provides static methods to extract and validate different parts of user commands.
 */
public class Parser {

    private static final String DELIMITER_BY = " /by ";
    private static final String DELIMITER_FROM = " /from ";
    private static final String DELIMITER_TO = " /to ";
    private static final int MIN_PARTS_DEADLINE = 2;
    private static final int MIN_PARTS_EVENT = 3;
    private static final String DELIMITER_REPEAT = " /repeat ";

    /**
     * Extracts the command word from the user input.
     *
     * @param input The full user input string.
     * @return The first word of the input, representing the command.
     */
    public static String getCommand(String input) {
        assert input != null : "Input cannot be null";
        return input.split("\\s+")[0];
    }

    /**
     * Extracts the description/arguments portion of the user input after the command word.
     *
     * @param input   The full user input string.
     * @param command The command word to remove from the input.
     * @return The remaining text after the command word, or empty string if none exists.
     */
    public static String getDescription(String input, String command) {
        assert input != null : "Input cannot be null";
        assert command != null : "Command cannot be null";
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
        assert input != null : "Input should not be null";
        String[] parts = input.split("\\s+");
        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            throw new EchoException("Please provide a task number.");
        }
        try {
            int taskNum = Integer.parseInt(parts[1].trim());
            assert taskNum > 0 : "Task number from user should be positive";
            return taskNum;
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
        if (!description.contains(DELIMITER_BY)) {
            throw new EchoException("Deadlines need a date! Use: deadline <task> /by <date>");
        }
        String[] parts = description.split(DELIMITER_BY, 2);
        if (parts.length < MIN_PARTS_DEADLINE || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
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
        if (!description.contains(DELIMITER_FROM) || !description.contains(DELIMITER_TO)) {
            throw new EchoException("Events need start and end times! "
                    + "Use: event <task> /from <time> /to <time>");
        }
        String[] parts = description.split(DELIMITER_FROM + "|" + DELIMITER_TO);
        if (parts.length < MIN_PARTS_EVENT || parts[0].trim().isEmpty()
                || parts[1].trim().isEmpty() || parts[2].trim().isEmpty()) {
            throw new EchoException("Please provide event description, start time, and end time.");
        }
        return new String[]{parts[0].trim(), parts[1].trim(), parts[2].trim()};
    }

    /**
     * Extracts the recurrence pattern from the event description.
     * Expected format: "... /repeat daily|weekly|monthly"
     *
     * @param description The description that may contain /repeat.
     * @return The recurrence pattern ("daily", "weekly", "monthly"), or null if not present.
     */
    public static String extractRecurrence(String description) {
        if (description.contains(DELIMITER_REPEAT)) {
            String[] parts = description.split(DELIMITER_REPEAT, 2);
            if (parts.length > 1) {
                return parts[1].trim();
            }
        }
        return null;
    }

    /**
     * Removes the /repeat portion from the description.
     *
     * @param description The description that may contain /repeat.
     * @return The description without the /repeat portion.
     */
    public static String removeRecurrence(String description) {
        if (description.contains(DELIMITER_REPEAT)) {
            return description.split(DELIMITER_REPEAT, 2)[0].trim();
        }
        return description;
    }
}
