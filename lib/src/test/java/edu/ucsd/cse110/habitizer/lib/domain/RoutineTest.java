package edu.ucsd.cse110.habitizer.lib.domain;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.List;

import edu.ucsd.cse110.habitizer.lib.data.InMemoryTaskDataSource;


public class RoutineTest {

    @Test
    public void testMorningRoutine(){

        InMemoryTaskDataSource dataSource;
        Routine routine;
        dataSource = InMemoryTaskDataSource.fromDefaultMorning();
        routine = new Routine(0, 0, "Morning Routine", dataSource);

        List<Task> actTasks = routine.findAll().getValue();
        List<Task> expTasks = List.of(

                new Task(0,0 , "Shower", false),
                new Task(1,1,"Brush Teeth", false),
                new Task(2,2,"Dress", false),
                new Task(3,3,"Make Coffee", false),
                new Task(4,4,"Make Lunch", false),
                new Task(5,5,"Dinner Prep", false),
                new Task(6,6,"Pack Bag", false)

        );

        assertEquals(actTasks, expTasks);

    }

    @Test
    public void testEveningRoutine(){

        InMemoryTaskDataSource dataSource;
        Routine routine;
        dataSource = InMemoryTaskDataSource.fromDefaultEvening();
        routine = new Routine(0, 0, "Evening", dataSource);

        List<Task> actTasks = routine.findAll().getValue();
        List<Task> expTasks = List.of(
                new Task(0,0 , "Prepare Dinner", false),
                new Task(1,1,"Eat Dinner", false),
                new Task(2,2,"Do Laundry", false),
                new Task(3,3,"Watch TV", false),
                new Task(4,4,"Walk Dog", false),
                new Task(5,5,"Shower", false),
                new Task(6,6,"Get in Bed", false)

        );

        assertEquals(actTasks, expTasks);

    }

    @Test
    public void testPrepend(){

        InMemoryTaskDataSource dataSource;
        Routine routine;
        dataSource = InMemoryTaskDataSource.fromDefaultEvening();
        Task newTask = new Task(7,0,"Put Night Mask On", false);
        routine = new Routine(0, 0, "Evening", dataSource);
        routine.prepend(newTask);

        List<Task> actTasks = routine.findAll().getValue();
        List<Task> expTasks = List.of(
                new Task(0,1, "Prepare Dinner", false),
                new Task(1,2,"Eat Dinner", false),
                new Task(2,3,"Do Laundry", false),
                new Task(3,4,"Watch TV", false),
                new Task(4,5,"Walk Dog", false),
                new Task(5,6,"Shower", false),
                new Task(6,7,"Get in Bed", false),
                new Task(7,0,"Put Night Mask On", false)
        );

        assertEquals(actTasks, expTasks);

    }

    @Test
    public void testAppend(){

        InMemoryTaskDataSource dataSource;
        Routine routine;
        dataSource = InMemoryTaskDataSource.fromDefaultEvening();
        Task newTask = new Task(7,0,"Put Night Mask On", false);
        routine = new Routine(0, 0, "Evening", dataSource);
        routine.append(newTask);

        List<Task> actTasks = routine.findAll().getValue();
        List<Task> expTasks = List.of(
                new Task(0,0, "Prepare Dinner", false),
                new Task(1,1,"Eat Dinner", false),
                new Task(2,2,"Do Laundry", false),
                new Task(3,3,"Watch TV", false),
                new Task(4,4,"Walk Dog", false),
                new Task(5,5,"Shower", false),
                new Task(6,6,"Get in Bed", false),
                new Task(7,7,"Put Night Mask On", false)
        );

        assertEquals(actTasks, expTasks);

    }

    @Test
    public void testSave(){

        InMemoryTaskDataSource dataSource;
        Routine routine;
        dataSource = InMemoryTaskDataSource.fromDefaultEvening();
        Task rewroteTask = new Task(0,0,"Take out trash", true);
        routine = new Routine(0, 0, "Evening", dataSource);
        routine.save(rewroteTask);

        List<Task> actTasks = routine.findAll().getValue();
        List<Task> expTasks = List.of(
                new Task(0,0, "Take out trash", true),
                new Task(1,1,"Eat Dinner", false),
                new Task(2,2,"Do Laundry", false),
                new Task(3,3,"Watch TV", false),
                new Task(4,4,"Walk Dog", false),
                new Task(5,5,"Shower", false),
                new Task(6,6,"Get in Bed", false)
        );

        assertEquals(actTasks, expTasks);

    }

    @Test
    public void testRename(){


        InMemoryTaskDataSource dataSource;
        Routine routine;
        dataSource = InMemoryTaskDataSource.fromDefaultEvening();
        routine = new Routine(0, 0, "Evening", dataSource);
        routine.rename(0,"Take out trash");

        List<Task> actTasks = routine.findAll().getValue();
        List<Task> expTasks = List.of(
                new Task(0,0, "Take out trash", false),
                new Task(1,1,"Eat Dinner", false),
                new Task(2,2,"Do Laundry", false),
                new Task(3,3,"Watch TV", false),
                new Task(4,4,"Walk Dog", false),
                new Task(5,5,"Shower", false),
                new Task(6,6,"Get in Bed", false)
        );

        assertEquals(actTasks, expTasks);

    }

    @Test
    public void testGoalTime(){

        InMemoryTaskDataSource dataSource;
        Routine routine;
        dataSource = InMemoryTaskDataSource.fromDefaultEvening();
        routine = new Routine(0, 0, "Evening", dataSource);

        assertEquals(45, routine.getGoalTime());

    }

    @Test
    public void testSetGoalTime(){

        InMemoryTaskDataSource dataSource;
        Routine routine;
        dataSource = InMemoryTaskDataSource.fromDefaultEvening();
        routine = new Routine(0, 0, "Evening", dataSource);
        routine.setGoalTime(90);
        assertEquals(90, routine.getGoalTime());

    }

    @Test
    public void testRemove(){

        InMemoryTaskDataSource dataSource;
        Routine routine;
        dataSource = InMemoryTaskDataSource.fromDefaultEvening();
        routine = new Routine(0, 0, "Evening", dataSource);
        routine.remove(0);

        List<Task> actTasks = routine.findAll().getValue();
        List<Task> expTasks = List.of(
                new Task(1,0,"Eat Dinner", false),
                new Task(2,1,"Do Laundry", false),
                new Task(3,2,"Watch TV", false),
                new Task(4,3,"Walk Dog", false),
                new Task(5,4,"Shower", false),
                new Task(6,5,"Get in Bed", false)
        );

        assertEquals(actTasks, expTasks);
    }

    @Test
    public void testSwapTasks(){ //added test for swapping tasks
        InMemoryTaskDataSource dataSource;
        Routine routine;
        dataSource = InMemoryTaskDataSource.fromDefaultEvening();


        List<Task> expTasks = List.of(

                new Task(0,0 , "Shower", false),
                new Task(1,1,"Brush Teeth", false),
                new Task(2,2,"Dress", false),
                new Task(3,3,"Make Coffee", false),
                new Task(4,4,"Make Lunch", false),
                new Task(5,5,"Dinner Prep", false),
                new Task(6,6,"Pack Bag", false)

        );
    }

}
