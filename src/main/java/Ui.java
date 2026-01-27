import java.util.Scanner;

public class Ui {
    private final Scanner scanner;

    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    public void showWelcome() {
        String logo = """
                ____________________________________________________________
                 Hello! I'm Echo
                 What can I do for you?
                ____________________________________________________________""";
        System.out.println(logo);
    }

    public void showGoodbye() {
        String message = """
                ____________________________________________________________
                 Bye. Hope to see you again soon!
                ____________________________________________________________
                """;
        System.out.println(message);
    }

    public void showLine() {
        System.out.println("____________________________________________________________");
    }

    public void showError(String message) {
        System.out.println("____________________________________________________________");
        System.out.println(" " + message);
        System.out.println("____________________________________________________________");
    }

    public void showLoadingError() {
        System.out.println("No previous data found. Starting fresh!\n");
    }

    public void showTaskAdded(Task task, int taskCount) {
        System.out.println("____________________________________________________________");
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + taskCount + " tasks in the list.");
        System.out.println("____________________________________________________________");
    }

    public void showTaskDeleted(Task task, int taskCount) {
        System.out.println("____________________________________________________________");
        System.out.println(" Noted. I've removed this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + taskCount + " tasks in the list.");
        System.out.println("____________________________________________________________");
    }

    public void showTaskMarked(Task task, boolean isDone) {
        System.out.println("____________________________________________________________");
        if (isDone) {
            System.out.println(" Nice! I've marked this task as done:");
        } else {
            System.out.println(" OK, I've marked this task as not done yet:");
        }
        System.out.println("   " + task);
        System.out.println("____________________________________________________________");
    }

    public void showTaskList(TaskList tasks) {
        System.out.println("____________________________________________________________");
        System.out.println(" Here are the tasks in your list:\n");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + "." + tasks.get(i));
        }
        System.out.println("____________________________________________________________\n");
    }

    public String readCommand() {
        return scanner.nextLine();
    }

    public void close() {
        scanner.close();
    }
}