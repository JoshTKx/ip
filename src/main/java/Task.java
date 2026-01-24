public class Task {
    protected String description;
    protected boolean isDone;

    Task(String description) {
        this.description = description;
        this.isDone = false;
    }
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    public void markDone() {
        isDone = true;
    }

    public void markNotDone() {
        isDone = false;
    }

    public String getDescription() {
        return description;
    }

    public String toFileFormat() {
        return "| " + (isDone ? "1" : "0") + " |" + description;
    }

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
