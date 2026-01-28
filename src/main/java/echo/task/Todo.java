package echo.task;

/**
 * Represents a todo task without any date/time attached to it.
 * A todo is a simple task with just a description.
 */
public class Todo extends Task {

    /**
     * Constructs a Todo task with the specified description.
     *
     * @param description The description of the todo task.
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns the file format representation of this todo task.
     * Format: "T | STATUS | DESCRIPTION"
     *
     * @return A string representation suitable for file storage.
     */
    @Override
    public String toFileFormat() {
        return "T " + super.toFileFormat();
    }

    /**
     * Returns the string representation of this todo task for display.
     * Format: "[T][STATUS] DESCRIPTION"
     *
     * @return A formatted string showing the task type, status, and description.
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}