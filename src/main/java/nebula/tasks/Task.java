package nebula.tasks;

public class Task {
    private String description;
    private boolean isDone;

    /**
     * Creates a task with the given description.
     *
     * @param description Description of the task.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public void markDone() {
        this.isDone = true;
    }

    public void markNotDone() {
        this.isDone = false;
    }

    /**
     * Returns the status icon representing whether the task is completed.
     *
     * @return Status icon string ("X" for done, otherwise a blank space).
     */
    protected String statusIcon() {
        return isDone ? "X" : " ";
    }

    /**
     * Returns whether this task is marked as completed.
     *
     * @return True if the task is completed, false otherwise.
     */
    public boolean isDone() { // Public getter
        return isDone;
    }

    /**
     * Returns the description of this task.
     *
     * @return Task description.
     */
    public String getDescription() { // Public getter
        return description;
    }

    @Override
    public String toString() {
        return "[" + statusIcon() + "] " + description;
    }
}
