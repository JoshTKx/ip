package echo.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import echo.util.DateTimeParser;

/**
 * Represents a task that occurs during a specific time period.
 * Supports parsing start and end times in multiple formats: datetime (yyyy-MM-dd HHmm),
 * date only (yyyy-MM-dd), or plain text strings.
 */
public class Event extends Task {
    private static final String RECURRENCE_DAILY = "daily";
    private static final String RECURRENCE_WEEKLY = "weekly";
    private static final String RECURRENCE_MONTHLY = "monthly";
    protected String from;
    protected String to;
    protected String recurrence; // "daily", "weekly", "monthly", or null
    private final LocalDateTime startDateTime;
    private final LocalDateTime endDateTime;
    private final LocalDate startDate;
    private final LocalDate endDate;
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
     * Constructs an Event task with recurrence.
     *
     * @param description The description of the event task.
     * @param from        The start date/time as a string.
     * @param to          The end date/time as a string.
     * @param recurrence  The recurrence pattern ("daily", "weekly", "monthly").
     */
    public Event(String description, String from, String to, String recurrence) {
        this(description, from, to);
        this.recurrence = recurrence;
    }

    /**
     * Generates the next N occurrences of this recurring event.
     * Only works if the event has a valid datetime and recurrence pattern.
     *
     * @param count Number of future occurrences to generate.
     * @return List of future start times, or empty list if not applicable.
     */
    public List<LocalDateTime> getNextOccurrences(int count) {
        if (startDateTime == null || recurrence == null) {
            return new ArrayList<>();
        }

        List<LocalDateTime> occurrences = new ArrayList<>();
        LocalDateTime next = startDateTime;

        for (int i = 0; i < count; i++) {
            switch (recurrence) {
            case RECURRENCE_DAILY:
                next = next.plusDays(1);
                break;
            case RECURRENCE_WEEKLY:
                next = next.plusWeeks(1);
                break;
            case RECURRENCE_MONTHLY:
                next = next.plusMonths(1);
                break;
            default:
                return occurrences;
            }
            occurrences.add(next);
        }
        return occurrences;
    }

    /**
     * Returns the file format representation of this event task.
     * Format: "E | STATUS | DESCRIPTION | START_TIME | END_TIME"
     *
     * @return A string representation suitable for file storage.
     */
    @Override
    public String toFileFormat() {
        return "E " + super.toFileFormat() + " | " + from + " | " + to
                + " | " + (recurrence != null ? recurrence : "none");
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

        String result = "[E]" + super.toString() + " (from: " + fromString + " to: " + toString + ")";

        if (recurrence != null) {
            result += " (repeats " + recurrence + ")";
            List<LocalDateTime> nextOccurrences = getNextOccurrences(3);
            if (!nextOccurrences.isEmpty()) {
                String dates = nextOccurrences.stream()
                        .map(dt -> dt.format(java.time.format.DateTimeFormatter.ofPattern("MMM d")))
                        .collect(Collectors.joining(", "));
                result += "\n   Next: " + dates;
            }
        }

        return result;
    }
}
