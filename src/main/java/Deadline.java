import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


public class Deadline extends Task {
    protected String by;
    protected LocalDateTime dateTime;
    protected LocalDate date;

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

    @Override
    public String toFileFormat() {
        return "D " + super.toFileFormat() + " | " + by;
    }

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