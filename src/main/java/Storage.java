import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class Storage {
    private static final Path FILE_PATH = Paths.get("data", "nebula.txt");

    public static ArrayList<Task> load() {
        ensureDataFolderExists();

        ArrayList<Task> tasks = new ArrayList<>();
        if (!Files.exists(FILE_PATH)) {
            return tasks; // no save file yet -> start empty
        }

        try {
            List<String> lines = Files.readAllLines(FILE_PATH);
            for (String line : lines) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                tasks.add(parseLine(line));
            }
        } catch (IOException e) {
            // Minimal handling: if load fails, start empty
            return new ArrayList<>();
        }

        return tasks;
    }

    public static void save(ArrayList<Task> tasks) {
        ensureDataFolderExists();

        ArrayList<String> lines = new ArrayList<>();
        for (Task t : tasks) {
            lines.add(toLine(t));
        }

        try {
            Files.write(FILE_PATH, lines);
        } catch (IOException e) {
            // Minimal handling: ignore or print a short message
            // System.out.println(" Could not save tasks to disk.");
        }
    }

    private static void ensureDataFolderExists() {
        try {
            Files.createDirectories(FILE_PATH.getParent());
        } catch (IOException ignored) {
            // If directory can't be created, later save/load will fail gracefully
        }
    }

    private static Task parseLine(String line) {
        String[] parts = line.split(" \\| ");

        String type = parts[0];
        boolean done = parts[1].equals("1");

        Task task;
        if (type.equals("T")) {
            // T | done | desc
            task = new Todo(parts[2]);
        } else if (type.equals("D")) {
            // D | done | desc | by
            task = new Deadline(parts[2], parts[3]);
        } else {
            // E | done | desc | from | to
            task = new Event(parts[2], parts[3], parts[4]);
        }

        if (done) {
            task.markDone();
        }
        return task;
    }

    private static String toLine(Task t) {
        String done = t.isDone ? "1" : "0";

        if (t instanceof Todo) {
            return "T | " + done + " | " + t.description;
        }

        if (t instanceof Deadline) {
            Deadline d = (Deadline) t;
            return "D | " + done + " | " + d.description + " | " + d.getBy();
        }

        Event e = (Event) t;
        return "E | " + done + " | " + e.description + " | " + e.getFrom() + " | " + e.getTo();
    }


}
