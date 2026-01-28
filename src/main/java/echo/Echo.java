package echo;

import echo.exception.EchoException;
import echo.parser.Parser;
import echo.storage.Storage;
import echo.task.Deadline;
import echo.task.Event;
import echo.task.Task;
import echo.task.Todo;
import echo.tasklist.TaskList;
import echo.ui.Ui;

import java.io.IOException;

/**
 * Main class for the Echo task management chatbot.
 * Handles initialization, command processing, and coordination between components.
 */

public class Echo {
    private static final String FILE_PATH = "./data/echo.txt";

    private final Storage storage;
    private TaskList tasks;
    private final Ui ui;


    /**
     * Constructs an Echo instance with the specified file path for data storage.
     *
     * @param filePath The path to the data file for saving and loading tasks.
     */
    public Echo(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
            if (tasks.size() > 0) {
                System.out.println("Loaded " + tasks.size() + " task(s) from file.\n");
            }
        } catch (IOException e) {
            ui.showLoadingError();
            tasks = new TaskList();
        }
    }


    /**
     * Runs the main loop of the chatbot, processing user commands until exit.
     */
    public void run() {
        ui.showWelcome();

        while (true) {
            String input = ui.readCommand();

            try {
                String command = Parser.getCommand(input);

                if (command.equals("bye")) {
                    break;

                } else if (command.equals("list")) {
                    ui.showTaskList(tasks);

                } else if (command.equals("mark")) {
                    handleMark(input);

                } else if (command.equals("unmark")) {
                    handleUnmark(input);

                } else if (command.equals("todo")) {
                    handleTodo(input);

                } else if (command.equals("deadline")) {
                    handleDeadline(input);

                } else if (command.equals("event")) {
                    handleEvent(input);

                } else if (command.equals("delete")) {
                    handleDelete(input);

                } else if (command.equals("clear")) {
                    handleClear();

                } else if (!input.trim().isEmpty()) {
                    throw new EchoException("I don't understand '" + input
                            + "'. Try: todo, deadline, event, list, mark, or unmark.");
                }

            } catch (EchoException e) {
                ui.showError(e.getMessage());
            } catch (IOException e) {
                ui.showError("Error saving to file: " + e.getMessage());
            } catch (Exception e) {
                ui.showError("Uh oh! Something unexpected happened: " + e.getMessage());
            }
        }

        ui.showGoodbye();
        ui.close();
    }


    /**
     * Handles the mark command to mark a task as done.
     *
     * @param input The user input containing the task number to mark.
     * @throws EchoException If the task number is invalid or missing.
     * @throws IOException If there's an error saving to file.
     */
    private void handleMark(String input) throws EchoException, IOException {
        if (input.equals("mark") || Parser.getDescription(input, "mark").isEmpty()) {
            throw new EchoException("Which task should I mark? Use: mark <task number>");
        }
        int taskNum = Parser.getTaskNumber(input) - 1;
        if (taskNum < 0 || taskNum >= tasks.size()) {
            throw new EchoException("Task number doesn't exist.");
        }
        tasks.get(taskNum).markDone();
        storage.save(tasks);
        ui.showTaskMarked(tasks.get(taskNum), true);
    }


    /**
     * Handles the unmark command to mark a task as not done.
     *
     * @param input The user input containing the task number to unmark.
     * @throws EchoException If the task number is invalid or missing.
     * @throws IOException If there's an error saving to file.
     */
    private void handleUnmark(String input) throws EchoException, IOException {
        if (input.equals("unmark") || Parser.getDescription(input, "unmark").isEmpty()) {
            throw new EchoException("Which task should I unmark? Use: unmark <task number>");
        }
        int taskNum = Parser.getTaskNumber(input) - 1;
        if (taskNum < 0 || taskNum >= tasks.size()) {
            throw new EchoException("Task number doesn't exist.");
        }
        tasks.get(taskNum).markNotDone();
        storage.save(tasks);
        ui.showTaskMarked(tasks.get(taskNum), false);
    }

    /**
     * Handles the todo command to create a new todo task.
     *
     * @param input The full user input string containing the todo command and description.
     * @throws EchoException If the task description is empty.
     * @throws IOException If there's an error saving the new task to file.
     */
    private void handleTodo(String input) throws EchoException, IOException {
        String description = Parser.getDescription(input, "todo");
        if (description.isEmpty()) {
            throw new EchoException("Hmm, you forgot to tell me what the todo is! Try: todo <description>");
        }
        Task task = new Todo(description);
        tasks.add(task);
        storage.save(tasks);
        ui.showTaskAdded(task, tasks.size());
    }


    /**
     * Handles the deadline command to create a new deadline task.
     *
     * @param input The full user input string containing the deadline command, description, and date.
     * @throws EchoException If the description or date is missing or invalid.
     * @throws IOException If there's an error saving the new task to file.
     */
    private void handleDeadline(String input) throws EchoException, IOException {
        String description = Parser.getDescription(input, "deadline");
        String[] parts = Parser.parseDeadline(description);
        Task task = new Deadline(parts[0], parts[1]);
        tasks.add(task);
        storage.save(tasks);
        ui.showTaskAdded(task, tasks.size());
    }


    /**
     * Handles the event command to create a new event task.
     *
     * @param input The full user input string containing the event command, description, start and end times.
     * @throws EchoException If the description, start time, or end time is missing or invalid.
     * @throws IOException If there's an error saving the new task to file.
     */
    private void handleEvent(String input) throws EchoException, IOException {
        String description = Parser.getDescription(input, "event");
        String[] parts = Parser.parseEvent(description);
        Task task = new Event(parts[0], parts[1], parts[2]);
        tasks.add(task);
        storage.save(tasks);
        ui.showTaskAdded(task, tasks.size());
    }


    /**
     * Handles the delete command to remove a task from the list.
     *
     * @param input The full user input string containing the delete command and task number.
     * @throws EchoException If the task number is invalid, missing, or out of bounds.
     * @throws IOException If there's an error saving the updated task list to file.
     */
    private void handleDelete(String input) throws EchoException, IOException {
        if (input.equals("delete") || Parser.getDescription(input, "delete").isEmpty()) {
            throw new EchoException("Please specify which task to delete.");
        }
        int taskNum = Parser.getTaskNumber(input) - 1;
        if (taskNum < 0 || taskNum >= tasks.size()) {
            throw new EchoException("Task number doesn't exist.");
        }
        Task removedTask = tasks.remove(taskNum);
        storage.save(tasks);
        ui.showTaskDeleted(removedTask, tasks.size());
    }


    /**
     * Handles the clear command to remove all tasks from the list.
     *
     * @throws IOException If there's an error saving the empty task list to file.
     */
    private void handleClear() throws IOException {
        tasks.clear();
        storage.save(tasks);
        ui.showLine();
        System.out.println(" All tasks have been cleared!");
        ui.showLine();
    }

    /**
     * Main entry point for the Echo application.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        new Echo(FILE_PATH).run();
    }
}
