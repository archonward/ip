package nebula.tasks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TaskListTest {
    private TaskList taskList;

    @BeforeEach
    void setUp() {
        taskList = new TaskList();
        taskList.add(new Todo("Read book"));
        taskList.add(new Todo("Submit report"));
        taskList.add(new Todo("Buy groceries"));
    }

    @Test
    void delete_middleIndex_removesCorrectTask() {
        Task removed = taskList.delete(1);  // Delete "Submit report"
        assertEquals("Submit report", removed.getDescription());
        assertEquals(2, taskList.size());
        assertEquals("Read book", taskList.get(0).getDescription());
        assertEquals("Buy groceries", taskList.get(1).getDescription());
    }

    @Test
    void delete_firstIndex_removesFirstTask() {
        Task removed = taskList.delete(0);
        assertEquals("Read book", removed.getDescription());
        assertEquals(2, taskList.size());
        assertEquals("Submit report", taskList.get(0).getDescription());
    }

    @Test
    void delete_lastIndex_removesLastTask() {
        Task removed = taskList.delete(2);  // Last valid index in 3-item list
        assertEquals("Buy groceries", removed.getDescription());
        assertEquals(2, taskList.size());
    }

    @Test
    void delete_emptyList_throwsException() {
        TaskList empty = new TaskList();
        assertThrows(IndexOutOfBoundsException.class, () -> empty.delete(0));
    }

    @Test
    void delete_negativeIndex_throwsException() {
        assertThrows(IndexOutOfBoundsException.class, () -> taskList.delete(-1));
    }
}