package edu.ucsd.cse110.habitizer.app.data.db;


import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {RoutineEntity.class, TaskEntity.class}, version = 1, exportSchema = false)
public abstract class HabitizerDatabase extends RoomDatabase {
    public abstract RoutineDao routineDao();
    public abstract TaskDao taskDao();
}