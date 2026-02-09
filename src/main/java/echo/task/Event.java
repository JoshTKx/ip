package echo.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents a task that occurs during a specific time period.
 * Supports parsing start and end times in multiple formats: datetime (yyyy-MM-dd HHmm),
 * date only (yyyy-MM-dd), or plain text strings.
 */
public class Event extends Task {
    protected String from;
    protected String to;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private LocalDate startDate;
    private LocalDate endDate;

    /**
     * Constructs an Event task with the specified description, start time, and end time.
     * Attempts to parse each time string as datetime, then date, falling back to plain text.
     *
     * @param description The description of the event task.
     * @param from        The start date/time as a string (supports yyyy-MM-dd HHmm, yyyy-MM-dd, or plain text).
     * @param to          The end date/time as a string (supports yyyy-MM-dd HHmm, yyyy-MM-dd, or plain text).
     */
    public Event(String description, String from, String to) {
        super(description);
        assert from != null : "Event 'from' parameter cannot be null";
        assert to != null : "Event 'to' parameter cannot be null";
        this.from = from;
        this.to = to;

        try {
            this.startDateTime = LocalDateTime.parse(from, DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
            this.startDate = null;
        } catch (DateTimeParseException e) {
            try {
                this.startDate = LocalDate.parse(from);
                this.startDateTime = null;
            } catch (DateTimeParseException e2) {
                this.startDateTime = null;
                this.startDate = null;
            }
        }

        try {
            this.endDateTime = LocalDateTime.parse(to, DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
            this.endDate = null;
        } catch (DateTimeParseException e) {
            try {
                this.endDate = LocalDate.parse(to);
                this.endDateTime = null;
            } catch (DateTimeParseException e2) {
                this.endDateTime = null;
                this.endDate = null;
            }
        }
    }

    /**
     * Returns the file format representation of this event task.
     * Format: "E | STATUS | DESCRIPTION | START_TIME | END_TIME"
     *
     * @return A string representation suitable for file storage.
     */
    @Override
    public String toFileFormat() {
        return "E " + super.toFileFormat() + " | " + from + " | " + to;
    }

    /**
     * Returns the string representation of this event task for display.
     * Formats parsed dates as "MMM d yyyy" or "MMM d yyyy, h:mma" for datetimes.
     * Falls back to the original strings if parsing failed.
     *
     * @return A formatted string showing the task type, status, description, start time, and end time.
     */
    @Override
    public String toString() {
        String fromString;
        String toString;

        // Format "from"
        if (startDateTime != null) {
            fromString = startDateTime.format(DateTimeFormatter.ofPattern("MMM d yyyy, h:mma"));
        } else if (startDate != null) {
            fromString = startDate.format(DateTimeFormatter.ofPattern("MMM d yyyy"));
        } else {
            fromString = from;
        }

        // Format "to"
        if (endDateTime != null) {
            toString = endDateTime.format(DateTimeFormatter.ofPattern("MMM d yyyy, h:mma"));
        } else if (endDate != null) {
            toString = endDate.format(DateTimeFormatter.ofPattern("MMM d yyyy"));
        } else {
            toString = to;
        }

        return "[E]" + super.toString() + " (from: " + fromString + " to: " + toString + ")";
    }
}
