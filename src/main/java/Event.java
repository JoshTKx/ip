import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Event extends Task {
    protected String from;
    protected String to;
    protected LocalDateTime startDateTime;
    protected LocalDateTime endDateTime;
    protected LocalDate startDate;
    protected LocalDate endDate;

    public Event(String description, String from, String to) {
        super(description);
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

    @Override
    public String toFileFormat() {
        return "E " + super.toFileFormat() + " | "  + from + " | " + to ;
    }

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