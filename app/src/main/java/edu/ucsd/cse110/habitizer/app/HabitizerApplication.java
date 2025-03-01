package edu.ucsd.cse110.habitizer.app;

import android.app.Application;

import edu.ucsd.cse110.habitizer.lib.data.InMemoryRoutineDataSource;
import edu.ucsd.cse110.habitizer.lib.data.InMemoryTaskDataSource;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.RoutineRepository;

public class HabitizerApplication extends Application {
    private InMemoryRoutineDataSource routineDataSource;

    private RoutineRepository routineRepository;

    @Override
    public void onCreate() {
        super.onCreate();


        var morningDataSource = InMemoryTaskDataSource.fromDefaultMorning();
        var eveningDataSource = InMemoryTaskDataSource.fromDefaultEvening();

        var morningRoutine = new Routine(0, 0, "Morning Routine", morningDataSource);
        var eveningRoutine = new Routine(1, 1, "Evening Routine", eveningDataSource);

        var routineDataSource = InMemoryRoutineDataSource.fromDefault();
        this.routineRepository = new RoutineRepository(routineDataSource);

        routineRepository.append(morningRoutine);
        routineRepository.append(eveningRoutine);

        routineRepository.append(new Routine(2, 2, "Custom Routine", InMemoryTaskDataSource.fromDefaultNew()));
    }

    public RoutineRepository getRoutineRepository() {
        return this.routineRepository;
    }
}
