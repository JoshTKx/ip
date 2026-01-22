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
        String[] tasks = new String[100];
        int taskCount = 0;

        while (true){
            input = scanner.nextLine();

            if (input.equals("bye")){
                break;
            } else if (input.equals("list")){
                System.out.println("____________________________________________________________\n");
                for(int i = 0; i < taskCount; i++){
                    System.out.println((i+1) + ". " + tasks[i]);
                }
                System.out.println("____________________________________________________________\n");
            } else {
                tasks[taskCount] = input;
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
