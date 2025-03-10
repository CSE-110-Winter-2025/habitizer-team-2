package edu.ucsd.cse110.habitizer.app;

import android.app.Application;

import edu.ucsd.cse110.habitizer.lib.data.InMemoryRoutineDataSource;
import edu.ucsd.cse110.habitizer.lib.data.InMemoryTaskDataSource;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.RoutineRepository;
import edu.ucsd.cse110.habitizer.lib.domain.Task;

public class HabitizerApplication extends Application {

    private RoutineRepository routineRepository;

    @Override
    public void onCreate() {
        super.onCreate()

        var routineDataSource = InMemoryRoutineDataSource.fromDefault();
        this.routineRepository = new RoutineRepository(routineDataSource);
    }

    public RoutineRepository getRoutineRepository() {
        return this.routineRepository;
    }
}
