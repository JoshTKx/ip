package echo.ui;

import echo.task.Task;
import echo.tasklist.TaskList;

import java.util.Scanner;

/**
 * Handles all user interface interactions.
 * Manages input/output operations including displaying messages and reading user commands.
 */
public class Ui {
    private final Scanner scanner;

    /**
     * Constructs a Ui instance and initializes the scanner for reading user input.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Displays the welcome message when the application starts.
     */
    public void showWelcome() {
        showLine();
        System.out.println(" Hello! I'm Echo");
        System.out.println(" What can I do for you?");
        showLine();
    }

    /**
     * Displays the goodbye message when the application exits.
     */
    public void showGoodbye() {
        showLine();
        System.out.println(" Bye. Hope to see you again soon!");
        showLine();
    }

    /**
     * Displays a horizontal line separator.
     */
    public void showLine() {
        System.out.println("____________________________________________________________");
    }

    /**
     * Displays an error message to the user.
     *
     * @param message The error message to display.
     */
    public void showError(String message) {
        showLine();
        System.out.println(" " + message);
        showLine();
    }

    /**
     * Displays a message indicating that no previous data was found.
     */
    public void showLoadingError() {
        System.out.println("No previous data found. Starting fresh!\n");
    }

    /**
     * Displays a confirmation message after a task has been added.
     *
     * @param task The task that was added.
     * @param taskCount The total number of tasks in the list after adding.
     */
    public void showTaskAdded(Task task, int taskCount) {
        showLine();
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + taskCount + " tasks in the list.");
        showLine();
    }

    /**
     * Displays a confirmation message after a task has been deleted.
     *
     * @param task The task that was deleted.
     * @param taskCount The total number of tasks in the list after deletion.
     */
    public void showTaskDeleted(Task task, int taskCount) {
        showLine();
        System.out.println(" Noted. I've removed this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + taskCount + " tasks in the list.");
        showLine();
    }

    /**
     * Displays a confirmation message after a task's status has been changed.
     *
     * @param task The task whose status was changed.
     * @param isDone True if the task was marked as done, false if marked as not done.
     */
    public void showTaskMarked(Task task, boolean isDone) {
        showLine();
        if (isDone) {
            System.out.println(" Nice! I've marked this task as done:");
        } else {
            System.out.println(" OK, I've marked this task as not done yet:");
        }
        System.out.println("   " + task);
        showLine();
    }

    /**
     * Displays the list of all tasks.
     *
     * @param tasks The TaskList containing all tasks to display.
     */
    public void showTaskList(TaskList tasks) {
        showLine();
        System.out.println(" Here are the tasks in your list:\n");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + "." + tasks.get(i));
        }
        showLine();
        System.out.println();
    }


    /**
     * Displays the list of tasks matching a search keyword.
     *
     * @param matchingTasks The TaskList containing matching tasks to display.
     */
    public void showMatchingTasks(TaskList matchingTasks) {
        showLine();
        if (matchingTasks.size() == 0) {
            System.out.println(" No matching tasks found.");
        } else {
            System.out.println(" Here are the matching tasks in your list:");
            for (int i = 0; i < matchingTasks.size(); i++) {
                System.out.println(" " + (i + 1) + "." + matchingTasks.get(i));
            }
        }
        showLine();
    }

    /**
     * Reads and returns the next line of user input.
     *
     * @return The user's input as a string.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Closes the scanner to release system resources.
     * Should be called when the application exits.
     */
    public void close() {
        scanner.close();
    }
}