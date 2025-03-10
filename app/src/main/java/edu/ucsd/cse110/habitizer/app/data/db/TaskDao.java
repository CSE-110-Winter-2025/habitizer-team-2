package edu.ucsd.cse110.habitizer.app.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(TaskEntity task);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insert(List<TaskEntity> routine);

    @Query("SELECT * FROM tasks WHERE id = :id")
    TaskEntity find(int id);

    @Query("SELECT * FROM tasks ORDER BY sort_order")
    List<TaskEntity> findAll();

    @Query("SELECT * FROM tasks WHERE routine_id = :routineId")
    List<TaskEntity> findAllByRoutine(int routineId);

    @Query("SELECT * FROM tasks WHERE id = :id")
    LiveData<TaskEntity> findAsLiveData(int id);

    @Query("SELECT * FROM tasks ORDER BY sort_order")
    LiveData<List<TaskEntity>> findAllAsLiveData();

    @Query("SELECT * FROM tasks WHERE routine_id = :routineId")
    LiveData<List<TaskEntity>> findAllByRoutineAsLiveData(int routineId);

    @Query("SELECT COUNT(*) FROM tasks")
    int count();

    @Query("SELECT MIN(sort_order) FROM tasks WHERE routine_id = :routineId")
    int getMinSortOrder(Integer routineId);

    @Query("SELECT MAX(sort_order) FROM tasks WHERE routine_id = :routineId")
    int getMaxSortOrder(Integer routineId);

    @Query("UPDATE tasks SET sort_order = sort_order + :by " +
            "WHERE sort_order >= :from AND sort_order <= :to " +
            "AND routine_id = :routineId")
    void shiftSortOrders(int from, int to, int by, Integer routineId);

    @Transaction
    default int append(TaskEntity task) {
        var maxSortOrder = getMaxSortOrder(task.routineId);
        var newTask = new TaskEntity(
                maxSortOrder + 1, task.name, task.checkedOff, task.routineId
        );
        return Math.toIntExact(insert(newTask));
    }

    @Transaction
    default int prepend(TaskEntity task) {
        shiftSortOrders(getMinSortOrder(task.routineId), getMaxSortOrder(task.routineId), 1, task.routineId);
        var newTask = new TaskEntity(
                getMinSortOrder(task.routineId) - 1, task.name, task.checkedOff, task.routineId
        );
        return Math.toIntExact(insert(newTask));
    }

    @Query("DELETE FROM tasks WHERE id = :id")
    void delete(int id);

    @Query("UPDATE tasks SET name = :name " +
            "WHERE id = :id" )
    void rename(int id, String name);

}
