package edu.ucsd.cse110.habitizer.lib.domain;

import static org.junit.Assert.*;

import org.junit.Test;

public class TaskTest {
    @Test
    public void testGetters() {
        var task = new Task(1, 0, "wash hands");
        assertEquals(Integer.valueOf(1), task.id());
        assertEquals("wash hands", task.name());
        assertEquals(0, task.sortOrder());
    }

    @Test
    public void testWithId() {
        var task = new Task(1,0 , "wash hands");
        var expected = new Task(42, 0, "wash hands");
        var actual = task.withId(42);
        assertEquals(expected, actual);
    }

    @Test
    public void testWithSortOrder() {
        var task = new Task(1, 0, "wash hands");
        var expected = new Task(1, 42, "wash hands");
        var actual = task.withSortOrder(42);
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        var task1 = new Task(1, 0, "wash hands");
        var task2 = new Task(1, 0, "wash hands");
        var task3 = new Task(2, 1, "find land");

        assertEquals(task1, task2);
        assertNotEquals(task1, task3);
    }
}