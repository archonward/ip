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

            try {
                if (input.equals("bye")) {
                    System.out.println(" Bye. Hope to see you again soon!");
                    break;
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
                    int idx = parseIndex(input.substring(5), count);
                    tasks[idx].markDone();
                    System.out.println(" Marked as done: " + tasks[idx]);
                    continue;
                }

                if (input.startsWith("unmark ")) {
                    int idx = parseIndex(input.substring(7), count);
                    tasks[idx].markNotDone();
                    System.out.println(" Marked as not done: " + tasks[idx]);
                    continue;
                }

                if (input.equals("todo")) {
                    throw new NebulaException("Todo needs a description. Try: todo read book");
                }

                if (input.startsWith("todo ")) {
                    ensureSpace(tasks, count);

                    String desc = input.substring(5).trim();
                    if (desc.isEmpty()) {
                        throw new NebulaException("Todo description cannot be empty.");
                    }

                    tasks[count++] = new Todo(desc);
                    System.out.println(" Added: " + desc);
                    continue;
                }

                if (input.equals("deadline")) {
                    throw new NebulaException("Deadline needs details. Try: deadline submit report /by 11/10/2019 5pm");
                }

                if (input.startsWith("deadline ")) {
                    ensureSpace(tasks, count);

                    String rest = input.substring(9).trim();
                    int byPos = rest.indexOf(" /by ");
                    if (byPos == -1) {
                        throw new NebulaException("Deadline must include /by. Example: deadline submit report /by Friday 5pm");
                    }

                    String desc = rest.substring(0, byPos).trim();
                    String by = rest.substring(byPos + 5).trim();

                    if (desc.isEmpty()) {
                        throw new NebulaException("Deadline description cannot be empty.");
                    }
                    if (by.isEmpty()) {
                        throw new NebulaException("Deadline time cannot be empty after /by.");
                    }

                    tasks[count++] = new Deadline(desc, by);
                    System.out.println(" Added: " + desc);
                    continue;
                }

                if (input.equals("event")) {
                    throw new NebulaException("Event needs details. Try: event meeting /from Mon 2pm /to Mon 4pm");
                }

                if (input.startsWith("event ")) {
                    ensureSpace(tasks, count);

                    String rest = input.substring(6).trim();
                    int fromPos = rest.indexOf(" /from ");
                    int toPos = rest.indexOf(" /to ");

                    if (fromPos == -1 || toPos == -1 || toPos < fromPos) {
                        throw new NebulaException("Event must include /from and /to. Example: event meeting /from Mon 2pm /to Mon 4pm");
                    }

                    String desc = rest.substring(0, fromPos).trim();
                    String from = rest.substring(fromPos + 7, toPos).trim();
                    String to = rest.substring(toPos + 5).trim();

                    if (desc.isEmpty()) {
                        throw new NebulaException("Event description cannot be empty.");
                    }
                    if (from.isEmpty() || to.isEmpty()) {
                        throw new NebulaException("Event time cannot be empty after /from or /to.");
                    }

                    tasks[count++] = new Event(desc, from, to);
                    System.out.println(" Added: " + desc);
                    continue;
                }

                // Unknown command
                throw new NebulaException("I don't recognise that command. Try: todo, deadline, event, list, mark, unmark, bye");

            } catch (NebulaException e) {
                System.out.println(" " + e.getMessage());
            }
        }

        sc.close();
    }

    private static void ensureSpace(Task[] tasks, int count) throws NebulaException {
        if (count >= MAX_ITEMS) {
            throw new NebulaException("Task list is full. Max is " + MAX_ITEMS + ".");
        }
    }

    private static int parseIndex(String s, int count) throws NebulaException {
        try {
            int idx = Integer.parseInt(s.trim()) - 1;
            if (idx < 0 || idx >= count) {
                throw new NebulaException("That task number doesn't exist.");
            }
            return idx;
        } catch (NumberFormatException e) {
            throw new NebulaException("Task number must be a valid integer.");
        }
    }
}
