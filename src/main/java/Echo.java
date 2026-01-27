import java.io.IOException;

public class Echo {
    private static final String FILE_PATH = "./data/echo.txt";

    private Storage storage;
    private TaskList tasks;
    private Ui ui;

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
                    throw new EchoException("I don't understand '" + input + "'. Try: todo, deadline, event, list, mark, or unmark.");
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

    private void handleMark(String input) throws EchoException, IOException {
        if (input.equals("mark") || Parser.getDescription(input, "mark").isEmpty()) {
            throw new EchoException("Which task should I mark? Use: mark <task number>");
        }
        int taskNum = Parser.getTaskNumber(input) - 1;
        if (taskNum < 0 || taskNum >= tasks.size()) {
            throw new EchoException("Task number doesn't exist.");
        }
        tasks.get(taskNum).markDone();
        storage.save(tasks.getTasks());
        ui.showTaskMarked(tasks.get(taskNum), true);
    }

    private void handleUnmark(String input) throws EchoException, IOException {
        if (input.equals("unmark") || Parser.getDescription(input, "unmark").isEmpty()) {
            throw new EchoException("Which task should I unmark? Use: unmark <task number>");
        }
        int taskNum = Parser.getTaskNumber(input) - 1;
        if (taskNum < 0 || taskNum >= tasks.size()) {
            throw new EchoException("Task number doesn't exist.");
        }
        tasks.get(taskNum).markNotDone();
        storage.save(tasks.getTasks());
        ui.showTaskMarked(tasks.get(taskNum), false);
    }

    private void handleTodo(String input) throws EchoException, IOException {
        String description = Parser.getDescription(input, "todo");
        if (description.isEmpty()) {
            throw new EchoException("Hmm, you forgot to tell me what the todo is! Try: todo <description>");
        }
        Task task = new Todo(description);
        tasks.add(task);
        storage.save(tasks.getTasks());
        ui.showTaskAdded(task, tasks.size());
    }

    private void handleDeadline(String input) throws EchoException, IOException {
        String description = Parser.getDescription(input, "deadline");
        String[] parts = Parser.parseDeadline(description);
        Task task = new Deadline(parts[0], parts[1]);
        tasks.add(task);
        storage.save(tasks.getTasks());
        ui.showTaskAdded(task, tasks.size());
    }

    private void handleEvent(String input) throws EchoException, IOException {
        String description = Parser.getDescription(input, "event");
        String[] parts = Parser.parseEvent(description);
        Task task = new Event(parts[0], parts[1], parts[2]);
        tasks.add(task);
        storage.save(tasks.getTasks());
        ui.showTaskAdded(task, tasks.size());
    }

    private void handleDelete(String input) throws EchoException, IOException {
        if (input.equals("delete") || Parser.getDescription(input, "delete").isEmpty()) {
            throw new EchoException("Please specify which task to delete.");
        }
        int taskNum = Parser.getTaskNumber(input) - 1;
        if (taskNum < 0 || taskNum >= tasks.size()) {
            throw new EchoException("Task number doesn't exist.");
        }
        Task removedTask = tasks.remove(taskNum);
        storage.save(tasks.getTasks());
        ui.showTaskDeleted(removedTask, tasks.size());
    }

    private void handleClear() throws IOException {
        tasks.getTasks().clear();
        storage.save(tasks.getTasks());
        ui.showLine();
        System.out.println(" All tasks have been cleared!");
        ui.showLine();
    }

    public static void main(String[] args) {
        new Echo(FILE_PATH).run();
    }
}
