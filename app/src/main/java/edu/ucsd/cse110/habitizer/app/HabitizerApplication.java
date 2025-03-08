package edu.ucsd.cse110.habitizer.app;

import android.app.Application;

import androidx.room.Room;

import edu.ucsd.cse110.habitizer.app.data.db.HabitizerDatabase;
import edu.ucsd.cse110.habitizer.app.domain.RoomRoutineRepository;
import edu.ucsd.cse110.habitizer.lib.data.InMemoryRoutineDataSource;
import edu.ucsd.cse110.habitizer.lib.data.InMemoryTaskDataSource;

public class HabitizerApplication extends Application {
    private InMemoryRoutineDataSource routineDataSource;

    private RoomRoutineRepository routineRepository;



    @Override
    public void onCreate() {
        super.onCreate();

        var database = Room.databaseBuilder(
                        getApplicationContext(),
                        HabitizerDatabase.class,
                        "habitizer-database"
                )
                .allowMainThreadQueries()
                .build();

        this.routineRepository = new RoomRoutineRepository(database);

//        Populate the database with some initial data on the first run.
        var sharedPreferences = getSharedPreferences("habitizer", MODE_PRIVATE);
        var isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);

        if (isFirstRun && database.routineDao().count() == 0 && database.taskDao().count() == 0){
            routineRepository.save(InMemoryRoutineDataSource.DEFAULT_ROUTINES);
//            routineRepository.saveTask(InMemoryTaskDataSource.DEFAULT_TASKS_MORNING);
//            routineRepository.saveTask(InMemoryTaskDataSource.DEFAULT_TASKS_EVENING);

            sharedPreferences.edit()
                    .putBoolean("isFirstRun", false)
                    .apply();
        }

//        var morningDataSource = InMemoryTaskDataSource.fromDefaultMorning();
//        var eveningDataSource = InMemoryTaskDataSource.fromDefaultEvening();
//
//        var morningRoutine = new Routine(0, 0, "Morning Routine", 45, morningDataSource);
//        var eveningRoutine = new Routine(1, 1, "Evening Routine", 45 ,eveningDataSource);
//
//        var routineDataSource = InMemoryRoutineDataSource.fromDefault();
//        this.routineRepository = new SimpleRoutineRepository(routineDataSource);
//
//        routineRepository.append(morningRoutine);
//        routineRepository.append(eveningRoutine);
//
//        routineRepository.append(new Routine(2, 2, "Custom Routine", 45, InMemoryTaskDataSource.fromDefaultNew()));
    }

    public RoomRoutineRepository getRoutineRepository() {
        return this.routineRepository;
    }
}
