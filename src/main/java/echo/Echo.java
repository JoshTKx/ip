package echo;

import java.io.IOException;

import echo.exception.EchoException;
import echo.parser.Parser;
import echo.storage.Storage;
import echo.task.Deadline;
import echo.task.Event;
import echo.task.Task;
import echo.task.Todo;
import echo.tasklist.TaskList;

/**
 * Main class for the Echo task management chatbot.
 * Handles initialization, command processing, and coordination between components.
 */
public class Echo {
    private static final String FILE_PATH = "./data/echo.txt";
    private static final String COMMAND_BYE = "bye";
    private static final String COMMAND_CLEAR = "clear";
    private static final String COMMAND_DEADLINE = "deadline";
    private static final String COMMAND_DELETE = "delete";
    private static final String COMMAND_EVENT = "event";
    private static final String COMMAND_FIND = "find";
    private static final String COMMAND_LIST = "list";
    private static final String COMMAND_MARK = "mark";
    private static final String COMMAND_TODO = "todo";
    private static final String COMMAND_UNMARK = "unmark";
    private final Storage storage;
    private TaskList tasks;
    /**
     * Constructs an Echo instance with the specified file path for data storage.
     *
     * @param filePath The path to the data file for saving and loading tasks.
     */
    public Echo(String filePath) {
        assert filePath != null : "File path cannot be null";
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
            if (tasks.size() > 0) {
                System.out.println("Loaded " + tasks.size() + " task(s) from file.\n");
            }
        } catch (IOException e) {
            tasks = new TaskList();
        }

        assert storage != null : "Storage must be initialized";
        assert tasks != null : "TaskList must be initialized";
    }

    /**
     * Generates a response for the user's input without using the UI.
     * This method is used by the GUI to get responses as strings.
     *
     * @param input The user's input command.
     * @return The response string from Echo.
     */
    public String getResponse(String input) {
        try {
            String command = Parser.getCommand(input);
            return executeCommand(command, input);
        } catch (EchoException e) {
            return e.getMessage();
        } catch (IOException e) {
            return "Error saving to file: " + e.getMessage();
        } catch (Exception e) {
            return "Uh oh! Something unexpected happened: " + e.getMessage();
        }
    }


    /**
     * Executes the appropriate command based on the command string.
     *
     * @param command The command string extracted from user input.
     * @param input   The full user input string.
     * @return The response string from executing the command.
     * @throws EchoException If the command is invalid or execution fails.
     * @throws IOException   If there's an error saving to file.
     */
    private String executeCommand(String command, String input) throws EchoException, IOException {
        switch (command) {
        case COMMAND_BYE:
            return "Bye. Hope to see you again soon!";
        case COMMAND_LIST:
            return getTaskListString();
        case COMMAND_MARK:
            return handleMarkResponse(input);
        case COMMAND_UNMARK:
            return handleUnmarkResponse(input);
        case COMMAND_TODO:
            return handleTodoResponse(input);
        case COMMAND_DEADLINE:
            return handleDeadlineResponse(input);
        case COMMAND_EVENT:
            return handleEventResponse(input);
        case COMMAND_DELETE:
            return handleDeleteResponse(input);
        case COMMAND_CLEAR:
            return handleClearResponse();
        case COMMAND_FIND:
            return handleFindResponse(input);
        default:
            if (!input.trim().isEmpty()) {
                throw new EchoException("I don't understand '" + input
                        + "'. Try: todo, deadline, event, list, mark, or unmark.");
            }
            return "";
        }
    }

    /**
     * Returns the task list as a formatted string.
     *
     * @return The formatted task list string.
     */
    private String getTaskListString() {
        if (tasks.size() == 0) {
            return "Your task list is empty!";
        }
        StringBuilder sb = new StringBuilder("Here are the tasks in your list:\n");
        for (int i = 0; i < tasks.size(); i++) {
            sb.append((i + 1)).append(". ").append(tasks.get(i)).append("\n");
        }
        return sb.toString().trim();
    }

    /**
     * Handles the mark command and returns a response string.
     *
     * @param input The user input containing the task number to mark.
     * @return The response string indicating the task was marked.
     * @throws EchoException If the task number is invalid or missing.
     * @throws IOException   If there's an error saving to file.
     */
    private String handleMarkResponse(String input) throws EchoException, IOException {
        if (input.equals(COMMAND_MARK) || Parser.getDescription(input, COMMAND_MARK).isEmpty()) {
            throw new EchoException("Which task should I mark? Use: mark <task number>");
        }
        int taskNum = Parser.getTaskNumber(input) - 1;
        assert taskNum >= -1 : "Task number after parsing should be >= -1";
        if (taskNum < 0 || taskNum >= tasks.size()) {
            throw new EchoException("Task number doesn't exist.");
        }

        assert tasks.get(taskNum) != null : "Task at valid index should not be null";
        tasks.get(taskNum).markDone();
        storage.save(tasks);
        return "Nice! I've marked this task as done:\n  " + tasks.get(taskNum);
    }

    /**
     * Handles the unmark command and returns a response string.
     *
     * @param input The user input containing the task number to unmark.
     * @return The response string indicating the task was unmarked.
     * @throws EchoException If the task number is invalid or missing.
     * @throws IOException   If there's an error saving to file.
     */
    private String handleUnmarkResponse(String input) throws EchoException, IOException {
        if (input.equals(COMMAND_UNMARK) || Parser.getDescription(input, COMMAND_UNMARK).isEmpty()) {
            throw new EchoException("Which task should I unmark? Use: unmark <task number>");
        }
        int taskNum = Parser.getTaskNumber(input) - 1;
        assert taskNum >= -1 : "Task number after parsing should be >= -1";
        if (taskNum < 0 || taskNum >= tasks.size()) {
            throw new EchoException("Task number doesn't exist.");
        }

        assert tasks.get(taskNum) != null : "Task at valid index should not be null";
        tasks.get(taskNum).markNotDone();
        storage.save(tasks);
        return "OK, I've marked this task as not done yet:\n  " + tasks.get(taskNum);
    }

    /**
     * Handles the todo command and returns a response string.
     *
     * @param input The full user input string containing the todo command and description.
     * @return The response string indicating the task was added.
     * @throws EchoException If the task description is empty.
     * @throws IOException   If there's an error saving the new task to file.
     */
    private String handleTodoResponse(String input) throws EchoException, IOException {
        String description = Parser.getDescription(input, COMMAND_TODO);
        if (description.isEmpty()) {
            throw new EchoException("Hmm, you forgot to tell me what the todo is! Try: todo <description>");
        }
        Task task = new Todo(description);
        tasks.add(task);
        storage.save(tasks);
        return "Got it. I've added this task:\n  " + task + "\nNow you have " + tasks.size() + " tasks in the list.";
    }

    /**
     * Handles the deadline command and returns a response string.
     *
     * @param input The full user input string containing the deadline command, description, and date.
     * @return The response string indicating the task was added.
     * @throws EchoException If the description or date is missing or invalid.
     * @throws IOException   If there's an error saving the new task to file.
     */
    private String handleDeadlineResponse(String input) throws EchoException, IOException {
        String description = Parser.getDescription(input, COMMAND_DEADLINE);
        String[] parts = Parser.parseDeadline(description);
        Task task = new Deadline(parts[0], parts[1]);
        tasks.add(task);
        storage.save(tasks);
        return "Got it. I've added this task:\n  " + task + "\nNow you have " + tasks.size() + " tasks in the list.";
    }

    /**
     * Handles the event command and returns a response string.
     *
     * @param input The full user input string containing the event command, description, start and end times.
     * @return The response string indicating the task was added.
     * @throws EchoException If the description, start time, or end time is missing or invalid.
     * @throws IOException   If there's an error saving the new task to file.
     */
    private String handleEventResponse(String input) throws EchoException, IOException {
        String description = Parser.getDescription(input, COMMAND_EVENT);
        String[] parts = Parser.parseEvent(description);
        Task task = new Event(parts[0], parts[1], parts[2]);
        tasks.add(task);
        storage.save(tasks);
        return "Got it. I've added this task:\n  " + task + "\nNow you have " + tasks.size() + " tasks in the list.";
    }

    /**
     * Handles the delete command and returns a response string.
     *
     * @param input The full user input string containing the delete command and task number.
     * @return The response string indicating the task was deleted.
     * @throws EchoException If the task number is invalid, missing, or out of bounds.
     * @throws IOException   If there's an error saving the updated task list to file.
     */
    private String handleDeleteResponse(String input) throws EchoException, IOException {
        if (input.equals(COMMAND_DELETE) || Parser.getDescription(input, COMMAND_DELETE).isEmpty()) {
            throw new EchoException("Please specify which task to delete.");
        }
        int taskNum = Parser.getTaskNumber(input) - 1;
        if (taskNum < 0 || taskNum >= tasks.size()) {
            throw new EchoException("Task number doesn't exist.");
        }
        Task removedTask = tasks.remove(taskNum);
        storage.save(tasks);
        return "Noted. I've removed this task:\n  " + removedTask + "\nNow you have "
                + tasks.size() + " tasks in the list.";
    }

    /**
     * Handles the clear command and returns a response string.
     *
     * @return The response string indicating all tasks were cleared.
     * @throws IOException If there's an error saving the empty task list to file.
     */
    private String handleClearResponse() throws IOException {
        tasks.clear();
        storage.save(tasks);
        return "All tasks have been cleared!";
    }

    /**
     * Handles the find command and returns a response string.
     *
     * @param input The full user input string containing the find command and keyword.
     * @return The response string with matching tasks or a message if none found.
     * @throws EchoException If the keyword is missing.
     */
    private String handleFindResponse(String input) throws EchoException {
        String keyword = Parser.getDescription(input, COMMAND_FIND);
        if (keyword.isEmpty()) {
            throw new EchoException("Please provide a keyword to search for. Use: find <keyword>");
        }
        TaskList matchingTasks = tasks.findTasks(keyword);
        if (matchingTasks.size() == 0) {
            return "No matching tasks found.";
        }
        StringBuilder sb = new StringBuilder("Here are the matching tasks in your list:\n");
        for (int i = 0; i < matchingTasks.size(); i++) {
            sb.append((i + 1)).append(". ").append(matchingTasks.get(i)).append("\n");
        }
        return sb.toString().trim();
    }
}
