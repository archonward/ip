package nebula.ui;

import nebula.tasks.Task;

import java.util.ArrayList;
import java.util.Scanner;

public class Ui {
    private static final String WELCOME_MESSAGE =
            " Hello! I'm Nebula.Nebula\n What can I do for you?";
    private static final String BYE_MESSAGE =
            " Bye. Hope to see you again soon!";
    private static final String EMPTY_LIST_MESSAGE =
            " Your list is empty.";
    private static final String TASK_LIST_HEADER =
            " Here are your stored items:";
    private static final String MARKED_PREFIX =
            " Marked as done: ";
    private static final String UNMARKED_PREFIX =
            " Marked as not done: ";
    private static final String DELETED_PREFIX =
            " Deleted: ";
    private static final String ADDED_PREFIX =
            " Added: ";
    private static final String ERROR_PREFIX = " ";


    /**
     * Shows the welcome message at application startup.
     */
    public void showWelcome() {
        System.out.println(WELCOME_MESSAGE);
    }

    /**
     * Reads the next command line from the user.
     *
     * @param sc Scanner used to read user input.
     * @return User command with leading and trailing whitespace removed.
     */
    public String readCommand(Scanner sc) {
        return sc.nextLine().trim();
    }

    /**
     * Shows the goodbye message before the application exits.
     */
    public void showBye() {
        System.out.println(BYE_MESSAGE);
    }

    /**
     * Shows the current list of tasks in a numbered format.
     * If the list is empty, an empty-list message is shown.
     *
     * @param tasks Tasks to display.
     */
    public void showTaskList(ArrayList<Task> tasks) {
        if (tasks.isEmpty()) {
            System.out.println(EMPTY_LIST_MESSAGE);
            return;
        }
        System.out.println(TASK_LIST_HEADER);
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + ". " + tasks.get(i));
        }
    }

    /**
     * Shows an error message to the user.
     *
     * @param message Error message to display.
     */
    public void showError(String message) {
        System.out.println(ERROR_PREFIX + message);
    }

    /**
     * Shows a message indicating that a task has been added.
     *
     * @param description Description of the task that was added.
     */
    public void showAdded(String description) {
        System.out.println(ADDED_PREFIX + description);
    }

    /**
     * Shows a message indicating that a task has been marked as done.
     *
     * @param task Task that was marked as done.
     */
    public void showMarked(Task task) {
        System.out.println(MARKED_PREFIX + task);
    }

    /**
     * Shows a message indicating that a task has been marked as not done.
     *
     * @param task Task that was marked as not done.
     */
    public void showUnmarked(Task task) {
        System.out.println(UNMARKED_PREFIX + task);
    }

    /**
     * Shows a message indicating that a task has been deleted.
     *
     * @param task Task that was deleted.
     */
    public void showDeleted(Task task) {
        System.out.println(DELETED_PREFIX + task);
    }

    public void showFoundTasks(ArrayList<Task> matches) {
        System.out.println("____________________________________________________________");
        System.out.println(" Here are the matching tasks in your list:");

        for (int i = 0; i < matches.size(); i++) {
            System.out.println(" " + (i + 1) + "." + matches.get(i));
        }
    }

    public String getWelcomeMessage() {
        return WELCOME_MESSAGE;
    }

    public String getByeMessage() {
        return BYE_MESSAGE;
    }

    public String getTaskListMessage(ArrayList<Task> tasks) {
        if (tasks.isEmpty()) {
            return EMPTY_LIST_MESSAGE;
        }
        StringBuilder sb = new StringBuilder(TASK_LIST_HEADER + "\n");
        for (int i = 0; i < tasks.size(); i++) {
            sb.append(" ").append(i + 1).append(". ").append(tasks.get(i)).append("\n");
        }
        return sb.toString().trim();
    }

    public String getErrorMessage(String message) {
        return ERROR_PREFIX + message;
    }

    public String getAddedMessage(Task task, int currentSize) {
        return ADDED_PREFIX + task + "\n Now you have " + currentSize + " tasks in the list.";
    }

    public String getMarkedMessage(Task task) {
        return MARKED_PREFIX + task;
    }

    public String getUnmarkedMessage(Task task) {
        return UNMARKED_PREFIX + task;
    }

    public String getDeletedMessage(Task task, int currentSize) {
        return DELETED_PREFIX + task + "\n Now you have " + currentSize + " tasks in the list.";
    }

    public String getFoundTasksMessage(ArrayList<Task> matches) {
        if (matches.isEmpty()) {
            return " No matching tasks found.";
        }
        StringBuilder sb = new StringBuilder(" Here are the matching tasks in your list:\n");
        for (int i = 0; i < matches.size(); i++) {
            sb.append(" ").append(i + 1).append(". ").append(matches.get(i)).append("\n");
        }
        return sb.toString().trim();
    }

}
