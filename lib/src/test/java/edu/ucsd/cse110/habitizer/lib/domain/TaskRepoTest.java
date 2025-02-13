package edu.ucsd.cse110.habitizer.lib.domain;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.List;

import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.habitizer.lib.util.observables.PlainMutableSubject;


public class TaskRepoTest {

    @Test
    public void testTaskRepo(){

        InMemoryDataSource dataSource;
        TaskRepository taskRepository;
        dataSource = InMemoryDataSource.fromDefault();
        taskRepository = new TaskRepository(dataSource);

        List<Task> actTasks = taskRepository.findAll().getValue();
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

}
