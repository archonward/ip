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
     * Creates an event task with the given description and start/end dates.
     * The date strings are parsed using the {@code yyyy-MM-dd} format.
     *
     * @param description Description of the event task.
     * @param fromStr Start date in {@code yyyy-MM-dd} format.
     * @param toStr End date in {@code yyyy-MM-dd} format.
     * @throws java.time.format.DateTimeParseException If either date string cannot be parsed.
     * @throws IllegalArgumentException If the end date is before the start date.
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

    protected void setFrom(LocalDate newFrom) {
        assert newFrom != null : "New start date must not be null";
        this.from = newFrom;
        validateDates();
    }

    protected void setTo(LocalDate newTo) {
        assert newTo != null : "New end date must not be null";
        this.to = newTo;
        validateDates();
    }

    private void validateDates() {
        if (this.to.isBefore(this.from)) {
            throw new IllegalArgumentException(
                    "End date cannot be before start date: " +
                            this.from + " → " + this.to
            );
        }
    }

    @Override
    public String toString() {
        // Format dates for display using OUTPUT_FORMATTER
        return "[E]" + super.toString() +
                " (from: " + from.format(OUTPUT_FORMATTER) +
                " to: " + to.format(OUTPUT_FORMATTER) + ")";
    }
}
