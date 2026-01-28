package echo.task;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeadlineTest {

    @Test
    public void constructor_validDescription_success() {
        Deadline deadline = new Deadline("return book", "Sunday");
        assertEquals("return book", deadline.getDescription());
        assertEquals(" ", deadline.getStatusIcon());
    }

    @Test
    public void constructor_dateOnlyFormat_parsesDate() {
        Deadline deadline = new Deadline("homework", "2019-12-15");
        // Test behavior: formatted date should appear in output
        assertEquals("[D][ ] homework (by: Dec 15 2019)", deadline.toString());
    }

    @Test
    public void constructor_dateTimeFormat_parsesDateTime() {
        Deadline deadline = new Deadline("submit report", "2019-12-15 1800");
        // Test behavior: formatted datetime should appear in output
        assertEquals("[D][ ] submit report (by: Dec 15 2019, 6:00PM)", deadline.toString());
    }

    @Test
    public void constructor_stringFormat_noParsing() {
        Deadline deadline = new Deadline("homework", "Sunday");
        // Test behavior: original string should appear in output
        assertEquals("[D][ ] homework (by: Sunday)", deadline.toString());
    }

    @Test
    public void markDone_notDoneTask_marksAsDone() {
        Deadline deadline = new Deadline("return book", "Sunday");
        deadline.markDone();
        assertEquals("X", deadline.getStatusIcon());
    }

    @Test
    public void markNotDone_doneTask_marksAsNotDone() {
        Deadline deadline = new Deadline("return book", "Sunday");
        deadline.markDone();
        deadline.markNotDone();
        assertEquals(" ", deadline.getStatusIcon());
    }

    @Test
    public void toString_stringFormat_correctFormat() {
        Deadline deadline = new Deadline("return book", "Sunday");
        assertEquals("[D][ ] return book (by: Sunday)", deadline.toString());
    }

    @Test
    public void toString_dateFormat_formattedDate() {
        Deadline deadline = new Deadline("homework", "2019-12-15");
        assertEquals("[D][ ] homework (by: Dec 15 2019)", deadline.toString());
    }

    @Test
    public void toString_dateTimeFormat_formattedDateTime() {
        Deadline deadline = new Deadline("submit report", "2019-12-15 1800");
        assertEquals("[D][ ] submit report (by: Dec 15 2019, 6:00PM)", deadline.toString());
    }

    @Test
    public void toString_doneTask_correctFormat() {
        Deadline deadline = new Deadline("return book", "Sunday");
        deadline.markDone();
        assertEquals("[D][X] return book (by: Sunday)", deadline.toString());
    }

    @Test
    public void toFileFormat_notDone_correctFormat() {
        Deadline deadline = new Deadline("return book", "Sunday");
        assertEquals("D | 0 | return book | Sunday", deadline.toFileFormat());
    }

    @Test
    public void toFileFormat_done_correctFormat() {
        Deadline deadline = new Deadline("return book", "Sunday");
        deadline.markDone();
        assertEquals("D | 1 | return book | Sunday", deadline.toFileFormat());
    }

    @Test
    public void toFileFormat_dateFormat_preservesOriginalString() {
        Deadline deadline = new Deadline("homework", "2019-12-15");
        assertEquals("D | 0 | homework | 2019-12-15", deadline.toFileFormat());
    }
}