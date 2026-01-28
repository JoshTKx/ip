package echo.storage;

import echo.task.Task;
import echo.task.Todo;
import echo.task.Deadline;
import echo.task.Event;
import echo.tasklist.TaskList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StorageTest {

    @TempDir
    Path tempDir;

    private Storage storage;
    private String testFilePath;

    @BeforeEach
    public void setUp() {
        testFilePath = tempDir.resolve("test_tasks.txt").toString();
        storage = new Storage(testFilePath);
    }

    // ========== Load Tests ==========
    @Test
    public void load_fileDoesNotExist_returnsEmptyList() throws IOException {
        ArrayList<Task> tasks = storage.load();
        assertEquals(0, tasks.size());
    }

    @Test
    public void load_emptyFile_returnsEmptyList() throws IOException {
        new File(testFilePath).getParentFile().mkdirs();
        new File(testFilePath).createNewFile();

        ArrayList<Task> tasks = storage.load();
        assertEquals(0, tasks.size());
    }

    @Test
    public void load_validTodoTask_success() throws IOException {
        writeToFile("T | 0 | buy milk");

        ArrayList<Task> tasks = storage.load();
        assertEquals(1, tasks.size());
        assertEquals("[T][ ] buy milk", tasks.get(0).toString());
    }

    @Test
    public void load_validDeadlineTask_success() throws IOException {
        writeToFile("D | 0 | return book | Sunday");

        ArrayList<Task> tasks = storage.load();
        assertEquals(1, tasks.size());
        assertEquals("[D][ ] return book (by: Sunday)", tasks.get(0).toString());
    }

    @Test
    public void load_validEventTask_success() throws IOException {
        writeToFile("E | 0 | meeting | Mon 2pm | 4pm");

        ArrayList<Task> tasks = storage.load();
        assertEquals(1, tasks.size());
        assertEquals("[E][ ] meeting (from: Mon 2pm to: 4pm)", tasks.get(0).toString());
    }

    @Test
    public void load_markedTask_loadsAsDone() throws IOException {
        writeToFile("T | 1 | buy milk");

        ArrayList<Task> tasks = storage.load();
        assertEquals(1, tasks.size());
        assertEquals("X", tasks.get(0).getStatusIcon());
        assertEquals("[T][X] buy milk", tasks.get(0).toString());
    }

    @Test
    public void load_multipleTasks_success() throws IOException {
        writeToFile("T | 0 | buy milk\nD | 1 | homework | Sunday\nE | 0 | meeting | Mon | Tue");

        ArrayList<Task> tasks = storage.load();
        assertEquals(3, tasks.size());
        assertEquals("[T][ ] buy milk", tasks.get(0).toString());
        assertEquals("[D][X] homework (by: Sunday)", tasks.get(1).toString());
        assertEquals("[E][ ] meeting (from: Mon to: Tue)", tasks.get(2).toString());
    }

    @Test
    public void load_corruptedLine_skipsLine() throws IOException {
        writeToFile("T | 0 | buy milk\nCORRUPTED LINE\nD | 0 | homework | Sunday");

        ArrayList<Task> tasks = storage.load();
        assertEquals(2, tasks.size());
        assertEquals("[T][ ] buy milk", tasks.get(0).toString());
        assertEquals("[D][ ] homework (by: Sunday)", tasks.get(1).toString());
    }

    @Test
    public void load_incompleteLine_skipsLine() throws IOException {
        writeToFile("T | 0\nD | 0 | homework | Sunday");

        ArrayList<Task> tasks = storage.load();
        assertEquals(1, tasks.size());
        assertEquals("[D][ ] homework (by: Sunday)", tasks.get(0).toString());
    }

    @Test
    public void load_incompleteDeadline_skipsLine() throws IOException {
        writeToFile("D | 0 | homework\nT | 0 | buy milk");

        ArrayList<Task> tasks = storage.load();
        assertEquals(1, tasks.size());
        assertEquals("[T][ ] buy milk", tasks.get(0).toString());
    }

    @Test
    public void load_incompleteEvent_skipsLine() throws IOException {
        writeToFile("E | 0 | meeting | Mon\nT | 0 | buy milk");

        ArrayList<Task> tasks = storage.load();
        assertEquals(1, tasks.size());
        assertEquals("[T][ ] buy milk", tasks.get(0).toString());
    }

    // ========== Save Tests ==========
    @Test
    public void save_emptyList_createsEmptyFile() throws IOException {
        TaskList tasks = new TaskList();
        storage.save(tasks);

        assertTrue(new File(testFilePath).exists());
        ArrayList<Task> loaded = storage.load();
        assertEquals(0, loaded.size());
    }

    @Test
    public void save_singleTask_success() throws IOException {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("buy milk"));
        storage.save(tasks);

        ArrayList<Task> loaded = storage.load();
        assertEquals(1, loaded.size());
        assertEquals("[T][ ] buy milk", loaded.get(0).toString());
    }

    @Test
    public void save_multipleTasks_success() throws IOException {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("buy milk"));
        tasks.add(new Deadline("homework", "Sunday"));
        tasks.add(new Event("meeting", "Mon 2pm", "4pm"));
        storage.save(tasks);

        ArrayList<Task> loaded = storage.load();
        assertEquals(3, loaded.size());
        assertEquals("[T][ ] buy milk", loaded.get(0).toString());
        assertEquals("[D][ ] homework (by: Sunday)", loaded.get(1).toString());
        assertEquals("[E][ ] meeting (from: Mon 2pm to: 4pm)", loaded.get(2).toString());
    }

    @Test
    public void save_markedTask_preservesStatus() throws IOException {
        TaskList tasks = new TaskList();
        Todo todo = new Todo("buy milk");
        todo.markDone();
        tasks.add(todo);
        storage.save(tasks);

        ArrayList<Task> loaded = storage.load();
        assertEquals(1, loaded.size());
        assertEquals("X", loaded.get(0).getStatusIcon());
    }

    // ========== Save and Load Cycle Tests ==========
    @Test
    public void saveAndLoad_taskWithDates_preservesFormat() throws IOException {
        TaskList tasks = new TaskList();
        tasks.add(new Deadline("homework", "2019-12-15"));
        tasks.add(new Event("workshop", "2019-12-05 1400", "2019-12-05 1600"));
        storage.save(tasks);

        ArrayList<Task> loaded = storage.load();
        assertEquals(2, loaded.size());
        assertEquals("[D][ ] homework (by: Dec 15 2019)", loaded.get(0).toString());
        assertEquals("[E][ ] workshop (from: Dec 5 2019, 2:00PM to: Dec 5 2019, 4:00PM)", loaded.get(1).toString());
    }

    @Test
    public void saveAndLoad_mixedTasks_preservesAll() throws IOException {
        TaskList tasks = new TaskList();
        Todo todo = new Todo("buy milk");
        todo.markDone();
        tasks.add(todo);
        tasks.add(new Deadline("homework", "Sunday"));
        Event event = new Event("meeting", "Mon 2pm", "4pm");
        event.markDone();
        tasks.add(event);
        storage.save(tasks);

        ArrayList<Task> loaded = storage.load();
        assertEquals(3, loaded.size());
        assertEquals("X", loaded.get(0).getStatusIcon());
        assertEquals(" ", loaded.get(1).getStatusIcon());
        assertEquals("X", loaded.get(2).getStatusIcon());
    }

    @Test
    public void save_overwritesExistingFile_success() throws IOException {
        // Save first set of tasks
        TaskList tasks1 = new TaskList();
        tasks1.add(new Todo("task 1"));
        tasks1.add(new Todo("task 2"));
        storage.save(tasks1);

        // Save different set of tasks
        TaskList tasks2 = new TaskList();
        tasks2.add(new Todo("task 3"));
        storage.save(tasks2);

        // Load should only have the second set
        ArrayList<Task> loaded = storage.load();
        assertEquals(1, loaded.size());
        assertEquals("[T][ ] task 3", loaded.get(0).toString());
    }

    // ========== Helper Methods ==========
    private void writeToFile(String content) throws IOException {
        File file = new File(testFilePath);
        file.getParentFile().mkdirs();
        FileWriter writer = new FileWriter(file);
        writer.write(content);
        writer.close();
    }
}