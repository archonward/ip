import java.util.ArrayList;
import java.util.Scanner;

public class Ui {
    private static final String WELCOME_MESSAGE =
            " Hello! I'm Nebula\n What can I do for you?";
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



    public void showWelcome() {
        System.out.println(WELCOME_MESSAGE);
    }

    public String readCommand(Scanner sc) {
        return sc.nextLine().trim();
    }

    public void showBye() {
        System.out.println(BYE_MESSAGE);
    }

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

    public void showError(String message) {
        System.out.println(ERROR_PREFIX + message);
    }

    public void showAdded(String description) {
        System.out.println(ADDED_PREFIX + description);
    }

    public void showMarked(Task task) {
        System.out.println(MARKED_PREFIX + task);
    }

    public void showUnmarked(Task task) {
        System.out.println(UNMARKED_PREFIX + task);
    }

    public void showDeleted(Task task) {
        System.out.println(DELETED_PREFIX + task);
    }
}
