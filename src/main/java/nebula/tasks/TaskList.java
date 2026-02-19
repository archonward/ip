package nebula.tasks;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class TaskList {
    private final ArrayList<Task> tasks;

    /** Create an empty task list */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a task list backed by the given tasks.
     *
     * @param tasks Existing tasks to use as the internal list.
     */
    public TaskList(ArrayList<Task> tasks) {
        assert tasks != null : "Task list must not be null";
        this.tasks = tasks;
    }

    /**
     * Adds a task to this task list.
     *
     * @param task Task to add.
     */
    public void add(Task task) {
        assert task != null : "Cannot add null task";
        tasks.add(task);
    }

    /**
     * Deletes and returns the task at the specified 0-based index.
     *
     * @param index 0-based index of the task to delete.
     * @return The deleted task.
     * @throws IndexOutOfBoundsException If the index is out of range.
     */
    public Task delete(int index) {
        validateIndex(index);
        int sizeBefore = tasks.size();
        Task removed = tasks.remove(index);

        assert tasks.size() == sizeBefore - 1 : "Task count must decrease by 1 after deletion";
        assert removed != null : "Removed task must not be null";
        return removed;
    }

    /** Mark task at 0-based index as done */
    public void mark(int index) {
        validateIndex(index);
        Task task = tasks.get(index);
        boolean wasDone = task.isDone();
        task.markDone();

        assert task.isDone() : "Task must be marked done after markDone()";
        assert wasDone != task.isDone() || wasDone : "Task state should change unless already done";
    }

    /** Mark task at 0-based index as not done */
    public void unmark(int index) {
        validateIndex(index);
        Task task = tasks.get(index);
        boolean wasNotDone = !task.isDone();
        task.markNotDone();

        // Postcondition: task must be unmarked
        assert !task.isDone() : "Task must be unmarked after markNotDone()";
        assert wasNotDone != !task.isDone() || wasNotDone : "Task state should change unless already not done";
    }

    /** Get task at 0-based index */
    public Task get(int index) {
        validateIndex(index);
        Task task = tasks.get(index);

        // ✅ Invariant: tasks in list must never be null
        assert task != null : "Task at index " + index + " must not be null";
        return task;
    }


    /** Get total number of tasks */
    public int size() {
        return tasks.size();
    }

    /** Check if list is empty */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    /** Get all tasks (for display/storage)sd */
    public ArrayList<Task> getAll() {
        // Invariant: returned list reference must match internal list
        assert tasks != null : "Internal task list must not be null";
        return tasks;
    }

    // Use streams here to improve readability
    public ArrayList<Task> find(String keyword) {
        return tasks.stream()
                .filter(task -> task.getDescription().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private void validateIndex(int index) {
        if (index < 0 || index >= tasks.size()) {
            throw new IndexOutOfBoundsException(
                    "Index " + index + " out of bounds [0, " + tasks.size() + ")"
            );
        }
    }

    /**
     * Reschedules a deadline or event task to new date(s).
     * Todo tasks cannot be rescheduled.
     *
     * @param index 0-based task index
     * @param newBy New deadline date (for Deadline tasks)
     * @param newFrom New start date (for Event tasks)
     * @param newTo New end date (for Event tasks)
     * @throws IllegalArgumentException if task type doesn't support rescheduling
     * @throws IndexOutOfBoundsException if index is invalid
     */
    public void reschedule(int index, LocalDate newBy, LocalDate newFrom, LocalDate newTo) {
        validateIndex(index);
        Task task = tasks.get(index);

        if (task instanceof Deadline) {
            if (newBy == null) {
                throw new IllegalArgumentException("Deadline requires /by date");
            }
            ((Deadline) task).setBy(newBy);
        } else if (task instanceof Event) {
            if (newFrom == null || newTo == null) {
                throw new IllegalArgumentException("Event requires both /from and /to dates");
            }
            Event event = (Event) task;
            // Set in order to trigger validation
            event.setFrom(newFrom);
            event.setTo(newTo);
        } else {
            throw new IllegalArgumentException(
                    "Cannot reschedule Todo tasks (no date to change)"
            );
        }

        assert task != null : "Rescheduled task must not be null";
    }
}