package edu.ucsd.cse110.habitizer.lib.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

import java.util.List;

import edu.ucsd.cse110.habitizer.lib.data.InMemoryRoutineDataSource;
import edu.ucsd.cse110.habitizer.lib.data.InMemoryTaskDataSource;

public class RoutineRepositoryTest {

    @Test
    public void testDefaultRoutineRepository(){

        InMemoryRoutineDataSource routineDataSource;
        routineDataSource = InMemoryRoutineDataSource.fromDefault();

        RoutineRepository defaultRoutineRepository = new SimpleRoutineRepository(routineDataSource);

        List<Routine> actRoutines = defaultRoutineRepository.findAll().getValue();
        List<Routine> expRoutines = List.of(
                new Routine(0,0 , "Morning Routine", 45, InMemoryTaskDataSource.DEFAULT_TASKS_MORNING),
                new Routine(1,1,"Evening Routine", 45, InMemoryTaskDataSource.DEFAULT_TASKS_EVENING)
        );

        assertRoutineListEqual(expRoutines, actRoutines);

    }

    @Test
    public void testPrepend(){

        InMemoryRoutineDataSource routineDataSource;
        routineDataSource = InMemoryRoutineDataSource.fromDefault();

        RoutineRepository defaultRoutineRepository = new SimpleRoutineRepository(routineDataSource);


        InMemoryTaskDataSource newRoutineDataSource;
        newRoutineDataSource = InMemoryTaskDataSource.fromDefaultNew();

        Routine newRoutine = new Routine(2,0,"New Routine", 45, newRoutineDataSource);

        defaultRoutineRepository.prepend(newRoutine);
        List<Routine> actRoutines = defaultRoutineRepository.findAll().getValue();
        List<Routine> expRoutines = List.of(
                new Routine(0,1 , "Morning Routine", 45, InMemoryTaskDataSource.fromDefaultMorning()),
                new Routine(1,2,"Evening Routine", 45, InMemoryTaskDataSource.fromDefaultEvening()),
                new Routine(2,0,"New Routine", 45, InMemoryTaskDataSource.fromDefaultNew())

        );

        assertRoutineListEqual(expRoutines, actRoutines);


    }

    @Test
    public void testAppend(){

        InMemoryRoutineDataSource routineDataSource;
        routineDataSource = InMemoryRoutineDataSource.fromDefault();

        RoutineRepository defaultRoutineRepository = new SimpleRoutineRepository(routineDataSource);


        InMemoryTaskDataSource newRoutineDataSource;
        newRoutineDataSource = InMemoryTaskDataSource.fromDefaultNew();

        Routine newRoutine = new Routine(2,0,"New Routine", 45, newRoutineDataSource);

        defaultRoutineRepository.append(newRoutine);
        List<Routine> actRoutines = defaultRoutineRepository.findAll().getValue();
        List<Routine> expRoutines = List.of(
                new Routine(0,0 , "Morning Routine", 45, InMemoryTaskDataSource.fromDefaultMorning()),
                new Routine(1,1,"Evening Routine", 45, InMemoryTaskDataSource.fromDefaultEvening()),
                new Routine(2,2,"New Routine", 45, InMemoryTaskDataSource.fromDefaultNew())
        );

        assertRoutineListEqual(expRoutines, actRoutines);


    }


    @Test
    public void testSave(){

        InMemoryRoutineDataSource routineDataSource;
        routineDataSource = InMemoryRoutineDataSource.fromDefault();

        RoutineRepository defaultRoutineRepository = new SimpleRoutineRepository(routineDataSource);

        Routine horseshoe = defaultRoutineRepository.find(0).getValue().withName("Horseshoe");

        defaultRoutineRepository.save(horseshoe);
        List<Routine> actRoutines = defaultRoutineRepository.findAll().getValue();
        List<Routine> expRoutines = List.of(
                new Routine(0,0 , "Horseshoe", 45, InMemoryTaskDataSource.fromDefaultMorning()),
                new Routine(1,1,"Evening Routine", 45,InMemoryTaskDataSource.fromDefaultEvening())
        );

        assertRoutineListEqual(expRoutines, actRoutines);

    }

    @Test
    public void testRename(){
        InMemoryRoutineDataSource routineDataSource;
        routineDataSource = InMemoryRoutineDataSource.fromDefault();

        RoutineRepository defaultRoutineRepository = new SimpleRoutineRepository(routineDataSource);

        defaultRoutineRepository.rename(0, "Horseshoe");

        List<Routine> actRoutines = defaultRoutineRepository.findAll().getValue();
        List<Routine> expRoutines = List.of(
                new Routine(0,0 , "Horseshoe", 45, InMemoryTaskDataSource.fromDefaultMorning()),
                new Routine(1,1,"Evening Routine", 45, InMemoryTaskDataSource.fromDefaultEvening())
        );

        assertRoutineListEqual(expRoutines, actRoutines);

    }

    @Test
    public void testRemove(){

        InMemoryRoutineDataSource routineDataSource;
        routineDataSource = InMemoryRoutineDataSource.fromDefault();

        RoutineRepository defaultRoutineRepository = new SimpleRoutineRepository(routineDataSource);

        defaultRoutineRepository.remove(0);

        List<Routine> actRoutines = defaultRoutineRepository.findAll().getValue();
        List<Routine> expRoutines = List.of(
                new Routine(1,0,"Evening Routine", 45, InMemoryTaskDataSource.fromDefaultEvening())
        );

        assertRoutineListEqual(expRoutines, actRoutines);
    }

    @Test
    public void test_remove_multiple(){
        InMemoryRoutineDataSource routineDataSource;
        routineDataSource = InMemoryRoutineDataSource.fromDefault();

        RoutineRepository defaultRoutineRepository = new RoutineRepository(routineDataSource);

        defaultRoutineRepository.remove(0);
        defaultRoutineRepository.remove(1);

        List<Routine> actRoutines = defaultRoutineRepository.findAll().getValue();
        List<Routine> expRoutines = List.of();

        assertRoutineListEqual(expRoutines, actRoutines);
    }

    @Test
    public void test_remove_nonexisting() {
        InMemoryRoutineDataSource routineDataSource = InMemoryRoutineDataSource.fromDefault();
        RoutineRepository defaultRoutineRepository = new RoutineRepository(routineDataSource);

        // Verify the exception is thrown when trying to remove a non-existent routine
        assertThrows(IllegalArgumentException.class, () -> defaultRoutineRepository.remove(9));

        // Verify the repository wasn't modified by the failed removal
        List<Routine> actRoutines = defaultRoutineRepository.findAll().getValue();
        List<Routine> expRoutines = List.of(
                new Routine(0, 0, "Morning Routine", InMemoryTaskDataSource.fromDefaultMorning()),
                new Routine(1, 1, "Evening Routine", InMemoryTaskDataSource.fromDefaultEvening())
        );

        assertRoutineListEqual(expRoutines, actRoutines);
    }


    @Test
    public void testRemoveBug(){

        InMemoryRoutineDataSource routineDataSource;
        routineDataSource = InMemoryRoutineDataSource.fromDefault();

        RoutineRepository defaultRoutineRepository = new RoutineRepository(routineDataSource);

        defaultRoutineRepository.remove(0);

        List<Routine> actRoutines = defaultRoutineRepository.findAll().getValue();
        List<Routine> expRoutines = List.of(
                new Routine(1,0,"Evening Routine", InMemoryTaskDataSource.fromDefaultEvening())
        );

        assertRoutineListEqual(expRoutines, actRoutines);

        defaultRoutineRepository.append(new Routine(2,1,"new",InMemoryTaskDataSource.fromDefaultNew()));

        actRoutines = defaultRoutineRepository.findAll().getValue();
        expRoutines = List.of(
                new Routine(1,0,"Evening Routine", InMemoryTaskDataSource.fromDefaultEvening()),
                new Routine(2,1,"new",InMemoryTaskDataSource.fromDefaultNew())
        );


        assertRoutineListEqual(expRoutines, actRoutines);

        defaultRoutineRepository.findAll().getValue().get(0).rename(0, "breakfast");

        actRoutines = defaultRoutineRepository.findAll().getValue();
        expRoutines.get(0).rename(0,"breakfast");
        assertRoutineListEqual(expRoutines, actRoutines);


    }

    // Cannot compare two data sources as they refer to different places in memory
    //have to instead extract the task lists for each routine and check for equality of those.
    void assertRoutineListEqual(List<Routine> exp, List<Routine> act){

        for(int i = 0; i < act.size(); i++){
            assertEquals(exp.get(i).id(),act.get(i).id());
            assertEquals(exp.get(i).sortOrder(),act.get(i).sortOrder());
            assertEquals(exp.get(i).name(),act.get(i).name());
            List<Task> actTasks = act.get(i).findAll().getValue();
            List<Task> expTasks = exp.get(i).findAll().getValue();
            assertEquals(expTasks, actTasks);
        }

    }

}