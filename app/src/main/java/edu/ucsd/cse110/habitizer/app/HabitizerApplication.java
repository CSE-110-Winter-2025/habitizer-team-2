package edu.ucsd.cse110.habitizer.app;

import android.app.Application;

import androidx.room.Room;

import edu.ucsd.cse110.habitizer.app.data.db.HabitizerDatabase;
import edu.ucsd.cse110.habitizer.app.domain.RoomRoutineRepository;
import edu.ucsd.cse110.habitizer.lib.data.InMemoryRoutineDataSource;
import edu.ucsd.cse110.habitizer.lib.data.InMemoryTaskDataSource;


public class HabitizerApplication extends Application {

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

            sharedPreferences.edit()
                    .putBoolean("isFirstRun", false)
                    .apply();
        }

    }

    public RoomRoutineRepository getRoutineRepository() {
        return this.routineRepository;
    }
}
