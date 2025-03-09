package edu.ucsd.cse110.habitizer.lib.domain;

import java.util.List;

import edu.ucsd.cse110.habitizer.lib.data.InMemoryRoutineDataSource;
import edu.ucsd.cse110.habitizer.lib.util.observables.PlainMutableSubject;

public class SimpleRoutineRepository implements RoutineRepository {

    InMemoryRoutineDataSource routineDataSource;
    public SimpleRoutineRepository(InMemoryRoutineDataSource routineDataSource){
        this.routineDataSource = routineDataSource;
    }


    @Override
    public PlainMutableSubject<Routine> find(int id) {
        return routineDataSource.getRoutineSubject(id);
    }

    @Override
    public PlainMutableSubject<List<Routine>> findAll() {
        return routineDataSource.getAllRoutinesSubject();
    }


    @Override
    public void save(Routine routine) {
        routineDataSource.putRoutine(routine);
    }

    @Override
    public void save(List<Routine> routines) {
        routineDataSource.putRoutines(routines);
    }

    @Override
    public void remove(int id) {
        routineDataSource.removeRoutine(id);
    }

    @Override
    public void append(Routine routine){
        routineDataSource.putRoutine(
                routine.withSortOrder(routineDataSource.getMaxSortOrder() + 1)
        );
    }

    @Override
    public void prepend(Routine routine) {
        routineDataSource.shiftSortOrders(0, routineDataSource.getMaxSortOrder(), 1);
        routineDataSource.putRoutine(
                routine.withSortOrder(routineDataSource.getMinSortOrder()-1)
        );
    }

    @Override
    public void rename(int id, String name) {
        routineDataSource.replaceName(id, name);
    }



}
