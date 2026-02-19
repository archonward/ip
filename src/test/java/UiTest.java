import nebula.tasks.Deadline;
import nebula.tasks.Event;
import nebula.tasks.Task;
import nebula.tasks.Todo;
import nebula.ui.Ui;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the Ui class.
 */
public class UiTest {
    private Ui ui;
    private ArrayList<Task> tasks;

    @BeforeEach
    public void setUp() {
        ui = new Ui();
        tasks = new ArrayList<>();
    }

    @Test
    public void getWelcomeMessage_valid_returnsWelcomeMessage() {
        String result = ui.getWelcomeMessage();
        assertTrue(result.contains("Hello! I'm Nebula!"));
        assertTrue(result.contains("What can I do for you"));
    }

    @Test
    public void getByeMessage_valid_returnsByeMessage() {
        String result = ui.getByeMessage();
        assertEquals(" Bye. Hope to see you again soon!", result);
    }

    @Test
    public void getTaskListMessage_emptyList_returnsEmptyMessage() {
        ArrayList<Task> emptyTasks = new ArrayList<>();
        String result = ui.getTaskListMessage(emptyTasks);
        assertEquals(" Your list is empty.", result);
    }

    @Test
    public void getTaskListMessage_singleTask_returnsFormattedList() {
        tasks.add(new Todo("Read book"));
        String result = ui.getTaskListMessage(tasks);

        assertTrue(result.contains("Here are your stored items:"));
        assertTrue(result.contains("1. [T][ ] Read book"));
    }

    @Test
    public void getTaskListMessage_multipleTasks_returnsNumberedList() {
        tasks.add(new Todo("Task 1"));
        tasks.add(new Todo("Task 2"));
        tasks.add(new Deadline("Submit report", LocalDate.of(2026, 2, 20)));

        String result = ui.getTaskListMessage(tasks);

        assertTrue(result.contains("1. [T][ ] Task 1"));
        assertTrue(result.contains("2. [T][ ] Task 2"));
        assertTrue(result.contains("3. [D][ ] Submit report"));
        assertTrue(result.contains("by: Feb 20 2026"));
    }

    @Test
    public void getErrorMessage_valid_returnsPrefixedError() {
        String result = ui.getErrorMessage("Something went wrong");
        assertEquals("  Something went wrong", result);
    }

    @Test
    public void getAddedMessage_todoTask_returnsFormattedMessage() {
        Todo todo = new Todo("Read book");
        String result = ui.getAddedMessage(todo, 1);

        assertTrue(result.contains("Added: [T][ ] Read book"));
        assertTrue(result.contains("Now you have 1 tasks in the list"));
    }

    @Test
    public void getAddedMessage_deadlineTask_returnsFormattedMessage() {
        Deadline deadline = new Deadline("Submit report", LocalDate.of(2026, 2, 20));
        String result = ui.getAddedMessage(deadline, 5);

        assertTrue(result.contains("Added: [D][ ] Submit report"));
        assertTrue(result.contains("by: Feb 20 2026"));
        assertTrue(result.contains("Now you have 5 tasks in the list"));
    }

    @Test
    public void getAddedMessage_eventTask_returnsFormattedMessage() {
        Event event = new Event("Project meeting", "2026-02-20", "2026-02-21");
        String result = ui.getAddedMessage(event, 3);

        assertTrue(result.contains("Added: [E][ ] Project meeting"));
        assertTrue(result.contains("from: Feb 20 2026"));
        assertTrue(result.contains("to: Feb 21 2026"));
        assertTrue(result.contains("Now you have 3 tasks in the list"));
    }

    @Test
    public void getMarkedMessage_todoTask_returnsFormattedMessage() {
        Todo todo = new Todo("Read book");
        todo.markDone();
        String result = ui.getMarkedMessage(todo);
        assertEquals(" Marked as done: [T][X] Read book", result);
    }

    @Test
    public void getUnmarkedMessage_deadlineTask_returnsFormattedMessage() {
        Deadline deadline = new Deadline("Submit report", LocalDate.of(2026, 2, 20));
        String result = ui.getUnmarkedMessage(deadline);
        assertTrue(result.contains("Marked as not done: [D][ ] Submit report"));
    }

    @Test
    public void getDeletedMessage_todoTask_returnsFormattedMessage() {
        Todo todo = new Todo("Read book");
        String result = ui.getDeletedMessage(todo, 2);

        assertTrue(result.contains("Deleted: [T][ ] Read book"));
        assertTrue(result.contains("Now you have 2 tasks in the list"));
    }

    @Test
    public void getDeletedMessage_deadlineTask_returnsFormattedMessage() {
        Deadline deadline = new Deadline("Submit report", LocalDate.of(2026, 2, 20));
        String result = ui.getDeletedMessage(deadline, 0);

        assertTrue(result.contains("Deleted: [D][ ] Submit report"));
        assertTrue(result.contains("Now you have 0 tasks in the list"));
    }

    @Test
    public void getFoundTasksMessage_emptyList_returnsNoMatchesMessage() {
        ArrayList<Task> emptyMatches = new ArrayList<>();
        String result = ui.getFoundTasksMessage(emptyMatches);
        assertEquals(" No matching tasks found.", result);
    }

    @Test
    public void getFoundTasksMessage_singleMatch_returnsFormattedList() {
        ArrayList<Task> matches = new ArrayList<>();
        matches.add(new Todo("Read book"));

        String result = ui.getFoundTasksMessage(matches);

        assertTrue(result.contains("Here are the matching tasks in your list:"));
        assertTrue(result.contains("1. [T][ ] Read book"));
    }

    @Test
    public void getFoundTasksMessage_multipleMatches_returnsNumberedList() {
        ArrayList<Task> matches = new ArrayList<>();
        matches.add(new Todo("Read book"));
        matches.add(new Deadline("Submit book report", LocalDate.of(2026, 2, 20)));

        String result = ui.getFoundTasksMessage(matches);

        assertTrue(result.contains("1. [T][ ] Read book"));
        assertTrue(result.contains("2. [D][ ] Submit book report"));
    }

    @Test
    public void getRescheduledMessage_deadlineTask_returnsFormattedMessage() {
        Deadline deadline = new Deadline("Submit report", LocalDate.of(2026, 2, 20));
        String result = ui.getRescheduledMessage(deadline);
        assertTrue(result.contains("Rescheduled: [D][ ] Submit report"));
    }

    @Test
    public void getRescheduledMessage_eventTask_returnsFormattedMessage() {
        Event event = new Event("Meeting", "2026-02-20", "2026-02-21");
        String result = ui.getRescheduledMessage(event);
        assertTrue(result.contains("Rescheduled: [E][ ] Meeting"));
    }

    @Test
    public void getTaskListMessage_preservesTaskOrder() {
        tasks.add(new Todo("First task"));
        tasks.add(new Todo("Second task"));
        tasks.add(new Todo("Third task"));

        String result = ui.getTaskListMessage(tasks);

        int firstIndex = result.indexOf("First task");
        int secondIndex = result.indexOf("Second task");
        int thirdIndex = result.indexOf("Third task");

        assertTrue(firstIndex < secondIndex);
        assertTrue(secondIndex < thirdIndex);
    }

    @Test
    public void getAddedMessage_handlesLargeTaskCount() {
        Todo todo = new Todo("Task");
        String result = ui.getAddedMessage(todo, 999);
        assertTrue(result.contains("Now you have 999 tasks in the list"));
    }

    @Test
    public void getDeletedMessage_handlesZeroTasks() {
        Todo todo = new Todo("Last task");
        String result = ui.getDeletedMessage(todo, 0);
        assertTrue(result.contains("Now you have 0 tasks in the list"));
    }
}