package echo.task;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class EventTest {

    @Test
    public void constructor_validDescription_success() {
        Event event = new Event("meeting", "Mon 2pm", "4pm");
        assertEquals("meeting", event.getDescription());
        assertEquals(" ", event.getStatusIcon());
    }

    @Test
    public void constructor_dateOnlyFormat_parsesDates() {
        Event event = new Event("conference", "2019-12-10", "2019-12-12");
        // Test behavior: formatted dates should appear in output
        assertEquals("[E][ ] conference (from: Dec 10 2019 to: Dec 12 2019)", event.toString());
    }

    @Test
    public void constructor_dateTimeFormat_parsesDateTimes() {
        Event event = new Event("workshop", "2019-12-05 1400", "2019-12-05 1600");
        // Test behavior: formatted datetimes should appear in output
        assertEquals("[E][ ] workshop (from: Dec 5 2019, 2:00PM to: Dec 5 2019, 4:00PM)", event.toString());
    }

    @Test
    public void constructor_stringFormat_noParsing() {
        Event event = new Event("party", "Saturday 8pm", "Sunday 2am");
        // Test behavior: original strings should appear in output
        assertEquals("[E][ ] party (from: Saturday 8pm to: Sunday 2am)", event.toString());
    }

    @Test
    public void constructor_mixedFormat_parsesBoth() {
        Event event = new Event("trip", "2019-12-10", "evening");
        // Test behavior: mixed formatted/unformatted output
        assertEquals("[E][ ] trip (from: Dec 10 2019 to: evening)", event.toString());
    }

    @Test
    public void markDone_notDoneTask_marksAsDone() {
        Event event = new Event("meeting", "Mon 2pm", "4pm");
        event.markDone();
        assertEquals("X", event.getStatusIcon());
    }

    @Test
    public void markNotDone_doneTask_marksAsNotDone() {
        Event event = new Event("meeting", "Mon 2pm", "4pm");
        event.markDone();
        event.markNotDone();
        assertEquals(" ", event.getStatusIcon());
    }

    @Test
    public void toString_stringFormat_correctFormat() {
        Event event = new Event("meeting", "Mon 2pm", "4pm");
        assertEquals("[E][ ] meeting (from: Mon 2pm to: 4pm)", event.toString());
    }

    @Test
    public void toString_dateFormat_formattedDates() {
        Event event = new Event("conference", "2019-12-10", "2019-12-12");
        assertEquals("[E][ ] conference (from: Dec 10 2019 to: Dec 12 2019)", event.toString());
    }

    @Test
    public void toString_dateTimeFormat_formattedDateTimes() {
        Event event = new Event("workshop", "2019-12-05 1400", "2019-12-05 1600");
        assertEquals("[E][ ] workshop (from: Dec 5 2019, 2:00PM to: Dec 5 2019, 4:00PM)", event.toString());
    }

    @Test
    public void toString_doneTask_correctFormat() {
        Event event = new Event("meeting", "Mon 2pm", "4pm");
        event.markDone();
        assertEquals("[E][X] meeting (from: Mon 2pm to: 4pm)", event.toString());
    }

    @Test
    public void toFileFormat_notDone_correctFormat() {
        Event event = new Event("meeting", "Mon 2pm", "4pm");
        assertEquals("E | 0 | meeting | Mon 2pm | 4pm", event.toFileFormat());
    }

    @Test
    public void toFileFormat_done_correctFormat() {
        Event event = new Event("meeting", "Mon 2pm", "4pm");
        event.markDone();
        assertEquals("E | 1 | meeting | Mon 2pm | 4pm", event.toFileFormat());
    }

    @Test
    public void toFileFormat_dateFormat_preservesOriginalStrings() {
        Event event = new Event("conference", "2019-12-10", "2019-12-12");
        assertEquals("E | 0 | conference | 2019-12-10 | 2019-12-12", event.toFileFormat());
    }
}
