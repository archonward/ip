import java.util.Scanner;

public class Nebula {
    private static final int MAX_ITEMS = 100;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        Task[] tasks = new Task[MAX_ITEMS];
        int count = 0;

        System.out.println(" Hello! I'm Nebula");
        System.out.println(" What can I do for you?");

        while (true) {
            String input = sc.nextLine().trim();

            if (input.equals("bye")) {
                System.out.println(" Bye. Hope to see you again soon!");
                break;
            }

            if (input.startsWith("todo ")) {
                if (count >= MAX_ITEMS) {
                    System.out.println("I can't store more than " + MAX_ITEMS + " tasks.");
                    continue;
                }
                String desc = input.substring(5).trim();
                tasks[count++] = new Todo(desc);
                System.out.println(" Added: " + desc);
                continue;
            }

            if (input.startsWith("deadline ")) {
                if (count >= MAX_ITEMS) {
                    System.out.println("I can't store more than " + MAX_ITEMS + " tasks.");
                    continue;
                }

                String rest = input.substring(9).trim(); // after "deadline "
                int byPos = rest.indexOf(" /by ");
                String desc = (byPos == -1) ? rest : rest.substring(0, byPos).trim();
                String by = (byPos == -1) ? "" : rest.substring(byPos + 5).trim();

                tasks[count++] = new Deadline(desc, by);
                System.out.println(" Added: " + desc);
                continue;
            }

            if (input.startsWith("event ")) {
                if (count >= MAX_ITEMS) {
                    System.out.println("I can't store more than " + MAX_ITEMS + " tasks.");
                    continue;
                }

                String rest = input.substring(6).trim(); // after "event "
                int fromPos = rest.indexOf(" /from ");
                int toPos = rest.indexOf(" /to ");

                String desc;
                String from = "";
                String to = "";

                if (fromPos == -1 || toPos == -1 || toPos < fromPos) {
                    desc = rest;
                } else {
                    desc = rest.substring(0, fromPos).trim();
                    from = rest.substring(fromPos + 7, toPos).trim();
                    to = rest.substring(toPos + 5).trim();
                }

                tasks[count++] = new Event(desc, from, to);
                System.out.println(" Added: " + desc);
                continue;
            }

            if (input.equals("list")) {
                if (count == 0) {
                    System.out.println(" Your list is empty.");
                } else {
                    System.out.println(" Here are your stored items:");
                    for (int i = 0; i < count; i++) {
                        System.out.println(" " + (i + 1) + ". " + tasks[i]);
                    }
                }
                continue;
            }

            if (input.startsWith("mark ")) {
                int idx = Integer.parseInt(input.substring(5).trim()) - 1;
                if (idx >= 0 && idx < count) {
                    tasks[idx].markDone(); // polymorphism: Task method works for all types
                    System.out.println(" Marked as done: " + tasks[idx]);
                }
                continue;
            }

            if (input.startsWith("unmark ")) {
                int idx = Integer.parseInt(input.substring(7).trim()) - 1;
                if (idx >= 0 && idx < count) {
                    tasks[idx].markNotDone();
                    System.out.println(" Marked as not done: " + tasks[idx]);
                }
                continue;
            }

            System.out.println(input);
        }

        sc.close();
    }
}
