package kotha.tasks;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import kotha.KothaException;

class ConstraintTest {
    @Test
    void createConstraint_createsTaskWithTriggerAndExpectedText() throws KothaException {
        Constraint task = (Constraint) Task.createConstraint("constraint return book /after exam");

        assertEquals("return book", task.getDescription());
        assertEquals("exam", task.getAfter());
        assertEquals("[C][ ] return book (after: exam)", task.toString());
    }
}
