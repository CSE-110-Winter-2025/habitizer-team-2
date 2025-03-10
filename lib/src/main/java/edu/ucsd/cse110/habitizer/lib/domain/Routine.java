package edu.ucsd.cse110.habitizer.lib.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import edu.ucsd.cse110.habitizer.lib.data.InMemoryTaskDataSource;
import edu.ucsd.cse110.habitizer.lib.util.observables.PlainMutableSubject;

public class Routine implements Serializable {

    private final @Nullable Integer id;
    private final int sortOrder;
    private final @NonNull String name;
    private final InMemoryTaskDataSource dataSource;

    public Routine(@Nullable Integer id, int sortOrder, @NonNull String name, @NonNull InMemoryTaskDataSource dataSource) {
        this.id = id;
        this.name = name;
        this.dataSource = dataSource;
        this.sortOrder = sortOrder;
    }

    public String name(){return this.name;}

    public Integer id(){return this.id;}

    public int sortOrder(){return this.sortOrder;}

    public InMemoryTaskDataSource dataSource(){return this.dataSource;}

    public Routine withId(int id){return new Routine(id, this.sortOrder, this.name, this.dataSource);}

    public Routine withName(String name){ return new Routine(this.id, this.sortOrder, name, this.dataSource);}

    public Routine withSortOrder(int sortOrder){return new Routine(this.id, sortOrder, this.name, this.dataSource);}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Routine routine = (Routine) o;
        return sortOrder == routine.sortOrder && Objects.equals(id, routine.id) && Objects.equals(name, routine.name) && Objects.equals(dataSource, routine.dataSource);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sortOrder, name, dataSource);
    }


    ///////////////////////// Manipulation tasks in routine below this point

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
        if(dataSource.getTasks().isEmpty()){
            dataSource.putTask(task.withSortOrder(0)); return;}
        dataSource.putTask(
                task.withSortOrder(dataSource.getMaxSortOrder() + 1)
        );
    }

    public void prepend(Task task) {
        if(dataSource.getTasks().isEmpty()){
            dataSource.putTask(task.withSortOrder(0)); return;}
        dataSource.shiftSortOrders(0, dataSource.getMaxSortOrder(), 1);
        dataSource.putTask(
                task.withSortOrder(dataSource.getMinSortOrder()-1)
        );
    }

    public void swapTasks(long id1, long id2){
        dataSource.swapSortOrders(Math.toIntExact(id1), Math.toIntExact(id2)); //added to communicate to task data source
    }

    public int getGoalTime(){
        return dataSource.getGoalTime();
    }

    public void setGoalTime(int goalTime){
        dataSource.setGoalTime(goalTime);
    }

    public void rename(int id, String name) {
        dataSource.replaceName(id, name);
    }

}
