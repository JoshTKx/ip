package echo.util;

import echo.exception.EchoException;

/**
 * Utility class for validating user inputs across the application.
 *
 * AI-Assisted: Used Claude Code to identify common validation patterns across Parser and Echo classes,
 * suggesting extraction into a centralized utility for better maintainability and consistency.
 * Claude identified edge cases like null inputs, empty strings, whitespace-only strings, and
 * boundary conditions that should be validated uniformly.
 */
public class InputValidator {

    /**
     * Validates that a string is not null, not empty, and not just whitespace.
     *
     * @param input The string to validate.
     * @param fieldName The name of the field for error messages (e.g., "description", "task number").
     * @throws EchoException If the input is null, empty, or contains only whitespace.
     */
    public static void requireNonEmpty(String input, String fieldName) throws EchoException {
        if (input == null || input.trim().isEmpty()) {
            throw new EchoException(fieldName + " cannot be empty.");
        }
    }

    /**
     * Validates that a string contains a required delimiter.
     *
     * @param input The string to check.
     * @param delimiter The required delimiter.
     * @param errorMessage The error message to throw if delimiter is missing.
     * @throws EchoException If the delimiter is not found in the input.
     */
    public static void requireDelimiter(String input, String delimiter, String errorMessage)
            throws EchoException {
        if (!input.contains(delimiter)) {
            throw new EchoException(errorMessage);
        }
    }

    /**
     * Validates that a task number is within valid bounds.
     *
     * AI-Assisted: Claude identified that task number validation was scattered across multiple methods
     * in Echo.java (lines 147-149, 171-173, 263-265), suggesting consolidation into a single
     * validation method to ensure consistent error messages and reduce code duplication.
     *
     * @param taskNum The task number (0-indexed) to validate.
     * @param maxIndex The maximum valid index (exclusive).
     * @throws EchoException If the task number is out of bounds.
     */
    public static void requireValidTaskIndex(int taskNum, int maxIndex) throws EchoException {
        if (taskNum < 0 || taskNum >= maxIndex) {
            throw new EchoException("Task number doesn't exist. Please provide a number between 1 and "
                    + maxIndex + ".");
        }
    }

    /**
     * Validates that an integer string is actually a valid positive integer.
     *
     * @param numberString The string to parse.
     * @param fieldName The name of the field for error messages.
     * @return The parsed integer.
     * @throws EchoException If the string is not a valid positive integer.
     */
    public static int requirePositiveInteger(String numberString, String fieldName) throws EchoException {
        try {
            int number = Integer.parseInt(numberString.trim());
            if (number <= 0) {
                throw new EchoException(fieldName + " must be a positive number.");
            }
            return number;
        } catch (NumberFormatException e) {
            throw new EchoException(fieldName + " must be a valid number.");
        }
    }

    /**
     * Validates that a split result has the expected number of parts.
     *
     * @param parts The array of parts from a split operation.
     * @param expectedMinParts The minimum expected number of parts.
     * @param errorMessage The error message if validation fails.
     * @throws EchoException If the parts array is too short or contains empty elements.
     */
    public static void requireMinParts(String[] parts, int expectedMinParts, String errorMessage)
            throws EchoException {
        if (parts.length < expectedMinParts) {
            throw new EchoException(errorMessage);
        }
        for (int i = 0; i < expectedMinParts; i++) {
            if (parts[i].trim().isEmpty()) {
                throw new EchoException(errorMessage);
            }
        }
    }
}
