package echo.parser;

import echo.exception.EchoException;

public class Parser {

    public static String getCommand(String input) {
        return input.split(" ")[0];
    }

    public static String getDescription(String input, String command) {
        if (input.length() <= command.length()) {
            return "";
        }
        return input.substring(command.length()).trim();
    }

    public static int getTaskNumber(String input) throws EchoException {
        String[] parts = input.split(" ");
        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            throw new EchoException("Please provide a task number.");
        }
        try {
            return Integer.parseInt(parts[1].trim());
        } catch (NumberFormatException e) {
            throw new EchoException("Please provide a valid task number.");
        }
    }

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

    public static String[] parseEvent(String description) throws EchoException {
        if (!description.contains("/from") || !description.contains("/to")) {
            throw new EchoException("Events need start and end times! Use: event <task> /from <time> /to <time>");
        }
        String[] parts = description.split(" /from | /to ");
        if (parts.length < 3 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty() || parts[2].trim().isEmpty()) {
            throw new EchoException("Please provide event description, start time, and end time.");
        }
        return new String[]{parts[0].trim(), parts[1].trim(), parts[2].trim()};
    }
}