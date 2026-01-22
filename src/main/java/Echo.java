import java.util.List;
import java.util.Scanner;

public class Echo {
    public static void main(String[] args) {
        String intro = "____________________________________________________________\n" +
                " Hello! I'm Echo\n" +
                " What can I do for you?\n" +
                "____________________________________________________________\n";
        String end = "____________________________________________________________\n" +
                " Bye. Hope to see you again soon!\n" +
                "____________________________________________________________\n";

        System.out.println(intro);

        Scanner scanner = new Scanner(System.in);
        String input = "";
        Task[] tasks = new Task[100];
        int taskCount = 0;


        while (true) {
            input = scanner.nextLine();
            try {
                if (input.equals("bye")) {
                    break;
                } else if (input.equals("list")) {
                    System.out.println("____________________________________________________________\n" +
                            " Here are the tasks in your list:\n");
                    for (int i = 0; i < taskCount; i++) {
                        System.out.println(" " + (i + 1) + "." + tasks[i]);
                    }
                    System.out.println("____________________________________________________________\n");

                } else if (input.equals("mark") || (input.startsWith("mark ") && input.substring(5).trim().isEmpty())) {
                    throw new EchoException("Which task should I mark? Use: mark <task number>");

                } else if (input.startsWith("mark ")) {
                    int taskNum = Integer.parseInt(input.substring(5)) - 1;
                    tasks[taskNum].markDone();
                    System.out.println("____________________________________________________________\n" +
                            " Nice! I've marked this task as done:\n");
                    System.out.println("   [X] " + tasks[taskNum].description);
                    System.out.println("____________________________________________________________\n");

                } else if (input.equals("unmark") || (input.startsWith("unmark ") && input.substring(7).trim().isEmpty())) {
                    throw new EchoException("Which task should I unmark? Use: unmark <task number>");

                } else if (input.startsWith("unmark ")) {
                    int taskNum = Integer.parseInt(input.substring(7)) - 1;
                    tasks[taskNum].markNotDone();
                    System.out.println("____________________________________________________________\n" +
                            " Nice! I've marked this task as not done yet:\n");
                    System.out.println("   [] " + tasks[taskNum].description);
                    System.out.println("____________________________________________________________\n");
                } else if (input.equals("todo") || (input.startsWith("todo ") && input.substring(5).trim().isEmpty())) {
                    throw new EchoException("Hmm, you forgot to tell me what the todo is! Try: todo <description>");

                } else if (input.startsWith("todo ")) {
                    String description = input.substring(5);
                    tasks[taskCount] = new Todo(description);
                    taskCount++;
                    System.out.println("____________________________________________________________");
                    System.out.println(" Got it. I've added this task:");
                    System.out.println("   " + tasks[taskCount - 1]);
                    System.out.println(" Now you have " + taskCount + " tasks in the list.");
                    System.out.println("____________________________________________________________");
                } else if (input.equals("deadline") || (input.startsWith("deadline ") && !input.contains("/by"))) {
                    throw new EchoException("Deadlines need a date! Use: deadline <task> /by <date>");

                }else if (input.startsWith("deadline ")) {
                    String[] parts = input.substring(9).split(" /by ");
                    String description = parts[0];
                    String by = parts[1];
                    tasks[taskCount] = new Deadline(description, by);
                    taskCount++;
                    System.out.println("____________________________________________________________");
                    System.out.println(" Got it. I've added this task:");
                    System.out.println("   " + tasks[taskCount - 1]);
                    System.out.println(" Now you have " + taskCount + " tasks in the list.");
                    System.out.println("____________________________________________________________");

                } else if (input.equals("event") || (input.startsWith("event ") && (!input.contains("/from") || !input.contains("/to")))) {
                    throw new EchoException("Events need start and end times! Use: event <task> /from <time> /to <time>");

                }else if (input.startsWith("event ")) {
                    String[] parts = input.substring(6).split(" /from | /to ");
                    String description = parts[0];
                    String from = parts[1];
                    String to = parts[2];
                    tasks[taskCount] = new Event(description, from, to);
                    taskCount++;
                    System.out.println("____________________________________________________________");
                    System.out.println(" Got it. I've added this task:");
                    System.out.println("   " + tasks[taskCount - 1]);
                    System.out.println(" Now you have " + taskCount + " tasks in the list.");
                    System.out.println("____________________________________________________________");
                } else if (input.trim().isEmpty()) {
                        continue;
                } else {
                    throw new EchoException("I don't understand '" + input + "'. Try: todo, deadline, event, list, mark, or unmark.");
                }

            } catch (EchoException e) {
                System.out.println("____________________________________________________________");
                System.out.println(" " + e.getMessage());
                System.out.println("____________________________________________________________");
            } catch (Exception e) {
                System.out.println("____________________________________________________________");
                System.out.println(" Uh oh! Something unexpected happened: " + e.getMessage());
                System.out.println("____________________________________________________________");
            }



        }
        System.out.println(end);
        scanner.close();
    }
}

