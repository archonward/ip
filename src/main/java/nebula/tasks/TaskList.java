package nebula.tasks;

import java.util.ArrayList;

public class TaskList {
    private final ArrayList<Task> tasks;

    /** Create an empty task list */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /** Create task list from existing data (for loading from storage) */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /** Add a task to the list */
    public void add(Task task) {
        tasks.add(task);
    }

    /** Delete task at 0-based index. Returns deleted task. */
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

    private void validateIndex(int index) {
        if (index < 0 || index >= tasks.size()) {
            throw new IndexOutOfBoundsException(
                    "Index " + index + " out of bounds [0, " + tasks.size() + ")"
            );
        }
    }
}