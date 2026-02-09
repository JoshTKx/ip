package echo.task;

import java.time.LocalDate;
import java.time.LocalDateTime;

import echo.util.DateTimeParser;

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
        this.from = from;
        this.to = to;

        this.startDateTime = DateTimeParser.parseDateTime(from);
        if (this.startDateTime == null) {
            this.startDate = DateTimeParser.parseDate(from);
        } else {
            this.startDate = null;
        }

        this.endDateTime = DateTimeParser.parseDateTime(to);
        if (this.endDateTime == null) {
            this.endDate = DateTimeParser.parseDate(to);
        } else {
            this.endDate = null;
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

        if (startDateTime != null) {
            fromString = DateTimeParser.formatDateTime(startDateTime);
        } else if (startDate != null) {
            fromString = DateTimeParser.formatDate(startDate);
        } else {
            fromString = from;
        }

        if (endDateTime != null) {
            toString = DateTimeParser.formatDateTime(endDateTime);
        } else if (endDate != null) {
            toString = DateTimeParser.formatDate(endDate);
        } else {
            toString = to;
        }

        return "[E]" + super.toString() + " (from: " + fromString + " to: " + toString + ")";
    }
}
