package echo.storage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import echo.task.Deadline;
import echo.task.Event;
import echo.task.Task;
import echo.task.Todo;
import echo.tasklist.TaskList;

/**
 * Handles loading and saving of tasks to/from a file.
 * Manages file I/O operations and parsing of task data from file format.
 */
public class Storage {

    private static final String DELIMITER = " \\| ";
    private static final int MIN_PARTS = 3;
    private static final int DEADLINE_PARTS = 4;
    private static final int EVENT_PARTS = 5;
    private static final int EVENT_PARTS_WITH_RECURRENCE = 6;
    private static final int INDEX_TYPE = 0;
    private static final int INDEX_STATUS = 1;
    private static final int INDEX_DESCRIPTION = 2;
    private static final int INDEX_DEADLINE_BY = 3;
    private static final int INDEX_EVENT_FROM = 3;
    private static final int INDEX_EVENT_TO = 4;
    private static final int INDEX_EVENT_RECURRENCE = 5;
    private static final String STATUS_DONE = "1";
    private static final String TYPE_TODO = "T";
    private static final String TYPE_DEADLINE = "D";
    private static final String TYPE_EVENT = "E";

    private final String filePath;

    /**
     * Constructs a Storage object with the specified file path.
     *
     * @param filePath The path to the file used for storing tasks.
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads tasks from the storage file.
     * Creates the directory and file if they don't exist.
     * Skips and warns about any corrupted lines in the file.
     *
     * @return An ArrayList of tasks loaded from the file, or an empty list if file doesn't exist.
     * @throws IOException If there's an error reading from the file.
     */
    public ArrayList<Task> load() throws IOException {
        ArrayList<Task> tasks = new ArrayList<>();
        File file = new File(filePath);

        // Create directory if it doesn't exist
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        // If file doesn't exist, return empty list
        if (!file.exists()) {
            return tasks;
        }

        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            try {
                Task task = parseTask(line);
                if (task != null) {
                    tasks.add(task);
                }
            } catch (Exception e) {
                // Skip corrupted lines
                System.out.println("Warning: Skipped corrupted line: " + line);
            }
        }
        scanner.close();
        return tasks;
    }

    /**
     * Parses a single line from the file into a Task object.
     * Expected format: "TYPE | STATUS | DESCRIPTION | [ADDITIONAL_INFO]"
     *
     * @param line The line from the file to parse.
     * @return A Task object (Todo, Deadline, or Event), or null if the line is corrupted.
     */
    private Task parseTask(String line) {
        assert line != null : "Line cannot be null";
        String[] parts = line.split(DELIMITER);
        if (parts.length < MIN_PARTS) {
            return null; // Corrupted line
        }

        parts = Arrays.stream(parts)
                .map(String::trim)
                .toArray(String[]::new);

        String type = parts[INDEX_TYPE];
        boolean isDone = parts[INDEX_STATUS].equals(STATUS_DONE);
        String description = parts[INDEX_DESCRIPTION];

        Task task = null;
        switch (type) {
        case TYPE_TODO:
            task = new Todo(description);
            break;
        case TYPE_DEADLINE:
            if (parts.length >= DEADLINE_PARTS) {
                task = new Deadline(description, parts[INDEX_DEADLINE_BY]);
            }
            break;
        case TYPE_EVENT:
            if (parts.length >= EVENT_PARTS_WITH_RECURRENCE) {
                // Event with recurrence
                String recurrence = parts[INDEX_EVENT_RECURRENCE];
                if (recurrence.equals("none")) {
                    task = new Event(description, parts[INDEX_EVENT_FROM], parts[INDEX_EVENT_TO]);
                } else {
                    task = new Event(description, parts[INDEX_EVENT_FROM], parts[INDEX_EVENT_TO], recurrence);
                }
            } else if (parts.length >= EVENT_PARTS) {
                // Event without recurrence (old format, backward compatible)
                task = new Event(description, parts[INDEX_EVENT_FROM], parts[INDEX_EVENT_TO]);
            }
            break;
        default:
            break;
        }

        if (task != null && isDone) {
            task.markDone();
        }

        assert task == null || task.getDescription() != null : "Task description must not be null";
        return task;
    }

    /**
     * Saves the list of tasks to the storage file.
     * Creates the directory and file if they don't exist.
     * Overwrites any existing file content.
     *
     * @param tasks The TaskList containing tasks to save.
     * @throws IOException If there's an error writing to the file.
     */
    public void save(TaskList tasks) throws IOException {
        File file = new File(filePath);

        // Create directory if it doesn't exist
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        FileWriter writer = new FileWriter(file);
        for (int i = 0; i < tasks.size(); i++) {
            writer.write(tasks.get(i).toFileFormat() + "\n");
        }
        writer.close();
    }
}
