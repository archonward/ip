import java.util.ArrayList;
import java.util.Scanner;
import java.time.format.DateTimeParseException;

public class Nebula {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        ArrayList<Task> tasks = Storage.load();

        Ui ui = new Ui();
        ui.showWelcome();

        while (true) {
            String input = ui.readCommand(sc);

            try {
                if (input.equals("bye")) {
                    ui.showBye();
                    break;
                }

                if (input.equals("list")) {
                    ui.showTaskList(tasks);
                    continue;
                }

                if (input.startsWith("mark ")) {
                    int idx = parseIndex(input.substring(5), tasks.size());
                    tasks.get(idx).markDone();
                    ui.showMarked(tasks.get(idx));
                    Storage.save(tasks);
                    continue;
                }

                if (input.startsWith("unmark ")) {
                    int idx = parseIndex(input.substring(7), tasks.size());
                    tasks.get(idx).markNotDone();
                    ui.showUnmarked(tasks.get(idx));
                    Storage.save(tasks);
                    continue;
                }

                // Level 6: delete
                if (input.startsWith("delete ")) {
                    int idx = parseIndex(input.substring(7), tasks.size());
                    Task removed = tasks.remove(idx);
                    ui.showDeleted(removed);
                    Storage.save(tasks);
                    continue;
                }

                // todo
                if (input.equals("todo")) {
                    throw new NebulaException("Todo needs a description. Try: todo read book");
                }
                if (input.startsWith("todo ")) {
                    String desc = input.substring(5).trim();
                    if (desc.isEmpty()) {
                        throw new NebulaException("Todo description cannot be empty.");
                    }
                    tasks.add(new Todo(desc));
                    System.out.println(" Added: " + desc);
                    Storage.save(tasks);
                    continue;
                }

                // deadline
                if (input.equals("deadline")) {
                    throw new NebulaException("Deadline needs details. Try: deadline submit report /by Friday 5pm");
                }
                if (input.startsWith("deadline ")) {
                    String rest = input.substring(9).trim();
                    int byPos = rest.indexOf(" /by ");
                    if (byPos == -1) {
                        throw new NebulaException("Deadline must include /by. Example: deadline submit report /by Friday 5pm");
                    }

                    String desc = rest.substring(0, byPos).trim();
                    String by = rest.substring(byPos + 5).trim();

                    if (desc.isEmpty()) throw new NebulaException("Deadline description cannot be empty.");
                    if (by.isEmpty()) throw new NebulaException("Deadline time cannot be empty after /by.");

                    tasks.add(new Deadline(desc, Deadline.parseDate(by)));
                    System.out.println(" Added: " + desc);
                    Storage.save(tasks);
                    continue;
                }

                // event
                if (input.equals("event")) {
                    throw new NebulaException("Event needs details. Try: event meeting /from Mon 2pm /to Mon 4pm");
                }
                if (input.startsWith("event ")) {
                    String rest = input.substring(6).trim();
                    int fromPos = rest.indexOf(" /from ");
                    int toPos = rest.indexOf(" /to ");

                    if (fromPos == -1 || toPos == -1 || toPos < fromPos) {
                        throw new NebulaException("Event must include /from and /to. Example: event meeting /from Mon 2pm /to Mon 4pm");
                    }

                    String desc = rest.substring(0, fromPos).trim();
                    String from = rest.substring(fromPos + 7, toPos).trim();
                    String to = rest.substring(toPos + 5).trim();

                    if (desc.isEmpty()) throw new NebulaException("Event description cannot be empty.");
                    if (from.isEmpty() || to.isEmpty()) throw new NebulaException("Event time cannot be empty after /from or /to.");

                    tasks.add(new Event(desc, from, to));
                    System.out.println(" Added: " + desc);
                    Storage.save(tasks);
                    continue;
                }

                throw new NebulaException("I don't recognise that command. Try: todo, deadline, event, list, mark, unmark, delete, bye");

            } catch (NebulaException e) {
                ui.showError(e.getMessage());
            }
        }

        sc.close();
    }

    private static int parseIndex(String s, int size) throws NebulaException {
        try {
            int idx = Integer.parseInt(s.trim()) - 1;
            if (idx < 0 || idx >= size) {
                throw new NebulaException("That task number doesn't exist.");
            }
            return idx;
        } catch (NumberFormatException e) {
            throw new NebulaException("Task number must be a valid integer.");
        }
    }
}
