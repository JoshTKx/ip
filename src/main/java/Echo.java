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

        while (true){
            input = scanner.nextLine();

            if (input.equals("bye")){
                break;
            } else if (input.equals("list")){
                System.out.println("____________________________________________________________\n" +
                        " Here are the tasks in your list:\n");
                for(int i = 0; i < taskCount; i++){
                    String mark = tasks[i].isDone ? "X" : "";
                    System.out.println(" " + (i+1) + ". [" + mark + "] " + tasks[i].description);
                }
                System.out.println("____________________________________________________________\n");
            } else if (input.startsWith("mark ")) {
                int taskNum = Integer.parseInt(input.substring(5)) - 1;
                tasks[taskNum].isDone = true;
                System.out.println("____________________________________________________________\n" +
                        " Nice! I've marked this task as done:\n");
                System.out.println("   [X] " + tasks[taskNum].description);
                System.out.println("____________________________________________________________\n");

            } else if (input.startsWith("unmark ")) {
                int taskNum = Integer.parseInt(input.substring(7)) - 1;
                tasks[taskNum].isDone = false;
                System.out.println("____________________________________________________________\n" +
                        " Nice! I've marked this task as not done yet:\n");
                System.out.println("   [] " + tasks[taskNum].description);
                System.out.println("____________________________________________________________\n");
            } else {
                tasks[taskCount] = new Task(input);
                taskCount++;

                String response = "____________________________________________________________\n" +
                        "added: " + input + "\n" +
                        "____________________________________________________________\n";
                System.out.println(response);
            }

        }

        System.out.println(end);
    }
}

class Task {
    String description;
    boolean isDone;

    Task(String description) {
        this.description = description;
        this.isDone = false;
    }
}
