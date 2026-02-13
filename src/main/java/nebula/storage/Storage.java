package nebula.storage;

import nebula.tasks.Deadline;
import nebula.tasks.Event;
import nebula.tasks.Task;
import nebula.tasks.Todo;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;

public class Storage {
    private static final String DATA_DIR = "data";
    private static final String TASKS_FILE = "tasks.txt";
    private static final String FILE_PATH = DATA_DIR + File.separator + TASKS_FILE;

    // Consistent delimiter constants (single space before/after pipe)
    private static final String DELIMITER_REGEX = " \\| ";   // For parsing (regex)
    private static final String DELIMITER = " | ";           // For writing (literal)

    /**
     * Loads tasks from the persistent storage file.
     * If the storage file does not exist or cannot be read, an empty list is returned.
     *
     * @return List of loaded tasks.
     */
    public static ArrayList<Task> load() {
        ensureDataFolderExists();
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            return new ArrayList<>(); // Return empty list on first run
        }

        try {
            ArrayList<Task> tasks = new ArrayList<>();
            for (String line : Files.readAllLines(Paths.get(FILE_PATH))) {
                line = line.trim();
                if (!line.isEmpty()) {
                    tasks.add(parseLine(line));
                }
            }
            return tasks;
        } catch (IOException e) {
            System.err.println("Warning: Could not load tasks from disk. Starting with empty list.");
            return new ArrayList<>();
        }
    }

    /**
     * Saves the given tasks to the persistent storage file.
     *
     * @param tasks Tasks to save.
     */
    public static void save(ArrayList<Task> tasks) {
        ensureDataFolderExists();
        ArrayList<String> lines = new ArrayList<>();

        for (Task task : tasks) {
            lines.add(toLine(task));
        }

        try {
            Files.write(Paths.get(FILE_PATH), lines);
        } catch (IOException e) {
            System.err.println("Warning: Could not save tasks to disk.");
        }
    }

    private static void ensureDataFolderExists() {
        try {
            Files.createDirectories(Paths.get(FILE_PATH).getParent());
        } catch (IOException e) {
            // Directory creation failure will be caught during actual file I/O
        }
    }

    private static Task parseLine(String line) {
        String[] parts = line.split(DELIMITER_REGEX);
        String type = parts[0].trim();
        boolean done = parts[1].trim().equals("1");
        Task task;

        switch (type) {
            case "T":
                task = new Todo(parts[2].trim());
                break;
            case "D":
                task = new Deadline(parts[2].trim(), LocalDate.parse(parts[3].trim()));
                break;
            case "E":
                task = new Event(parts[2].trim(), parts[3].trim(), parts[4].trim());
                break;
            default:
                throw new IllegalArgumentException("Unknown task type: " + type);
        }

        if (done) {
            task.markDone();
        }
        return task;
    }

    private static String toLine(Task task) {
        String doneStatus = task.isDone() ? "1" : "0";

        if (task instanceof Todo) {
            return "T" + DELIMITER + doneStatus + DELIMITER + task.getDescription();
        } else if (task instanceof Deadline) {
            Deadline deadline = (Deadline) task;
            return "D" + DELIMITER + doneStatus + DELIMITER
                    + deadline.getDescription() + DELIMITER
                    + deadline.getBy().toString();
        } else { // Event
            Event event = (Event) task;
            return "E" + DELIMITER + doneStatus + DELIMITER
                    + event.getDescription() + DELIMITER
                    + event.getFrom().toString() + DELIMITER
                    + event.getTo().toString();
        }
    }
}