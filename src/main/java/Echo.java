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

        while (!input.equals("bye")){
            input = scanner.nextLine();

            String response = "____________________________________________________________\n" +
                    input + "\n" +
                    "____________________________________________________________\n";

            System.out.println(response);
        }

        System.out.println(end);

    }
}
