package nebula.tasks;

public class Task {
    private String description;
    private boolean isDone;

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

    protected String statusIcon() {
        return isDone ? "X" : " ";
    }

    public boolean isDone() { // Public getter
        return isDone;
    }

    public String getDescription() { // Public getter
        return description;
    }

    @Override
    public String toString() {
        return "[" + statusIcon() + "] " + description;
    }
}
