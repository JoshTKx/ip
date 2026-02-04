package echo.task;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TodoTest {

    @Test
    public void constructor_validDescription_success() {
        Todo todo = new Todo("buy milk");
        assertEquals("buy milk", todo.getDescription());
        assertEquals(" ", todo.getStatusIcon());
    }

    @Test
    public void markDone_notDoneTask_marksAsDone() {
        Todo todo = new Todo("buy milk");
        todo.markDone();
        assertEquals("X", todo.getStatusIcon());
    }

    @Test
    public void markNotDone_doneTask_marksAsNotDone() {
        Todo todo = new Todo("buy milk");
        todo.markDone();
        todo.markNotDone();
        assertEquals(" ", todo.getStatusIcon());
    }

    @Test
    public void toString_notDone_correctFormat() {
        Todo todo = new Todo("buy milk");
        assertEquals("[T][ ] buy milk", todo.toString());
    }

    @Test
    public void toString_done_correctFormat() {
        Todo todo = new Todo("buy milk");
        todo.markDone();
        assertEquals("[T][X] buy milk", todo.toString());
    }

    @Test
    public void toFileFormat_notDone_correctFormat() {
        Todo todo = new Todo("buy milk");
        assertEquals("T | 0 | buy milk", todo.toFileFormat());
    }

    @Test
    public void toFileFormat_done_correctFormat() {
        Todo todo = new Todo("buy milk");
        todo.markDone();
        assertEquals("T | 1 | buy milk", todo.toFileFormat());
    }

    @Test
    public void getStatusIcon_notDone_returnsSpace() {
        Todo todo = new Todo("buy milk");
        assertEquals(" ", todo.getStatusIcon());
    }

    @Test
    public void getStatusIcon_done_returnsX() {
        Todo todo = new Todo("buy milk");
        todo.markDone();
        assertEquals("X", todo.getStatusIcon());
    }
}
