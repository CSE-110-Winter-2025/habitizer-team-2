package edu.ucsd.cse110.habitizer.lib.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public record Routine(@Nullable Integer id,
                      int sortOrder,
                      @NonNull String name,
                      int goalTime,
                      @NonNull List<Task> tasks) {

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public Integer id() {
        return this.id;
    }

    @Override
    public List<Task> tasks() {
        return tasks;
    }

    public Routine withId(int id) {
        return new Routine(id, this.sortOrder, this.name, this.goalTime, this.tasks);
    }

    public Routine withName(String name) {
        return new Routine(this.id, this.sortOrder, name, this.goalTime, this.tasks);
    }

    public Routine withSortOrder(int sortOrder) {
        return new Routine(this.id, sortOrder, this.name, this.goalTime, this.tasks);
    }

    public Routine withGoalTime(int goalTime) {
        return new Routine(this.id, this.sortOrder, this.name, goalTime, this.tasks);
    }

    public Routine withTasks(List<Task> tasks) {
        return new Routine(this.id, this.sortOrder, this.name, goalTime, tasks);
    }

//    ///////////////////////// Manipulation tasks in routine below this point
//
//    public PlainMutableSubject<Task> find(int id) {
//        return dataSource.getTaskSubject(id);
//    }
//
//    public PlainMutableSubject<List<Task>> findAll() {
//        return dataSource.getAllTasksSubject();
//    }
//
//
//    public void save(Task task) {
//        dataSource.putTask(task);
//    }
//
//    public void save(List<Task> tasks) {
//        dataSource.putTasks(tasks);
//    }
//
//    public void saveAtSortOrder(Task task, int sortOrder){
//        dataSource.putTask(
//                task.withSortOrder(sortOrder)
//        );
//    }
//
//    public void remove(int id) {
//        dataSource.removeTask(id);
//    }
//
//    public void append(Task task){
//        dataSource.putTask(
//                task.withSortOrder(dataSource.getMaxSortOrder() + 1)
//        );
//    }
//
//    public void prepend(Task task) {
//        dataSource.shiftSortOrders(0, dataSource.getMaxSortOrder(), 1);
//        dataSource.putTask(
//                task.withSortOrder(dataSource.getMinSortOrder()-1)
//        );
//    }
//
//    public int getGoalTime(){
//        return dataSource.getGoalTime();
//    }
//
//    public void setGoalTime(int goalTime){
//        dataSource.setGoalTime(goalTime);
//    }
//
//    public void rename(int id, String name) {
//        dataSource.replaceName(id, name);
//    }
//


}
