package edu.ucsd.cse110.habitizer.app.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface RoutineDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(RoutineEntity routine);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insert(List<RoutineEntity> routine);

    @Query("SELECT * FROM routines WHERE id = :id")
    RoutineEntity find(int id);

    @Transaction
    @Query("SELECT * FROM routines WHERE id = :id")
    RoutineWithTasksQueryResult findWithTasks(int id);

    @Query("SELECT * FROM routines ORDER BY sort_order")
    List<RoutineEntity> findAll();

    @Query("SELECT * FROM routines WHERE id = :id")
    LiveData<RoutineEntity> findAsLiveData(int id);

    @Transaction
    @Query("SELECT * FROM routines WHERE id = :id")
    LiveData<RoutineWithTasksQueryResult> findWithTasksAsLiveData(int id);

    @Transaction
    @Query("SELECT * FROM routines ORDER BY sort_order")
    LiveData<List<RoutineWithTasksQueryResult>> findAllWithTasksAsLiveData();

    @Query("SELECT COUNT(*) FROM routines")
    int count();

    @Query("SELECT MIN(sort_order) FROM routines")
    int getMinSortOrder();

    @Query("SELECT MAX(sort_order) FROM routines")
    int getMaxSortOrder();

    @Query("UPDATE routines SET sort_order = sort_order + :by " +
            "WHERE sort_order >= :from AND sort_order <= :to")
    void shiftSortOrders(int from, int to, int by);

    @Transaction
    default int append(RoutineEntity routine) {
        var maxSortOrder = getMaxSortOrder();
        var newRoutine = new RoutineEntity(
                routine.name, maxSortOrder + 1, routine.goalTime
        );
        return Math.toIntExact(insert(newRoutine));
    }

    @Transaction
    default int prepend(RoutineEntity routine) {
        shiftSortOrders(getMinSortOrder(), getMaxSortOrder(), 1);
        var newRoutine = new RoutineEntity(
                routine.name, getMinSortOrder() - 1, routine.goalTime
        );
        return Math.toIntExact(insert(newRoutine));
    }

    @Query("DELETE FROM routines WHERE id = :id")
    void delete(int id);

    @Query("UPDATE routines SET name = :name " +
            "WHERE id = :id" )
    void rename(int id, String name);

    @Query("UPDATE routines SET goal_time = :goalTime WHERE id = :id")
    void setGoalTime(int id, int goalTime);
}
