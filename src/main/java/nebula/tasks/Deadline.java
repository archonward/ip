package nebula.tasks;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Deadline extends Task {
    private LocalDate by;
    private static final DateTimeFormatter INPUT_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter OUTPUT_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy");

    /**
     * Creates a deadline task with the given description and due date.
     *
     * @param description Description of the deadline task.
     * @param by Due date of the deadline task.
     */
    public Deadline(String description, LocalDate by) {
        super(description);
        this.by = by;
    }

    /**
     * Returns a {@code LocalDate} parsed from the given date text in {@code yyyy-MM-dd} format.
     *
     * @param text Date text in {@code yyyy-MM-dd} format.
     * @return Parsed date.
     * @throws java.time.format.DateTimeParseException If the date text cannot be parsed.
     */
    public static LocalDate parseDate(String text) {
        return LocalDate.parse(text, INPUT_FORMAT);
    }

    /**
     * Returns the due date of this deadline task.
     *
     * @return Due date.
     */
    public LocalDate getBy() {
        return by;
    }

    protected void setBy(LocalDate newBy) {
        assert newBy != null : "New deadline date must not be null";
        this.by = newBy;
    }

    /**
     * Returns the string representation of this deadline task, for display.
     *
     * @return Display string containing the deadline type tag, task details, and due date
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by.format(OUTPUT_FORMAT) + ")";
    }
}
