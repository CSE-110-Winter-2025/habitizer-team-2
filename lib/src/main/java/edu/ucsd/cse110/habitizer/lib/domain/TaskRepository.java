package edu.ucsd.cse110.habitizer.lib.domain;

import java.util.List;

import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.habitizer.lib.util.observables.PlainMutableSubject;

public class TaskRepository {
    private final InMemoryDataSource dataSource;

    public TaskRepository(InMemoryDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public PlainMutableSubject<Task> find(int id) {
        return dataSource.getTaskSubject(id);
    }

    public PlainMutableSubject<List<Task>> findAll() {
        return dataSource.getAllTasksSubject();
    }


    public void save(Task task) {
        dataSource.putTask(task);
    }

    public void save(List<Task> tasks) {
        dataSource.putTasks(tasks);
    }

    public void saveAtSortOrder(Task task, int sortOrder){
        dataSource.putTask(
                task.withSortOrder(sortOrder)
        );
    }

    public void remove(int id) {
        dataSource.removeTask(id);
    }

    public void append(Task task){
        dataSource.putTask(
                task.withSortOrder(dataSource.getMaxSortOrder() + 1)
        );
    }

    public void prepend(Task task) {
        dataSource.shiftSortOrders(0, dataSource.getMaxSortOrder(), 1);
        dataSource.putTask(
                task.withSortOrder(dataSource.getMinSortOrder()-1)
        );
    }


    public void rename(int id, String name) {
        dataSource.replaceName(id, name);
    }
}
