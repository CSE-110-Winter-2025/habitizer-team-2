package edu.ucsd.cse110.habitizer.lib.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.Objects;
public class Task implements Serializable{

    private final @Nullable Integer id;
    private final int sortOrder;
    private final @NonNull String name;


    public Task(@Nullable Integer id, int sortOrder, @NonNull String name){
        this.id = id;
        this.sortOrder = sortOrder;
        this.name = name;
    }

    public @Nullable Integer id() { return id; }

    public @NonNull String name() { return name; }

    public int sortOrder() { return sortOrder; }

    public Task withId(int id){ return new Task(id, sortOrder, name); }

    public Task withSortOrder(int sortOrder) {
        return new Task(id, sortOrder, name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return sortOrder == task.sortOrder && Objects.equals(id, task.id) && Objects.equals(name, task.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sortOrder, name);
    }
}


