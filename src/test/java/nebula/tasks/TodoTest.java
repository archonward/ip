package nebula.tasks;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TodoTest {

    @Test
    void newTodo_isNotDoneByDefault() {
        Todo todo = new Todo("Read book");
        assertFalse(todo.isDone());
    }

    @Test
    void markDone_changesStateToDone() {
        Todo todo = new Todo("Read book");
        assertFalse(todo.isDone());

        todo.markDone();
        assertTrue(todo.isDone());
    }

    @Test
    void markNotDone_changesStateBackToNotDone() {
        Todo todo = new Todo("Read book");
        todo.markDone();
        assertTrue(todo.isDone());

        todo.markNotDone();
        assertFalse(todo.isDone());
    }

    @Test
    void getDescription_returnsCorrectDescription() {
        Todo todo = new Todo("Read book");
        assertEquals("Read book", todo.getDescription());
    }

    @Test
    void toString_includesCorrectPrefixAndStatus() {
        Todo todo = new Todo("Read book");
        assertEquals("[T][ ] Read book", todo.toString());

        todo.markDone();
        assertEquals("[T][X] Read book", todo.toString());
    }
}