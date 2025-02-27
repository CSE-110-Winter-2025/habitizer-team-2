package edu.ucsd.cse110.habitizer.lib.domain;

import java.util.List;

import edu.ucsd.cse110.habitizer.lib.data.InMemoryRoutineDataSource;
import edu.ucsd.cse110.habitizer.lib.util.observables.PlainMutableSubject;
public class RoutineRepository {

    InMemoryRoutineDataSource routineDataSource;
    RoutineRepository(InMemoryRoutineDataSource routineDataSource){
        this.routineDataSource = routineDataSource;
    }


    public PlainMutableSubject<Routine> find(int id) {
        return routineDataSource.getRoutineSubject(id);
    }

    public PlainMutableSubject<List<Routine>> findAll() {
        return routineDataSource.getAllRoutinesSubject();
    }


    public void save(Routine routine) {
        routineDataSource.putRoutine(routine);
    }

    public void save(List<Routine> routines) {
        routineDataSource.putRoutines(routines);
    }

    public void saveAtSortOrder(Routine routine, int sortOrder){
        routineDataSource.putRoutine(
                routine.withSortOrder(sortOrder)
        );
    }

    public void remove(int id) {
        routineDataSource.removeRoutine(id);
    }

    public void append(Routine routine){
        routineDataSource.putRoutine(
                routine.withSortOrder(routineDataSource.getMaxSortOrder() + 1)
        );
    }

    public void prepend(Routine routine) {
        routineDataSource.shiftSortOrders(0, routineDataSource.getMaxSortOrder(), 1);
        routineDataSource.putRoutine(
                routine.withSortOrder(routineDataSource.getMinSortOrder()-1)
        );
    }

    public void rename(int id, String name) {
        routineDataSource.replaceName(id, name);
    }



}
