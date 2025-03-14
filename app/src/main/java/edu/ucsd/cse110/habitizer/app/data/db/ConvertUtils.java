package edu.ucsd.cse110.habitizer.app.data.db;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.Task;

public class ConvertUtils {
    public static Task taskFromData(TaskEntity entity) {
        return new Task(
                entity.id,
                entity.sortOrder,
                entity.name,
                entity.checkedOff
        );
    }

    public static Routine routineFromData(RoutineWithTasksQueryResult data) {
        if(data == null){
            return new Routine(null,-1,"",0,new ArrayList<>());
        }
        return new Routine(
                data.routine.id,
                data.routine.sortOrder,
                data.routine.name,
                data.routine.goalTime,
                data.tasks.stream().map(ConvertUtils::taskFromData).toList()
        );
    }

    public static RoutineEntity dataFromRoutine(@NonNull Routine routine) {
        return new RoutineEntity(
                routine.id(),
                routine.name(),
                routine.sortOrder(),
                routine.goalTime()
        );
    }


}
