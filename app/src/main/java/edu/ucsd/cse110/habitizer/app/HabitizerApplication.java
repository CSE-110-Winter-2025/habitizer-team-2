package edu.ucsd.cse110.habitizer.app;

import android.app.Application;

import edu.ucsd.cse110.habitizer.lib.data.InMemoryRoutineDataSource;
import edu.ucsd.cse110.habitizer.lib.data.InMemoryTaskDataSource;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.RoutineRepository;

public class HabitizerApplication extends Application {
    private InMemoryTaskDataSource morningDataSource;

    private InMemoryTaskDataSource eveningDataSource;
    private Routine morningRoutine;
    private Routine eveningRoutine;

    private InMemoryRoutineDataSource routineDataSource;

    private RoutineRepository routineRepository;

    @Override
    public void onCreate() {
        super.onCreate();

        this.morningDataSource = InMemoryTaskDataSource.fromDefaultMorning();
        this.eveningDataSource = InMemoryTaskDataSource.fromDefaultEvening();
        this.morningRoutine = new Routine(0, 0, "Morning Routine", morningDataSource);
        this.eveningRoutine = new Routine(0, 0, "Evening Routine", eveningDataSource);

        this.routineDataSource = InMemoryRoutineDataSource.fromDefault();
        this.routineRepository = new RoutineRepository(routineDataSource);
    }

    public RoutineRepository getRoutineRepository() {return this.routineRepository;}
    public Routine getMorningTaskRepository() {return morningRoutine;}
    public Routine getEveningTaskRepository() {return eveningRoutine;}
}
