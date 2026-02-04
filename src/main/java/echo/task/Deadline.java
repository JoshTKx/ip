package echo.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents a task with a deadline.
 * Supports parsing dates in multiple formats: datetime (yyyy-MM-dd HHmm),
 * date only (yyyy-MM-dd), or plain text strings.
 */
public class Deadline extends Task {
    protected String by;
    private LocalDateTime dateTime;
    private LocalDate date;

    /**
     * Constructs a Deadline task with the specified description and due date.
     * Attempts to parse the date string as datetime, then date, falling back to plain text.
     *
     * @param description The description of the deadline task.
     * @param by          The due date/time as a string (supports yyyy-MM-dd HHmm, yyyy-MM-dd, or plain text).
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;

        try {
            this.dateTime = LocalDateTime.parse(by, DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
            this.date = null;
            return;
        } catch (DateTimeParseException e) {
            // Not a datetime, try date
        }

        // Try parsing as date only (yyyy-MM-dd)
        try {
            this.date = LocalDate.parse(by);
            this.dateTime = null;
        } catch (DateTimeParseException e) {
            this.date = null;
            this.dateTime = null;
        }
    }

    /**
     * Returns the file format representation of this deadline task.
     * Format: "D | STATUS | DESCRIPTION | DUE_DATE"
     *
     * @return A string representation suitable for file storage.
     */
    @Override
    public String toFileFormat() {
        return "D " + super.toFileFormat() + " | " + by;
    }

    /**
     * Returns the string representation of this deadline task for display.
     * Formats parsed dates as "MMM d yyyy" or "MMM d yyyy, h:mma" for datetimes.
     * Falls back to the original string if parsing failed.
     *
     * @return A formatted string showing the task type, status, description, and due date.
     */
    @Override
    public String toString() {
        String dateString;
        if (dateTime != null) {
            // Format with time: Dec 2 2019, 6:00PM
            dateString = dateTime.format(DateTimeFormatter.ofPattern("MMM d yyyy, h:mma"));
        } else if (date != null) {
            // Format date only: Dec 2 2019
            dateString = date.format(DateTimeFormatter.ofPattern("MMM d yyyy"));
        } else {
            // Keep original string
            dateString = by;
        }
        return "[D]" + super.toString() + " (by: " + dateString + ")";
    }
}
