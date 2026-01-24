import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


public class Deadline extends Task {
    protected String by;
    protected LocalDate date;

    public Deadline(String description, String by) {
        super(description);
        this.by = by;

        try {
            this.date = LocalDate.parse(by);
        } catch (DateTimeParseException e) {
            this.date = null; // If parsing fails, keep as string only
        }
    }

    @Override
    public String toFileFormat() {
        return "D " + super.toFileFormat() + " | " + by;
    }

    @Override
    public String toString() {
        String dateString;
        if (date != null) {
            dateString = date.format(DateTimeFormatter.ofPattern("MMM d yyyy"));
        } else {
            dateString = by;
        }
        return "[D]" + super.toString() + " (by: " + dateString + ")";
    }

}