package edu.ucsd.cse110.habitizer.app.domain;

import androidx.lifecycle.Transformations;

import java.util.List;
import java.util.stream.Collectors;

import edu.ucsd.cse110.habitizer.app.data.db.ConvertUtils;
import edu.ucsd.cse110.habitizer.app.data.db.HabitizerDatabase;
import edu.ucsd.cse110.habitizer.app.data.db.RoutineDao;
import edu.ucsd.cse110.habitizer.app.data.db.RoutineEntity;
import edu.ucsd.cse110.habitizer.app.data.db.TaskDao;
import edu.ucsd.cse110.habitizer.app.data.db.TaskEntity;
import edu.ucsd.cse110.habitizer.app.util.LiveDataSubjectAdapter;
import edu.ucsd.cse110.habitizer.lib.domain.RoutineRepository;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.util.observables.Subject;

public class RoomRoutineRepository implements RoutineRepository {

    private final HabitizerDatabase db;
    private final RoutineDao routineDao;
    private final TaskDao taskDao;

    public RoomRoutineRepository(HabitizerDatabase db) {
        this.db = db;
        this.routineDao = db.routineDao();
        this.taskDao = db.taskDao();
    }

    @Override
    public Subject<Routine> find(int id) {
        var queryResultLiveData = routineDao.findWithTasksAsLiveData(id);
        var routineLiveData = Transformations.map(queryResultLiveData, ConvertUtils::routineFromData);
        return new LiveDataSubjectAdapter<>(routineLiveData);
    }

    @Override
    public Subject<List<Routine>> findAll() {
        var queryResultsLiveData = routineDao.findAllWithTasksAsLiveData();
        var routinesLiveData = Transformations.map(queryResultsLiveData, (queryResults) -> {
            return queryResults.stream().map(ConvertUtils::routineFromData).toList();
        });
        return new LiveDataSubjectAdapter<>(routinesLiveData);
    }


    @Override
    public void save(Routine routine) {
        db.runInTransaction(() -> {
            // Handle each task...
            for (var task : routine.tasks()) {
                taskDao.insert(TaskEntity.fromTask(task));
            }
            routineDao.insert(ConvertUtils.dataFromRoutine(routine));
        });
    }

    @Override
    public void save(List<Routine> routines) {
        for (Routine routine : routines) {
            db.runInTransaction(() -> {
                // Handle each task of each routine
                for (var task: routine.tasks()) {
                    taskDao.insert(TaskEntity.fromTask(task));
                }
                routineDao.insert(ConvertUtils.dataFromRoutine(routine));
            });
        }
//
//        var entities = routines.stream()
//                .map(RoutineEntity::fromRoutine)
//                .collect(Collectors.toList());
//        routineDao.insert(entities);
    }

    @Override
    public void remove(int id) {
        routineDao.delete(id);
    }

    @Override
    public void append(Routine routine) {
        db.runInTransaction(() -> {
            // Handle each task...
            for (var task : routine.tasks()) {
                taskDao.append(TaskEntity.fromTask(task));
            }
            routineDao.append(ConvertUtils.dataFromRoutine(routine));
        });
    }

    @Override
    public void prepend(Routine routine) {
        db.runInTransaction(() -> {
            // Handle each task...
            for (var task : routine.tasks()) {
                taskDao.prepend(TaskEntity.fromTask(task));
            }
            routineDao.prepend(ConvertUtils.dataFromRoutine(routine));
        });
    }

    @Override
    public void rename(int id, String name) {
        routineDao.rename(id, name);
    }


    public Subject<Task> findTask(int id) {
        var entityLiveData = taskDao.findAsLiveData(id);
        var taskLiveData = Transformations.map(entityLiveData, ConvertUtils::taskFromData);
        return new LiveDataSubjectAdapter<>(taskLiveData);
    }

    public Subject<List<Task>> findAllTasks() {
        var entitiesLiveData = taskDao.findAllAsLiveData();
        var tasksLiveData = Transformations.map(entitiesLiveData, entities -> {
            return entities.stream()
                    .map(TaskEntity::toTask)
                    .collect(Collectors.toList());
        });
        return new LiveDataSubjectAdapter<>(tasksLiveData);
    }

    public Subject<List<Task>> findTasksByRoutine(int routineId) {
        var entitiesLiveData = taskDao.findAllByRoutineAsLiveData(routineId);
        var tasksLiveData = Transformations.map(entitiesLiveData, entities -> {
            return entities.stream()
                    .map(TaskEntity::toTask)
                    .collect(Collectors.toList());
        });
        return new LiveDataSubjectAdapter<>(tasksLiveData);
    }

    public void saveTask(Task task) {
        taskDao.insert(TaskEntity.fromTask(task));
    }

    public void saveTask(List<Task> tasks) {
        var entities = tasks.stream()
                .map(TaskEntity::fromTask)
                .collect(Collectors.toList());
        taskDao.insert(entities);
    }

    public void removeTask(int id) {
        taskDao.delete(id);
    }

    public void appendTask(Task task) {
        taskDao.append(TaskEntity.fromTask(task));
    }

    public void prependTask(Task task) {
        taskDao.prepend(TaskEntity.fromTask(task));
    }

    public void renameTask(int id, String name) {
        taskDao.rename(id, name);
    }

    public void setGoalTime(int routineId, int goalTime){
        routineDao.setGoalTime(routineId, goalTime);
    }
}
