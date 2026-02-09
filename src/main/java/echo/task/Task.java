package echo.task;

/**
 * Represents a task with a description and completion status.
 * This is an abstract base class for different types of tasks (Todo, Deadline, Event).
 */
public class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Constructs a Task with the specified description.
     * The task is initially marked as not done.
     *
     * @param description The description of the task.
     */
    protected Task(String description) {
        assert description != null : "Task description cannot be null";
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the status icon of the task.
     *
     * @return "X" if the task is done, " " (space) otherwise.
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /**
     * Marks this task as done.
     */
    public void markDone() {
        isDone = true;
    }

    /**
     * Marks this task as not done.
     */
    public void markNotDone() {
        isDone = false;
    }

    /**
     * Returns the description of this task.
     *
     * @return The task description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the file format representation of this task for saving to file.
     * Format: "| STATUS | DESCRIPTION" where STATUS is 1 for done, 0 for not done.
     *
     * @return A string representation suitable for file storage.
     */
    public String toFileFormat() {
        return "| " + (isDone ? "1" : "0") + " | " + description;
    }

    /**
     * Returns the string representation of this task for display to user.
     *
     * @return A formatted string showing the task status and description.
     */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
