package edu.ucsd.cse110.habitizer.app.data.db;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import edu.ucsd.cse110.habitizer.lib.domain.Routine;

@Entity(tableName = "routines")
public class RoutineEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    public Integer id;

    @ColumnInfo(name="name")
    public String name;

    @ColumnInfo(name="sort_order")
    public int sortOrder;

    @ColumnInfo(name="goal_time")
    public int goalTime;

    @Ignore
    RoutineEntity(@Nullable Integer id, @NonNull String name, int sortOrder, int goalTime){
        this.id = id;
        this.name = name;
        this.sortOrder = sortOrder;
        this.goalTime = goalTime;
    }

    public RoutineEntity(@NonNull String name, int sortOrder, int goalTime){
        this(null, name, sortOrder, goalTime);
    }


//    // TODO: HANDLE IN REPO
//    public static RoutineEntity fromRoutine (@NonNull Routine routine) {
//        var routineEntity = new RoutineEntity(routine.name(), routine.sortOrder(), routine.goalTime());
//        routineEntity.id = routine.id();
//        return routineEntity;
//    }

}
