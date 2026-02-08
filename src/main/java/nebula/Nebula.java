package nebula;

import java.util.ArrayList;
import java.util.Scanner;

import nebula.storage.Storage;
import nebula.tasks.Deadline;
import nebula.tasks.Event;
import nebula.tasks.Task;
import nebula.tasks.TaskList;
import nebula.tasks.Todo;
import nebula.ui.Ui;

/**
 * Main entry class for the Nebula task manager application.
 */
public class Nebula {
    private TaskList tasks;
    private final Storage storage;
    private final Ui ui = new Ui();  // Reuse existing Ui for formatting

    // Keep your existing constructor that loads tasks from storage
    public Nebula() {
        storage = new Storage();
        try {
            tasks = new TaskList(storage.load());
        } catch (Exception e) {
            tasks = new TaskList(new ArrayList<>());
        }
    }

    /**
     * Processes user input and returns Nebula's response as a String.
     * Mirrors CLI command parsing but returns strings instead of printing.
     *
     * @param input User command (e.g., "todo read book")
     * @return Formatted response string for GUI display
     */
    public String getResponse(String input) {
        input = input.trim();
        if (input.isEmpty()) {
            return ui.getErrorMessage("The description cannot be empty.");
        }

        try {
            // ===== EXACTLY MIRRORS YOUR CLI PARSING LOGIC =====
            if (input.equals("bye")) {
                return ui.getByeMessage();
            }

            if (input.equals("list")) {
                return ui.getTaskListMessage(tasks.getAll());
            }

            if (input.startsWith("mark ")) {
                int idx = parseIndex(input.substring(5).trim(), tasks.size());
                tasks.mark(idx);
                storage.save(tasks.getAll());
                return ui.getMarkedMessage(tasks.get(idx));
            }

            if (input.startsWith("unmark ")) {
                int idx = parseIndex(input.substring(7).trim(), tasks.size());
                tasks.unmark(idx);
                storage.save(tasks.getAll());
                return ui.getUnmarkedMessage(tasks.get(idx));
            }

            if (input.startsWith("delete ")) {
                int idx = parseIndex(input.substring(7).trim(), tasks.size());
                Task removed = tasks.delete(idx);
                storage.save(tasks.getAll());
                return ui.getDeletedMessage(removed, tasks.size());
            }

            if (input.startsWith("todo ")) {
                String desc = input.substring(5).trim();
                if (desc.isEmpty()) {
                    throw new NebulaException("Todo description cannot be empty.");
                }
                Todo todo = new Todo(desc);
                tasks.add(todo);
                storage.save(tasks.getAll());
                return ui.getAddedMessage(todo, tasks.size());
            }

            if (input.startsWith("deadline ")) {
                String rest = input.substring(9).trim();
                int byPos = rest.indexOf(" /by ");
                if (byPos == -1) {
                    throw new NebulaException("Deadline must include /by. Example: deadline submit report /by 2026-02-10");
                }
                String desc = rest.substring(0, byPos).trim();
                String by = rest.substring(byPos + 5).trim();
                if (desc.isEmpty() || by.isEmpty()) {
                    throw new NebulaException("Deadline description and date cannot be empty.");
                }
                Deadline deadline = new Deadline(desc, Deadline.parseDate(by));
                tasks.add(deadline);
                storage.save(tasks.getAll());
                return ui.getAddedMessage(deadline, tasks.size());
            }

            if (input.startsWith("event ")) {
                String rest = input.substring(6).trim();
                int fromPos = rest.indexOf(" /from ");
                int toPos = rest.indexOf(" /to ");
                if (fromPos == -1 || toPos == -1 || toPos < fromPos) {
                    throw new NebulaException("Event must include /from and /to. Example: event meeting /from 2026-02-10 /to 2026-02-11");
                }
                String desc = rest.substring(0, fromPos).trim();
                String from = rest.substring(fromPos + 7, toPos).trim();
                String to = rest.substring(toPos + 5).trim();
                if (desc.isEmpty() || from.isEmpty() || to.isEmpty()) {
                    throw new NebulaException("Event description and times cannot be empty.");
                }
                Event event = new Event(desc, from, to);
                tasks.add(event);
                storage.save(tasks.getAll());
                return ui.getAddedMessage(event, tasks.size());
            }

            if (input.startsWith("find ")) {
                String keyword = input.substring(5).trim();
                ArrayList<Task> matches = tasks.find(keyword);
                return ui.getFoundTasksMessage(matches);
            }

            // Unknown command
            return ui.getErrorMessage("I don't recognise that command. Try: todo, deadline, event, list, mark, unmark, delete, bye");

        } catch (Exception e) {
            return ui.getErrorMessage(e.getMessage());
        }
    }

    private int parseIndex(String s, int size) throws NebulaException {
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

    public static void main(String[] args) {
        // Your current CLI loop remains 100% intact
        Scanner sc = new Scanner(System.in);
        TaskList taskList = new TaskList(Storage.load());
        Ui ui = new Ui();
        ui.showWelcome();
    }
}