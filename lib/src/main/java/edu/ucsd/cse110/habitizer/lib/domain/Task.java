package edu.ucsd.cse110.habitizer.lib.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public record Task(
        @Nullable Integer id,
        int sortOrder,
        @NonNull String name,
        boolean checkedOff
) {
    public Task withId(int id) {
        return new Task(id, this.sortOrder, this.name, this.checkedOff);
    }

    public Task withName(String name) {
        return new Task(this.id, this.sortOrder, name, this.checkedOff);
    }

    public Task withSortOrder(int sortOrder) {
        return new Task(this.id, sortOrder, this.name, this.checkedOff);
    }

    public Task withCheckedOff(boolean checkedOff) {
        return new Task(this.id, this.sortOrder, this.name, checkedOff);
    }
}


