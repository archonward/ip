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
 * Orchestrates task management operations and provides responses for both CLI and GUI modes.
 */
public class Nebula {
    private TaskList tasks;
    private final Storage storage;
    private final Ui ui;

    /**
     * Constructs a Nebula instance with tasks loaded from storage.
     */
    public Nebula() {
        storage = new Storage();
        ui = new Ui();

        try {
            tasks = new TaskList(storage.load());
        } catch (Exception e) {
            tasks = new TaskList(new ArrayList<>());
        }

        assert tasks != null : "TaskList must be initialized";
    }

    /**
     * Processes user input and returns Nebula's response as a String.
     * Delegates command handling to specialized private methods.
     *
     * @param input User command (e.g., "todo read book")
     * @return Formatted response string for display
     */
    public String getResponse(String input) {
        assert input != null : "Input must not be null";
        input = input.trim();

        if (input.isEmpty()) {
            return ui.getErrorMessage("The description cannot be empty.");
        }

        try {
            // Route commands to specialized handlers
            if (input.equals("bye")) {
                return handleBye();
            } else if (input.equals("list")) {
                return handleList();
            } else if (input.startsWith("mark ")) {
                return handleMark(input.substring(5).trim());
            } else if (input.startsWith("unmark ")) {
                return handleUnmark(input.substring(7).trim());
            } else if (input.startsWith("delete ")) {
                return handleDelete(input.substring(7).trim());
            } else if (input.startsWith("todo ")) {
                return handleTodo(input.substring(5).trim());
            } else if (input.startsWith("deadline ")) {
                return handleDeadline(input.substring(9).trim());
            } else if (input.startsWith("event ")) {
                return handleEvent(input.substring(6).trim());
            } else if (input.startsWith("find ")) {
                return handleFind(input.substring(5).trim());
            } else {
                return ui.getErrorMessage("I don't recognise that command. "
                        + "Try: todo, deadline, event, list, mark, unmark, delete, find, bye");
            }
        } catch (Exception e) {
            return ui.getErrorMessage(e.getMessage());
        }
    }

    // ===== COMMAND HANDLERS (single responsibility, testable units) =====

    private String handleBye() {
        return ui.getByeMessage();
    }

    private String handleList() {
        return ui.getTaskListMessage(tasks.getAll());
    }

    private String handleMark(String args) throws NebulaException {
        int index = parseIndex(args, tasks.size());
        Task taskBefore = tasks.get(index);
        tasks.mark(index);
        storage.save(tasks.getAll());

        assert tasks.get(index).isDone() : "Task should be marked done after mark()";
        assert taskBefore.getDescription().equals(tasks.get(index).getDescription())
                : "Task description should not change after marking";

        return ui.getMarkedMessage(tasks.get(index));
    }

    private String handleUnmark(String args) throws NebulaException {
        int index = parseIndex(args, tasks.size());
        Task taskBefore = tasks.get(index);
        tasks.unmark(index);
        storage.save(tasks.getAll());

        assert !tasks.get(index).isDone() : "Task should be unmarked after unmark()";
        assert taskBefore.getDescription().equals(tasks.get(index).getDescription())
                : "Task description should not change after unmarking";

        return ui.getUnmarkedMessage(tasks.get(index));
    }

    private String handleDelete(String args) throws NebulaException {
        int index = parseIndex(args, tasks.size());
        int sizeBefore = tasks.size();
        Task removed = tasks.delete(index);
        storage.save(tasks.getAll());

        assert tasks.size() == sizeBefore - 1 : "Task count must decrease by 1 after deletion";
        assert removed != null : "Deleted task must not be null";

        return ui.getDeletedMessage(removed, tasks.size());
    }

    private String handleTodo(String description) throws NebulaException {
        if (description.isEmpty()) {
            throw new NebulaException("Todo description cannot be empty.");
        }

        Todo todo = new Todo(description);
        tasks.add(todo);
        storage.save(tasks.getAll());

        assert tasks.get(tasks.size() - 1) == todo : "New todo must be last in list";

        return ui.getAddedMessage(todo, tasks.size());
    }

    private String handleDeadline(String args) throws NebulaException {
        int byPos = args.indexOf(" /by ");
        if (byPos == -1) {
            throw new NebulaException("Deadline must include /by. "
                    + "Example: deadline submit report /by 2026-02-10");
        }

        String description = args.substring(0, byPos).trim();
        String byDate = args.substring(byPos + 5).trim();

        if (description.isEmpty()) {
            throw new NebulaException("Deadline description cannot be empty.");
        }
        if (byDate.isEmpty()) {
            throw new NebulaException("Deadline date cannot be empty after /by.");
        }

        Deadline deadline = new Deadline(description, Deadline.parseDate(byDate));
        tasks.add(deadline);
        storage.save(tasks.getAll());

        return ui.getAddedMessage(deadline, tasks.size());
    }

    private String handleEvent(String args) throws NebulaException {
        int fromPos = args.indexOf(" /from ");
        int toPos = args.indexOf(" /to ");

        if (fromPos == -1 || toPos == -1 || toPos <= fromPos) {
            throw new NebulaException("Event must include /from and /to with valid ordering. "
                    + "Example: event meeting /from 2026-02-10 /to 2026-02-11");
        }

        String description = args.substring(0, fromPos).trim();
        String from = args.substring(fromPos + 7, toPos).trim();
        String to = args.substring(toPos + 5).trim();

        if (description.isEmpty()) {
            throw new NebulaException("Event description cannot be empty.");
        }
        if (from.isEmpty() || to.isEmpty()) {
            throw new NebulaException("Event times cannot be empty after /from or /to.");
        }

        Event event = new Event(description, from, to);
        tasks.add(event);
        storage.save(tasks.getAll());

        return ui.getAddedMessage(event, tasks.size());
    }

    private String handleFind(String keyword) {
        ArrayList<Task> matches = tasks.find(keyword.trim());
        return ui.getFoundTasksMessage(matches);
    }

    // ===== UTILITY METHODS =====

    private int parseIndex(String s, int size) throws NebulaException {
        try {
            int index = Integer.parseInt(s.trim()) - 1;
            assert index >= -1 : "Parsed index should not be less than -1";

            if (index < 0 || index >= size) {
                throw new NebulaException("That task number doesn't exist.");
            }
            return index;
        } catch (NumberFormatException e) {
            throw new NebulaException("Task number must be a valid integer.");
        }
    }

    // ===== CLI ENTRY POINT (preserved for backward compatibility) =====

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Nebula nebula = new Nebula();
        nebula.ui.showWelcome();

        while (true) {
            String input = nebula.ui.readCommand(scanner);
            String response = nebula.getResponse(input);
            System.out.println(response);

            if (input.trim().equals("bye")) {
                break;
            }
        }

        scanner.close();
    }
}