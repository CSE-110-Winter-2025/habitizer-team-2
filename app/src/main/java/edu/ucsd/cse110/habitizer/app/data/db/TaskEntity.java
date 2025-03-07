package edu.ucsd.cse110.habitizer.app.data.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import edu.ucsd.cse110.habitizer.lib.domain.Task;

@Entity(tableName = "tasks",
        foreignKeys = @ForeignKey(entity = RoutineEntity.class,
                parentColumns = "id",
                childColumns = "routine_id",
                onDelete = ForeignKey.CASCADE),
        indices = {@Index(value = "routine_id")})
public class TaskEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public Integer id;


    @ColumnInfo(name="sort_order")
    public int sortOrder;

    @ColumnInfo(name="name")
    public String name;

    @ColumnInfo(name="checked_off")
    public Boolean checkedOff;

    @ColumnInfo(name="routine_id")
    public Integer routineId;

    TaskEntity(int sortOrder, @NonNull String name, @NonNull Boolean checkedOff, @NonNull Integer routineId){
        this.sortOrder = sortOrder;
        this.name = name;
        this.checkedOff = checkedOff;
        this.routineId = routineId;
    }

    public static TaskEntity fromTask (Task task){
        var taskEntity = new TaskEntity(task.sortOrder(), task.name(), task.checkedOff(), -1);
        taskEntity.id = task.id();
        return taskEntity;
    }

    public @NonNull Task toTask(){
        return new Task(id, sortOrder, name, checkedOff, routineId);
    }

}
