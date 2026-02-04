package echo.storage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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
        String[] parts = line.split(" \\| ");
        if (parts.length < 3) {
            return null; // Corrupted line
        }

        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].trim();
        }

        String type = parts[0];
        boolean isDone = parts[1].equals("1");
        String description = parts[2];

        Task task = null;
        switch (type) {
        case "T":
            task = new Todo(description);
            break;
        case "D":
            if (parts.length >= 4) {
                task = new Deadline(description, parts[3]);
            }
            break;
        case "E":
            if (parts.length >= 5) {
                task = new Event(description, parts[3], parts[4]);
            }
            break;
        default:
            break;
        }

        if (task != null && isDone) {
            task.markDone();
        }
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
