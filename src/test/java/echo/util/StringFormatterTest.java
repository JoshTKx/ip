package echo.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Tests for the StringFormatter utility class.
 *
 * AI-Assisted: Claude Code identified the importance of testing pluralization logic,
 * suggesting test cases for singular (1 task) vs plural (0, 2, many tasks) forms
 * to ensure grammatically correct output.
 */
public class StringFormatterTest {

    @Test
    public void formatTaskAdded_singleTask_correctFormat() {
        String result = StringFormatter.formatTaskAdded("[T][ ] read book", 1);
        assertEquals("Got it. I've added this task:\n  [T][ ] read book\n"
                + "Now you have 1 task in the list.", result);
    }

    @Test
    public void formatTaskAdded_multipleTasks_correctFormat() {
        String result = StringFormatter.formatTaskAdded("[T][ ] read book", 5);
        assertEquals("Got it. I've added this task:\n  [T][ ] read book\n"
                + "Now you have 5 tasks in the list.", result);
    }

    @Test
    public void formatTaskRemoved_singleTaskRemaining_correctFormat() {
        String result = StringFormatter.formatTaskRemoved("[T][X] read book", 1);
        assertEquals("Noted. I've removed this task:\n  [T][X] read book\n"
                + "Now you have 1 task in the list.", result);
    }

    @Test
    public void formatTaskRemoved_multipleTasksRemaining_correctFormat() {
        String result = StringFormatter.formatTaskRemoved("[T][X] read book", 3);
        assertEquals("Noted. I've removed this task:\n  [T][X] read book\n"
                + "Now you have 3 tasks in the list.", result);
    }

    @Test
    public void formatTaskRemoved_noTasksRemaining_correctFormat() {
        String result = StringFormatter.formatTaskRemoved("[T][X] read book", 0);
        assertEquals("Noted. I've removed this task:\n  [T][X] read book\n"
                + "Now you have 0 tasks in the list.", result);
    }

    @Test
    public void formatTaskCount_zeroTasks_pluralForm() {
        assertEquals("0 tasks", StringFormatter.formatTaskCount(0));
    }

    @Test
    public void formatTaskCount_oneTask_singularForm() {
        assertEquals("1 task", StringFormatter.formatTaskCount(1));
    }

    @Test
    public void formatTaskCount_twoTasks_pluralForm() {
        assertEquals("2 tasks", StringFormatter.formatTaskCount(2));
    }

    @Test
    public void formatTaskCount_manyTasks_pluralForm() {
        assertEquals("100 tasks", StringFormatter.formatTaskCount(100));
    }

    @Test
    public void formatListItem_firstItem_correctFormat() {
        String result = StringFormatter.formatListItem(0, "[T][ ] task");
        assertEquals("1. [T][ ] task", result);
    }

    @Test
    public void formatListItem_secondItem_correctFormat() {
        String result = StringFormatter.formatListItem(1, "[D][X] deadline");
        assertEquals("2. [D][X] deadline", result);
    }

    @Test
    public void formatListItem_tenthItem_correctFormat() {
        String result = StringFormatter.formatListItem(9, "[E][ ] event");
        assertEquals("10. [E][ ] event", result);
    }

    @Test
    public void formatListItem_anyObject_usesToString() {
        Object obj = new Object() {
            @Override
            public String toString() {
                return "custom object";
            }
        };
        String result = StringFormatter.formatListItem(5, obj);
        assertTrue(result.startsWith("6. "));
        assertTrue(result.contains("custom object"));
    }
}
