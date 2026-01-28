package echo.tasklist;

import echo.task.Task;
import echo.task.Todo;
import echo.task.Deadline;
import echo.task.Event;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TaskListTest {

    private TaskList tasks;

    @BeforeEach
    public void setUp() {
        tasks = new TaskList();
    }

    // ========== Add Tests ==========
    @Test
    public void add_singleTask_success() {
        Task task = new Todo("buy milk");
        tasks.add(task);
        assertEquals(1, tasks.size());
        assertEquals(task, tasks.get(0));
    }

    @Test
    public void add_multipleTasks_success() {
        Task task1 = new Todo("buy milk");
        Task task2 = new Deadline("homework", "Sunday");
        Task task3 = new Event("meeting", "Mon 2pm", "4pm");

        tasks.add(task1);
        tasks.add(task2);
        tasks.add(task3);

        assertEquals(3, tasks.size());
        assertEquals(task1, tasks.get(0));
        assertEquals(task2, tasks.get(1));
        assertEquals(task3, tasks.get(2));
    }

    @Test
    public void add_differentTaskTypes_success() {
        tasks.add(new Todo("todo task"));
        tasks.add(new Deadline("deadline task", "2019-12-15"));
        tasks.add(new Event("event task", "Mon", "Tue"));

        assertEquals(3, tasks.size());
    }

    // ========== Remove Tests ==========
    @Test
    public void remove_firstTask_success() {
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
    public void remove_lastTask_success() {
        Task task1 = new Todo("buy milk");
        Task task2 = new Todo("read book");
        tasks.add(task1);
        tasks.add(task2);

        Task removed = tasks.remove(1);

        assertEquals(1, tasks.size());
        assertEquals(task2, removed);
        assertEquals(task1, tasks.get(0));
    }

    @Test
    public void remove_middleTask_success() {
        Task task1 = new Todo("task 1");
        Task task2 = new Todo("task 2");
        Task task3 = new Todo("task 3");
        tasks.add(task1);
        tasks.add(task2);
        tasks.add(task3);

        Task removed = tasks.remove(1);

        assertEquals(2, tasks.size());
        assertEquals(task2, removed);
        assertEquals(task1, tasks.get(0));
        assertEquals(task3, tasks.get(1));
    }

    @Test
    public void remove_invalidIndex_exceptionThrown() {
        tasks.add(new Todo("task"));
        assertThrows(IndexOutOfBoundsException.class, () -> tasks.remove(5));
        assertThrows(IndexOutOfBoundsException.class, () -> tasks.remove(-1));
    }

    // ========== Get Tests ==========
    @Test
    public void get_validIndex_success() {
        Task task1 = new Todo("task 1");
        Task task2 = new Todo("task 2");
        tasks.add(task1);
        tasks.add(task2);

        assertEquals(task1, tasks.get(0));
        assertEquals(task2, tasks.get(1));
    }

    @Test
    public void get_invalidIndex_exceptionThrown() {
        tasks.add(new Todo("task"));
        assertThrows(IndexOutOfBoundsException.class, () -> tasks.get(5));
        assertThrows(IndexOutOfBoundsException.class, () -> tasks.get(-1));
    }

    // ========== Size Tests ==========
    @Test
    public void size_emptyList_returnsZero() {
        assertEquals(0, tasks.size());
    }

    @Test
    public void size_afterAdding_returnsCorrectSize() {
        tasks.add(new Todo("task 1"));
        assertEquals(1, tasks.size());

        tasks.add(new Todo("task 2"));
        assertEquals(2, tasks.size());

        tasks.add(new Todo("task 3"));
        assertEquals(3, tasks.size());
    }

    @Test
    public void size_afterRemoving_returnsCorrectSize() {
        tasks.add(new Todo("task 1"));
        tasks.add(new Todo("task 2"));
        tasks.add(new Todo("task 3"));
        assertEquals(3, tasks.size());

        tasks.remove(0);
        assertEquals(2, tasks.size());

        tasks.remove(1);
        assertEquals(1, tasks.size());

        tasks.remove(0);
        assertEquals(0, tasks.size());
    }

    // ========== Constructor Tests ==========
    @Test
    public void constructor_withArrayList_success() {
        ArrayList<Task> taskList = new ArrayList<>();
        taskList.add(new Todo("task 1"));
        taskList.add(new Todo("task 2"));

        TaskList tasks = new TaskList(taskList);
        assertEquals(2, tasks.size());
    }

    @Test
    public void constructor_withEmptyArrayList_success() {
        ArrayList<Task> taskList = new ArrayList<>();
        TaskList tasks = new TaskList(taskList);
        assertEquals(0, tasks.size());
    }

    // ========== Find Tests ==========
    @Test
    public void findTasks_matchingKeyword_returnsMatchingTasks() {
        tasks.add(new Todo("read book"));
        tasks.add(new Todo("buy groceries"));
        tasks.add(new Deadline("return book", "Sunday"));

        TaskList results = tasks.findTasks("book");

        assertEquals(2, results.size());
        assertEquals("[T][ ] read book", results.get(0).toString());
        assertEquals("[D][ ] return book (by: Sunday)", results.get(1).toString());
    }

    @Test
    public void findTasks_noMatch_returnsEmptyList() {
        tasks.add(new Todo("read book"));
        tasks.add(new Todo("buy groceries"));

        TaskList results = tasks.findTasks("homework");

        assertEquals(0, results.size());
    }

    @Test
    public void findTasks_emptyList_returnsEmptyList() {
        TaskList results = tasks.findTasks("book");
        assertEquals(0, results.size());
    }

    @Test
    public void findTasks_partialMatch_returnsMatchingTasks() {
        tasks.add(new Todo("read book"));
        tasks.add(new Todo("bookmark page"));
        tasks.add(new Todo("buy groceries"));

        TaskList results = tasks.findTasks("book");

        assertEquals(2, results.size());
    }

    @Test
    public void findTasks_caseSensitive_returnsExactMatches() {
        tasks.add(new Todo("Read Book"));
        tasks.add(new Todo("read book"));

        TaskList results = tasks.findTasks("book");

        assertEquals(1, results.size());
        assertEquals("[T][ ] read book", results.get(0).toString());
    }
}