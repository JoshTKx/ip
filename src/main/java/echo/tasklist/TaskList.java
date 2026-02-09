package echo.tasklist;

import java.util.ArrayList;
import java.util.stream.Collectors;

import echo.task.Task;

/**
 * Manages a list of tasks.
 * Provides operations to add, remove, and retrieve tasks from the list.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /**
     * Constructs an empty TaskList.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Constructs a TaskList with an existing ArrayList of tasks.
     *
     * @param tasks The ArrayList of tasks to initialize with.
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Adds a task to the list.
     *
     * @param task The task to add.
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Removes and returns the task at the specified index.
     *
     * @param index The index of the task to remove (0-based).
     * @return The removed task.
     * @throws IndexOutOfBoundsException If the index is out of range.
     */
    public Task remove(int index) {
        return tasks.remove(index);
    }

    /**
     * Returns the task at the specified index.
     *
     * @param index The index of the task to retrieve (0-based).
     * @return The task at the specified index.
     * @throws IndexOutOfBoundsException If the index is out of range.
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return The size of the task list.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Removes all tasks from the list.
     */
    public void clear() {
        tasks.clear();
    }

    /**
     * Finds all tasks whose descriptions contain the specified keyword.
     *
     * @param keyword The keyword to search for in task descriptions.
     * @return A new TaskList containing all matching tasks.
     */
    public TaskList findTasks(String keyword) {
        ArrayList<Task> matchingTasks = (ArrayList<Task>) tasks.stream()
                .filter(task -> task.getDescription().contains(keyword))
                .collect(Collectors.toList());
        return new TaskList(matchingTasks);
    }
}
