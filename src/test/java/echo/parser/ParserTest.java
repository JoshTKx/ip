package echo.parser;

import echo.exception.EchoException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class ParserTest {

    // ========== getCommand Tests ==========
    @Test
    public void getCommand_singleWordCommand_success() {
        assertEquals("todo", Parser.getCommand("todo"));
        assertEquals("deadline", Parser.getCommand("deadline"));
        assertEquals("event", Parser.getCommand("event"));
        assertEquals("list", Parser.getCommand("list"));
        assertEquals("bye", Parser.getCommand("bye"));
        assertEquals("clear", Parser.getCommand("clear"));
        assertEquals("bye", Parser.getCommand("bye"));

    }

    @Test
    public void getCommand_commandWithDescription_success() {
        assertEquals("todo", Parser.getCommand("todo buy milk"));
        assertEquals("deadline", Parser.getCommand("deadline homework /by Sunday"));
        assertEquals("event", Parser.getCommand("event homework /from Sunday /to Monday"));
        assertEquals("mark", Parser.getCommand("mark 1"));
        assertEquals("delete", Parser.getCommand("delete 3"));

    }

    @Test
    public void getCommand_commandWithMultipleSpaces_success() {
        assertEquals("todo", Parser.getCommand("todo    buy milk"));
        assertEquals("event", Parser.getCommand("event     meeting /from Mon /to Tue"));
    }

    // ========== getDescription Tests ==========
    @Test
    public void getDescription_commandWithDescription_success() {
        assertEquals("buy milk", Parser.getDescription("todo buy milk", "todo"));
        assertEquals("homework /by Sunday", Parser.getDescription("deadline homework /by Sunday", "deadline"));
        assertEquals("1", Parser.getDescription("mark 1", "mark"));
    }

    @Test
    public void getDescription_commandOnly_returnsEmpty() {
        assertEquals("", Parser.getDescription("list", "list"));
        assertEquals("", Parser.getDescription("bye", "bye"));
        assertEquals("", Parser.getDescription("todo", "todo"));
    }

    @Test
    public void getDescription_extraSpaces_trimmed() {
        assertEquals("buy milk", Parser.getDescription("todo    buy milk", "todo"));
        assertEquals("homework", Parser.getDescription("deadline   homework", "deadline"));
    }

    // ========== getTaskNumber Tests ==========
    @Test
    public void getTaskNumber_validNumber_success() throws EchoException {
        assertEquals(1, Parser.getTaskNumber("mark 1"));
        assertEquals(5, Parser.getTaskNumber("delete 5"));
        assertEquals(10, Parser.getTaskNumber("unmark 10"));
    }

    @Test
    public void getTaskNumber_withExtraSpaces_success() throws EchoException {
        assertEquals(3, Parser.getTaskNumber("mark   3"));
        assertEquals(7, Parser.getTaskNumber("delete  7  "));
    }

    @Test
    public void getTaskNumber_missingNumber_exceptionThrown() {
        assertThrows(EchoException.class, () -> Parser.getTaskNumber("mark"));
        assertThrows(EchoException.class, () -> Parser.getTaskNumber("delete "));
        assertThrows(EchoException.class, () -> Parser.getTaskNumber("mark   "));
    }

    @Test
    public void getTaskNumber_invalidNumber_exceptionThrown() {
        assertThrows(EchoException.class, () -> Parser.getTaskNumber("mark abc"));
        assertThrows(EchoException.class, () -> Parser.getTaskNumber("delete xyz"));
        assertThrows(EchoException.class, () -> Parser.getTaskNumber("unmark one"));
    }

    // ========== parseDeadline Tests ==========
    @Test
    public void parseDeadline_validInput_success() throws EchoException {
        String[] result1 = Parser.parseDeadline("return book /by Sunday");
        assertArrayEquals(new String[]{"return book", "Sunday"}, result1);

        String[] result2 = Parser.parseDeadline("homework /by 2019-12-15");
        assertArrayEquals(new String[]{"homework", "2019-12-15"}, result2);

        String[] result3 = Parser.parseDeadline("submit report /by 2019-12-20 1800");
        assertArrayEquals(new String[]{"submit report", "2019-12-20 1800"}, result3);
    }

    @Test
    public void parseDeadline_withExtraSpaces_trimmed() throws EchoException {
        String[] result = Parser.parseDeadline("  return book  /by  Sunday  ");
        assertArrayEquals(new String[]{"return book", "Sunday"}, result);
    }

    @Test
    public void parseDeadline_missingBy_exceptionThrown() {
        assertThrows(EchoException.class, () -> Parser.parseDeadline("return book"));
        assertThrows(EchoException.class, () -> Parser.parseDeadline("homework on Sunday"));
    }

    @Test
    public void parseDeadline_emptyDescription_exceptionThrown() {
        assertThrows(EchoException.class, () -> Parser.parseDeadline(" /by Sunday"));
        assertThrows(EchoException.class, () -> Parser.parseDeadline("/by Sunday"));
    }

    @Test
    public void parseDeadline_emptyDate_exceptionThrown() {
        assertThrows(EchoException.class, () -> Parser.parseDeadline("return book /by "));
        assertThrows(EchoException.class, () -> Parser.parseDeadline("homework /by"));
    }

    @Test
    public void parseDeadline_multipleBy_takesFirst() throws EchoException {
        String[] result = Parser.parseDeadline("task /by Sunday /by Monday");
        assertArrayEquals(new String[]{"task", "Sunday /by Monday"}, result);
    }

    // ========== parseEvent Tests ==========
    @Test
    public void parseEvent_validInput_success() throws EchoException {
        String[] result1 = Parser.parseEvent("meeting /from Mon 2pm /to 4pm");
        assertArrayEquals(new String[]{"meeting", "Mon 2pm", "4pm"}, result1);

        String[] result2 = Parser.parseEvent("conference /from 2019-12-10 /to 2019-12-12");
        assertArrayEquals(new String[]{"conference", "2019-12-10", "2019-12-12"}, result2);

        String[] result3 = Parser.parseEvent("workshop /from 2019-12-05 1400 /to 2019-12-05 1600");
        assertArrayEquals(new String[]{"workshop", "2019-12-05 1400", "2019-12-05 1600"}, result3);
    }

    @Test
    public void parseEvent_withExtraSpaces_trimmed() throws EchoException {
        String[] result = Parser.parseEvent("  meeting  /from  Mon 2pm  /to  4pm  ");
        assertArrayEquals(new String[]{"meeting", "Mon 2pm", "4pm"}, result);
    }

    @Test
    public void parseEvent_missingFrom_exceptionThrown() {
        assertThrows(EchoException.class, () -> Parser.parseEvent("meeting /to 4pm"));
        assertThrows(EchoException.class, () -> Parser.parseEvent("meeting at 2pm /to 4pm"));
    }

    @Test
    public void parseEvent_missingTo_exceptionThrown() {
        assertThrows(EchoException.class, () -> Parser.parseEvent("meeting /from Mon 2pm"));
        assertThrows(EchoException.class, () -> Parser.parseEvent("meeting /from Mon 2pm until 4pm"));
    }

    @Test
    public void parseEvent_missingBothFromAndTo_exceptionThrown() {
        assertThrows(EchoException.class, () -> Parser.parseEvent("meeting on Monday"));
        assertThrows(EchoException.class, () -> Parser.parseEvent("conference next week"));
    }

    @Test
    public void parseEvent_emptyDescription_exceptionThrown() {
        assertThrows(EchoException.class, () -> Parser.parseEvent(" /from Mon 2pm /to 4pm"));
        assertThrows(EchoException.class, () -> Parser.parseEvent("/from Mon 2pm /to 4pm"));
    }

    @Test
    public void parseEvent_emptyFrom_exceptionThrown() {
        assertThrows(EchoException.class, () -> Parser.parseEvent("meeting /from  /to 4pm"));
        assertThrows(EchoException.class, () -> Parser.parseEvent("meeting /from /to 4pm"));
    }

    @Test
    public void parseEvent_emptyTo_exceptionThrown() {
        assertThrows(EchoException.class, () -> Parser.parseEvent("meeting /from Mon 2pm /to "));
        assertThrows(EchoException.class, () -> Parser.parseEvent("meeting /from Mon 2pm /to"));
    }

    @Test
    public void parseEvent_wrongOrder_stillParses() throws EchoException {
        // Parser splits by " /from | /to " so order in input matters
        String[] result = Parser.parseEvent("meeting /to 4pm /from Mon 2pm");
        // This will parse incorrectly but Parser doesn't validate order
        assertEquals(3, result.length);
    }
}