package edu.ucsd.cse110.habitizer.app;

import android.app.Application;

import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.habitizer.lib.domain.TaskRepository;

public class HabitizerApplication extends Application {
    private InMemoryDataSource morningDataSource;

    private InMemoryDataSource eveningDataSource;
    private TaskRepository morningTaskRepository;
    private TaskRepository eveningTaskRepository;

    @Override
    public void onCreate() {
        super.onCreate();

        this.morningDataSource = InMemoryDataSource.fromDefaultMorning();
        this.eveningDataSource = InMemoryDataSource.fromDefaultEvening();
        this.morningTaskRepository = new TaskRepository(morningDataSource);
        this.eveningTaskRepository = new TaskRepository(eveningDataSource);
    }

    public TaskRepository getMorningTaskRepository() {return morningTaskRepository;}
    public TaskRepository getEveningTaskRepository() {return eveningTaskRepository;}
}
