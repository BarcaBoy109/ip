package kotha.tasks;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class TaskListTest {

    @Test
    void addTask_increasesListSize() {
        TaskList taskList = new TaskList();

        taskList.add(new ToDo("read book"));

        assertEquals(1, taskList.size());
    }

    @Test
    void getTask_returnsCorrectTask() {
        TaskList taskList = new TaskList();
        Task task = new ToDo("read book");
        taskList.add(task);

        assertEquals(task, taskList.get(0));
    }
}