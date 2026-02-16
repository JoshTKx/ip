package echo.util;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import echo.exception.EchoException;

/**
 * Tests for the InputValidator utility class.
 *
 * AI-Assisted: Claude Code suggested edge cases to test including:
 * - Null inputs
 * - Empty strings
 * - Whitespace-only strings
 * - Boundary conditions for task indices (0, -1, max, max+1)
 * - Invalid number formats (letters, decimals, special characters)
 * - Missing delimiters in various positions
 */
public class InputValidatorTest {

    @Test
    public void requireNonEmpty_validInput_success() {
        assertDoesNotThrow(() -> InputValidator.requireNonEmpty("valid text", "field"));
    }

    @Test
    public void requireNonEmpty_nullInput_throwsException() {
        EchoException exception = assertThrows(
                EchoException.class, () -> InputValidator.requireNonEmpty(null, "description"));
        assertEquals("description cannot be empty.", exception.getMessage());
    }

    @Test
    public void requireNonEmpty_emptyString_throwsException() {
        EchoException exception = assertThrows(
                EchoException.class, () -> InputValidator.requireNonEmpty("", "description"));
        assertEquals("description cannot be empty.", exception.getMessage());
    }

    @Test
    public void requireNonEmpty_whitespaceOnly_throwsException() {
        EchoException exception = assertThrows(
                EchoException.class, () -> InputValidator.requireNonEmpty("   ", "description"));
        assertEquals("description cannot be empty.", exception.getMessage());
    }

    @Test
    public void requireDelimiter_validDelimiter_success() {
        assertDoesNotThrow(() -> InputValidator.requireDelimiter("task /by date", "/by", "Missing /by"));
    }

    @Test
    public void requireDelimiter_missingDelimiter_throwsException() {
        EchoException exception = assertThrows(
                EchoException.class, () -> InputValidator.requireDelimiter("task date", "/by",
                        "Missing /by delimiter"));
        assertEquals("Missing /by delimiter", exception.getMessage());
    }

    @Test
    public void requireValidTaskIndex_validIndex_success() {
        assertDoesNotThrow(() -> InputValidator.requireValidTaskIndex(0, 5));
        assertDoesNotThrow(() -> InputValidator.requireValidTaskIndex(4, 5));
    }

    @Test
    public void requireValidTaskIndex_negativeIndex_throwsException() {
        EchoException exception = assertThrows(
                EchoException.class, () -> InputValidator.requireValidTaskIndex(-1, 5));
        assertEquals("Task number doesn't exist. Please provide a number between 1 and 5.",
                exception.getMessage());
    }

    @Test
    public void requireValidTaskIndex_indexEqualToMax_throwsException() {
        EchoException exception = assertThrows(
                EchoException.class, () -> InputValidator.requireValidTaskIndex(5, 5));
        assertEquals("Task number doesn't exist. Please provide a number between 1 and 5.",
                exception.getMessage());
    }

    @Test
    public void requireValidTaskIndex_indexGreaterThanMax_throwsException() {
        EchoException exception = assertThrows(
                EchoException.class, () -> InputValidator.requireValidTaskIndex(10, 5));
        assertEquals("Task number doesn't exist. Please provide a number between 1 and 5.",
                exception.getMessage());
    }

    @Test
    public void requirePositiveInteger_validPositiveInteger_success() {
        assertEquals(5, assertDoesNotThrow(() -> InputValidator.requirePositiveInteger("5", "number")));
        assertEquals(100, assertDoesNotThrow(() -> InputValidator.requirePositiveInteger("100", "number")));
    }

    @Test
    public void requirePositiveInteger_validIntegerWithWhitespace_success() {
        assertEquals(5, assertDoesNotThrow(() -> InputValidator.requirePositiveInteger("  5  ", "number")));
    }

    @Test
    public void requirePositiveInteger_zero_throwsException() {
        EchoException exception = assertThrows(
                EchoException.class, () -> InputValidator.requirePositiveInteger("0", "Task number"));
        assertEquals("Task number must be a positive number.", exception.getMessage());
    }

    @Test
    public void requirePositiveInteger_negativeNumber_throwsException() {
        EchoException exception = assertThrows(
                EchoException.class, () -> InputValidator.requirePositiveInteger("-5", "Task number"));
        assertEquals("Task number must be a positive number.", exception.getMessage());
    }

    @Test
    public void requirePositiveInteger_notANumber_throwsException() {
        EchoException exception = assertThrows(
                EchoException.class, () -> InputValidator.requirePositiveInteger("abc", "Task number"));
        assertEquals("Task number must be a valid number.", exception.getMessage());
    }

    @Test
    public void requirePositiveInteger_decimal_throwsException() {
        EchoException exception = assertThrows(
                EchoException.class, () -> InputValidator.requirePositiveInteger("3.14", "Task number"));
        assertEquals("Task number must be a valid number.", exception.getMessage());
    }

    @Test
    public void requireMinParts_validParts_success() {
        String[] parts = {"part1", "part2", "part3"};
        assertDoesNotThrow(() -> InputValidator.requireMinParts(parts, 3, "Error"));
    }

    @Test
    public void requireMinParts_tooFewParts_throwsException() {
        String[] parts = {"part1", "part2"};
        EchoException exception = assertThrows(
                EchoException.class, () -> InputValidator.requireMinParts(parts, 3, "Need 3 parts"));
        assertEquals("Need 3 parts", exception.getMessage());
    }

    @Test
    public void requireMinParts_emptyPart_throwsException() {
        String[] parts = {"part1", "", "part3"};
        EchoException exception = assertThrows(
                EchoException.class, () -> InputValidator.requireMinParts(parts, 3, "All parts required"));
        assertEquals("All parts required", exception.getMessage());
    }

    @Test
    public void requireMinParts_whitespacePart_throwsException() {
        String[] parts = {"part1", "   ", "part3"};
        EchoException exception = assertThrows(
                EchoException.class, () -> InputValidator.requireMinParts(parts, 3, "All parts required"));
        assertEquals("All parts required", exception.getMessage());
    }
}
