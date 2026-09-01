package kotha.tasks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import kotha.KothaException;
import org.junit.jupiter.api.Test;

import kotha.tasks.Task;

import java.time.LocalDateTime;

class TaskTest{

    @Test
    void createTodo_createsTaskWithCorrectDescription() throws KothaException {
        Task testTask = Task.createTodo("todo clean up my table");

        assertEquals("clean up my table", testTask.getDescription());
        assertEquals("[ ]", testTask.getStatusIcon());
        assertFalse(testTask.isDone());
    }

    @Test
    void markAsDone_marksTaskAsDone() throws KothaException {
        Task testTask = Task.createTodo("todo clean up my table");
        testTask.markAsDone();
        assertTrue(testTask.isDone());
        assertEquals("[X]", testTask.getStatusIcon());
    }

    @Test
    void markAsNotDone_marksTaskAsNotDone() throws KothaException {
        Task testTask = Task.createDeadline("deadline clean up my table /by 16/05/2027 1159");
        testTask.markAsNotDone();
        assertFalse(testTask.isDone());
        assertEquals("[ ]", testTask.getStatusIcon());
    }
}