package edu.ucsd.cse110.habitizer.lib.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.List;

import edu.ucsd.cse110.habitizer.lib.data.InMemoryRoutineDataSource;
import edu.ucsd.cse110.habitizer.lib.data.InMemoryTaskDataSource;

public class RoutineRepositoryTest {

    @Test
    public void testDefaultRoutineRepository(){

        InMemoryRoutineDataSource routineDataSource;
        routineDataSource = InMemoryRoutineDataSource.fromDefault();

        RoutineRepository defaultRoutineRepository = new RoutineRepository(routineDataSource);

        List<Routine> actRoutines = defaultRoutineRepository.findAll().getValue();
        List<Routine> expRoutines = List.of(
                new Routine(0,0 , "Morning Routine", InMemoryTaskDataSource.fromDefaultMorning()),
                new Routine(1,1,"Evening Routine", InMemoryTaskDataSource.fromDefaultEvening())
        );

        assertRoutineListEqual(expRoutines, actRoutines);

    }

    @Test
    public void testPrepend(){

        InMemoryRoutineDataSource routineDataSource;
        routineDataSource = InMemoryRoutineDataSource.fromDefault();

        RoutineRepository defaultRoutineRepository = new RoutineRepository(routineDataSource);


        InMemoryTaskDataSource newRoutineDataSource;
        newRoutineDataSource = InMemoryTaskDataSource.fromDefaultNew();

        Routine newRoutine = new Routine(2,0,"New Routine", newRoutineDataSource);

        defaultRoutineRepository.prepend(newRoutine);
        List<Routine> actRoutines = defaultRoutineRepository.findAll().getValue();
        List<Routine> expRoutines = List.of(
                new Routine(0,1 , "Morning Routine", InMemoryTaskDataSource.fromDefaultMorning()),
                new Routine(1,2,"Evening Routine", InMemoryTaskDataSource.fromDefaultEvening()),
                new Routine(2,0,"New Routine", InMemoryTaskDataSource.fromDefaultNew())

        );

        assertRoutineListEqual(expRoutines, actRoutines);


    }

    @Test
    public void testAppend(){

        InMemoryRoutineDataSource routineDataSource;
        routineDataSource = InMemoryRoutineDataSource.fromDefault();

        RoutineRepository defaultRoutineRepository = new RoutineRepository(routineDataSource);


        InMemoryTaskDataSource newRoutineDataSource;
        newRoutineDataSource = InMemoryTaskDataSource.fromDefaultNew();

        Routine newRoutine = new Routine(2,0,"New Routine", newRoutineDataSource);

        defaultRoutineRepository.append(newRoutine);
        List<Routine> actRoutines = defaultRoutineRepository.findAll().getValue();
        List<Routine> expRoutines = List.of(
                new Routine(0,0 , "Morning Routine", InMemoryTaskDataSource.fromDefaultMorning()),
                new Routine(1,1,"Evening Routine", InMemoryTaskDataSource.fromDefaultEvening()),
                new Routine(2,2,"New Routine", InMemoryTaskDataSource.fromDefaultNew())
        );

        assertRoutineListEqual(expRoutines, actRoutines);


    }

    //save is kind've a poor name here because it is more used for overwriting current RoutineRepo data
    //It "saves" changes you make to stuff already in the repo.
    @Test
    public void testSave(){

        InMemoryRoutineDataSource routineDataSource;
        routineDataSource = InMemoryRoutineDataSource.fromDefault();

        RoutineRepository defaultRoutineRepository = new RoutineRepository(routineDataSource);

        Routine horseshoe = defaultRoutineRepository.find(0).getValue().withName("Horseshoe");

        defaultRoutineRepository.save(horseshoe);
        List<Routine> actRoutines = defaultRoutineRepository.findAll().getValue();
        List<Routine> expRoutines = List.of(
                new Routine(0,0 , "Horseshoe", InMemoryTaskDataSource.fromDefaultMorning()),
                new Routine(1,1,"Evening Routine", InMemoryTaskDataSource.fromDefaultEvening())
        );

        assertRoutineListEqual(expRoutines, actRoutines);

    }

    @Test
    public void testRename(){
        InMemoryRoutineDataSource routineDataSource;
        routineDataSource = InMemoryRoutineDataSource.fromDefault();

        RoutineRepository defaultRoutineRepository = new RoutineRepository(routineDataSource);

        defaultRoutineRepository.rename(0, "Horseshoe");

        List<Routine> actRoutines = defaultRoutineRepository.findAll().getValue();
        List<Routine> expRoutines = List.of(
                new Routine(0,0 , "Horseshoe", InMemoryTaskDataSource.fromDefaultMorning()),
                new Routine(1,1,"Evening Routine", InMemoryTaskDataSource.fromDefaultEvening())
        );

        assertRoutineListEqual(expRoutines, actRoutines);

    }

    @Test
    public void testRemove(){

        InMemoryRoutineDataSource routineDataSource;
        routineDataSource = InMemoryRoutineDataSource.fromDefault();

        RoutineRepository defaultRoutineRepository = new RoutineRepository(routineDataSource);

        defaultRoutineRepository.remove(0);

        List<Routine> actRoutines = defaultRoutineRepository.findAll().getValue();
        List<Routine> expRoutines = List.of(
                new Routine(1,0,"Evening Routine", InMemoryTaskDataSource.fromDefaultEvening())
        );

        assertRoutineListEqual(expRoutines, actRoutines);

    }

    //fucking pain work around. Cannot compare two data sources as they refer to different places in memory
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
