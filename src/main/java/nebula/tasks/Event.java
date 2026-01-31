package nebula.tasks;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Event extends Task {
    private LocalDate from;
    private LocalDate to;

    // Input format: "yyyy-MM-dd"
    private static final DateTimeFormatter INPUT_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // Output format: "MMM dd yyyy"
    private static final DateTimeFormatter OUTPUT_FORMATTER =
            DateTimeFormatter.ofPattern("MMM dd yyyy");

    /**
     * Creates an event with date strings in "yyyy-MM-dd" format.
     * @throws DateTimeParseException if dates are invalid
     */
    public Event(String description, String fromStr, String toStr) {
        super(description);
        // Parse strings → LocalDate (critical fix!)
        this.from = LocalDate.parse(fromStr.trim(), INPUT_FORMATTER);
        this.to = LocalDate.parse(toStr.trim(), INPUT_FORMATTER);

        // Optional: Validate that 'to' is not before 'from'
        if (this.to.isBefore(this.from)) {
            throw new IllegalArgumentException(
                    "End date cannot be before start date: " + fromStr + " → " + toStr
            );
        }
    }

    // Domain-pure getters (return LocalDate for flexibility)
    public LocalDate getFrom() {
        return from;
    }

    public LocalDate getTo() {
        return to;
    }

    @Override
    public String toString() {
        // Format dates for display using OUTPUT_FORMATTER
        return "[E]" + super.toString() +
                " (from: " + from.format(OUTPUT_FORMATTER) +
                " to: " + to.format(OUTPUT_FORMATTER) + ")";
    }
}
