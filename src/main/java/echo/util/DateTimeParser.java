package echo.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Utility class for parsing and formatting date and time strings.
 * Provides methods to parse dates in multiple formats and format them for display.
 */
public class DateTimeParser {
    private static final DateTimeFormatter DATETIME_INPUT_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    private static final DateTimeFormatter DATETIME_DISPLAY_FORMAT =
            DateTimeFormatter.ofPattern("MMM d yyyy, h:mma");
    private static final DateTimeFormatter DATE_DISPLAY_FORMAT =
            DateTimeFormatter.ofPattern("MMM d yyyy");

    /**
     * Attempts to parse a string as LocalDateTime in yyyy-MM-dd HHmm format.
     *
     * @param dateTimeString The string to parse.
     * @return LocalDateTime if successful, null otherwise.
     */
    public static LocalDateTime parseDateTime(String dateTimeString) {
        try {
            return LocalDateTime.parse(dateTimeString, DATETIME_INPUT_FORMAT);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    /**
     * Attempts to parse a string as LocalDate in yyyy-MM-dd format.
     *
     * @param dateString The string to parse.
     * @return LocalDate if successful, null otherwise.
     */
    public static LocalDate parseDate(String dateString) {
        try {
            return LocalDate.parse(dateString);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    /**
     * Formats a LocalDateTime for display.
     *
     * @param dateTime The LocalDateTime to format.
     * @return Formatted string in "MMM d yyyy, h:mma" format.
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(DATETIME_DISPLAY_FORMAT);
    }

    /**
     * Formats a LocalDate for display.
     *
     * @param date The LocalDate to format.
     * @return Formatted string in "MMM d yyyy" format.
     */
    public static String formatDate(LocalDate date) {
        return date.format(DATE_DISPLAY_FORMAT);
    }
}
