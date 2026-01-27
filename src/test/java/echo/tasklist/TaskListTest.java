package echo.tasklist;

import echo.task.Task;
import echo.task.Todo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskListTest {

    @Test
    public void add_singleTask_success() {
        TaskList tasks = new TaskList();
        Task task = new Todo("buy milk");
        tasks.add(task);
        assertEquals(1, tasks.size());
        assertEquals(task, tasks.get(0));
    }

    @Test
    public void remove_existingTask_success() {
        TaskList tasks = new TaskList();
        Task task1 = new Todo("buy milk");
        Task task2 = new Todo("read book");
        tasks.add(task1);
        tasks.add(task2);

        Task removed = tasks.remove(0);

        assertEquals(1, tasks.size());
        assertEquals(task1, removed);
        assertEquals(task2, tasks.get(0));
    }

    @Test
    public void size_emptyList_returnsZero() {
        TaskList tasks = new TaskList();
        assertEquals(0, tasks.size());
    }

    @Test
    public void size_multipleItems_returnsCorrectSize() {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("task 1"));
        tasks.add(new Todo("task 2"));
        tasks.add(new Todo("task 3"));
        assertEquals(3, tasks.size());
    }
}