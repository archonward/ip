package nebula.tasks;

import java.util.ArrayList;

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
        this.tasks = tasks;
    }

    /**
     * Adds a task to this task list.
     *
     * @param task Task to add.
     */
    public void add(Task task) {
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
        return tasks.remove(index);
    }

    /** Mark task at 0-based index as done */
    public void mark(int index) {
        validateIndex(index);
        tasks.get(index).markDone();
    }

    /** Mark task at 0-based index as not done */
    public void unmark(int index) {
        validateIndex(index);
        tasks.get(index).markNotDone();
    }

    /** Get task at 0-based index */
    public Task get(int index) {
        validateIndex(index);
        return tasks.get(index);
    }

    /** Get total number of tasks */
    public int size() {
        return tasks.size();
    }

    /** Check if list is empty */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    /** Get all tasks (for display/storage) */
    public ArrayList<Task> getAll() {
        return tasks; // Direct reference (simplest for this scale)
    }

    public ArrayList<Task> find(String keyword) {
        ArrayList<Task> matches = new ArrayList<>();

        String trimmed = keyword.trim();
        if (trimmed.isEmpty()) {
            return matches;
        }

        String needle = trimmed.toLowerCase();
        for (Task task : tasks) {
            String haystack = task.getDescription().toLowerCase();
            if (haystack.contains(needle)) {
                matches.add(task);
            }
        }

        return matches;
    }

    private void validateIndex(int index) {
        if (index < 0 || index >= tasks.size()) {
            throw new IndexOutOfBoundsException(
                    "Index " + index + " out of bounds [0, " + tasks.size() + ")"
            );
        }
    }
}