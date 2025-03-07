package edu.ucsd.cse110.habitizer.app.data.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import edu.ucsd.cse110.habitizer.lib.domain.Routine;

@Entity(tableName = "routines")
public class RoutineEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    public Integer id = null;

    @ColumnInfo(name="name")
    public String name;

    @ColumnInfo(name="sort_order")
    public int sortOrder;

    @ColumnInfo(name="goal_time")
    public int goalTime;

    RoutineEntity(@NonNull String name, int sortOrder, int goalTime){
        this.name = name;
        this.sortOrder = sortOrder;
        this.goalTime = goalTime;
    }

    public static RoutineEntity fromRoutine (@NonNull Routine routine) {
        var routineEntity = new RoutineEntity(routine.name(), routine.sortOrder(), routine.goalTime());
        routineEntity.id = routine.id();
        return routineEntity;
    }

    public @NonNull Routine toRoutine(){
        return new Routine(id, sortOrder, name, goalTime, null);
    }
}
