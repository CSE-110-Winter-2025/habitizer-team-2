package edu.ucsd.cse110.habitizer.app.data.db;

import androidx.lifecycle.Transformations;

import java.util.List;
import java.util.stream.Collectors;

import edu.ucsd.cse110.habitizer.app.util.LiveDataSubjectAdapter;
import edu.ucsd.cse110.habitizer.lib.domain.RoutineRepository;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.util.observables.PlainMutableSubject;
import edu.ucsd.cse110.habitizer.lib.util.observables.Subject;

public class RoomRoutineRepository implements RoutineRepository {

    private final RoutineDao routineDao;
    private final TaskDao taskDao;
    public RoomRoutineRepository(RoutineDao routineDao, TaskDao taskDao) {
        this.routineDao = routineDao;
        this.taskDao = taskDao;
    }


    @Override
    public Subject<Routine> find(int id) {
        var entityLiveData = routineDao.findAsLiveData(id);
        var routineLiveData = Transformations.map(entityLiveData, RoutineEntity::toRoutine);
        return new LiveDataSubjectAdapter<>(routineLiveData);
    }

    public Subject<Task> findTask(int id) {
        var entityLiveData = taskDao.findAsLiveData(id);
        var taskLiveData = Transformations.map(entityLiveData, TaskEntity::toTask);
        return new LiveDataSubjectAdapter<>(taskLiveData);
    }

    @Override
    public Subject<List<Routine>> findAll() {
        var entitiesLiveData = routineDao.findAllAsLiveData();
        var routinesLiveData = Transformations.map(entitiesLiveData, entities -> {
            return entities.stream()
                    .map(RoutineEntity::toRoutine)
                    .collect(Collectors.toList());
        });
        return new LiveDataSubjectAdapter<>(routinesLiveData);
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



    @Override
    public void save(Routine routine) {
        routineDao.insert(RoutineEntity.fromRoutine(routine));
    }

    public void saveTask(Task task) {
        taskDao.insert(TaskEntity.fromTask(task));
    }

    @Override
    public void save(List<Routine> routines) {
        var entities = routines.stream()
                .map(RoutineEntity::fromRoutine)
                .collect(Collectors.toList());
        routineDao.insert(entities);
    }

    public void saveTask(List<Task> tasks) {
        var entities = tasks.stream()
                .map(TaskEntity::fromTask)
                .collect(Collectors.toList());
        taskDao.insert(entities);
    }

    @Override
    public void remove(int id) {
        routineDao.delete(id);
    }

    public void removeTask(int id) {
        taskDao.delete(id);
    }

    @Override
    public void append(Routine routine) {
        routineDao.append(RoutineEntity.fromRoutine(routine));
    }

    public void appendTask(Task task) {
        taskDao.append(TaskEntity.fromTask(task));
    }

    @Override
    public void prepend(Routine routine) {
        routineDao.prepend(RoutineEntity.fromRoutine(routine));
    }

    public void prependTask(Task task) {
        taskDao.prepend(TaskEntity.fromTask(task));
    }

    @Override
    public void rename(int id, String name) {
        routineDao.rename(id, name);
    }

    public void renameTask(int id, String name) {
        taskDao.rename(id, name);
    }
}
