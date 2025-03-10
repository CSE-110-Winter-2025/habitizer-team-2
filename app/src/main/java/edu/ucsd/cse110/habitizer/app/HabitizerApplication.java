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
        super.onCreate();

        var routineDataSource = InMemoryRoutineDataSource.fromDefault();
        this.routineRepository = new RoutineRepository(routineDataSource);
        this.routineRepository.append(new Routine(2,2,"hockey", InMemoryTaskDataSource.fromDefaultNew()));
        this.routineRepository.append(new Routine(3,3,"baby", InMemoryTaskDataSource.fromDefaultNew()));
        this.routineRepository.findAll().getValue().get(2).prepend(new Task(0,0,"asdf",false));
    }

    public RoutineRepository getRoutineRepository() {
        return this.routineRepository;
    }
}
