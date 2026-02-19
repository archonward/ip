package nebula.tasks;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import static org.junit.jupiter.api.Assertions.*;

/**
 * High-value tests for Deadline class core functionality.
 */
public class DeadlineTest {

    @Test
    public void constructor_setsDescriptionAndDueDate_correctly() {
        // Tests core object creation and state initialization
        Deadline deadline = new Deadline("Submit report", LocalDate.of(2026, 2, 19));

        assertEquals("Submit report", deadline.getDescription(), "Description must match");
        assertEquals(LocalDate.of(2026, 2, 19), deadline.getBy(), "Due date must match");
        assertFalse(deadline.isDone(), "New deadline must be unmarked by default");
    }

    @Test
    public void parseDate_validString_returnsCorrectLocalDate() {
        // Tests critical user-input parsing functionality
        LocalDate result = Deadline.parseDate("2026-12-31");

        assertEquals(LocalDate.of(2026, 12, 31), result, "Parsed date must match input");
    }

    @Test
    public void toString_formatsDeadline_correctlyForDisplay() {
        // Tests user-facing output (what users actually see in app)
        Deadline deadline = new Deadline("Project deadline", LocalDate.of(2026, 2, 19));

        // Format: [D][status] description (by: MMM dd yyyy)
        String expected = "[D][ ] Project deadline (by: Feb 19 2026)";
        assertEquals(expected, deadline.toString(), "ToString must match display format");
    }

    @Test
    public void parseDate_invalidFormat_throwsDateTimeParseException() {
        // Tests robustness against malformed user input (critical for error handling)
        assertThrows(
                DateTimeParseException.class,
                () -> Deadline.parseDate("invalid-date"),
                "Invalid date format must throw parsing exception"
        );
    }
}