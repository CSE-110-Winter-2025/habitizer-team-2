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

}
