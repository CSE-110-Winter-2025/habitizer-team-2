package edu.ucsd.cse110.habitizer.app;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;
import androidx.room.Transaction;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import edu.ucsd.cse110.habitizer.app.domain.RoomRoutineRepository;
import edu.ucsd.cse110.habitizer.app.util.MutableLiveDataSubjectAdapter;
import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.util.observables.MutableSubject;
import edu.ucsd.cse110.habitizer.lib.util.observables.PlainMutableSubject;
import edu.ucsd.cse110.habitizer.lib.util.observables.Subject;
import edu.ucsd.cse110.habitizer.lib.util.observables.Transformations;

public class MainViewModel extends ViewModel {

    private final RoomRoutineRepository repo;

    public static final ViewModelInitializer<MainViewModel> initializer =
            new ViewModelInitializer<>(
                    MainViewModel.class,
                    creationExtras -> {
                        var app = (HabitizerApplication) creationExtras.get(APPLICATION_KEY);
                        assert app != null;
                        return new MainViewModel(app.getRoutineRepository());
                    });

    public MainViewModel(RoomRoutineRepository routineRepository) {
        this.repo = routineRepository;

//        this.activeRoutineOrderedTasks = Transformations.switchMap(activeRoutineId, repo::findTasksByRoutine);
    }

    // KICKS EVERYTHING OFF

    public Subject<List<Routine>> getOrderedRoutines() {
        return Transformations.map(repo.findAll(), unsorted -> {
            return unsorted.stream()
                    .sorted(Comparator.comparingInt(Routine::sortOrder))
                    .toList();
        });
    }

    public Subject<Routine> getRoutine(int routineID){
        return Transformations.map(repo.find(routineID), routine ->{
            return routine;
        });
    }

    public Subject<String> getRoutineName(int routineID) {
        return Transformations.map(getRoutine(routineID), routine ->{return routine.name();} );
    }

    public Subject<List<Task>> getOrderedTasks(int routineID) {
        return Transformations.map(repo.findTasksByRoutine(routineID), unsorted -> {
            return unsorted.stream()
                    .sorted(Comparator.comparingInt(Task::sortOrder))
                    .toList();
        });
    }

    public Subject<String> getGoalTimeText(int routineID) {
        return Transformations.map(getRoutine(routineID), routine -> {
            return Integer.toString(routine.goalTime());
        });
    }

    public Subject<Task> getTask(int taskID){
        return Transformations.map(repo.findTask(taskID), task -> {return task;});
    }


    public void checkOff(int taskID){
            repo.checkoff(taskID);
    }

    public void removeCheckOff(int taskID){
        repo.uncheckoff(taskID);
    }

    public void uncheckTasks(int routineID){
        repo.uncheckoffall(routineID);
    }

    public void setGoalTimeByRoutine(int routineId, int goalTime){
        repo.setGoalTime(routineId, goalTime);
    }

    public int getNumTasks(int routineID){
        return getOrderedTasks(routineID).getValue().size();
    }

    public boolean allCheckedOff(int routineID){
        return repo.allCheckedOff(routineID);
    }

// TODO : Everything below this!

    public void removeTask(int id) {
//        List<Task> updatedTasks = routine.tasks().stream()
//                .filter(task -> task.id() == null || task.id() != id) // Remove task with matching ID
//                .toList(); // Creates a new immutable list
//
//        Routine updatedRoutine = routine.withTasks(updatedTasks);

        repo.removeTask(id);
//        repo.save(updatedRoutine);
    }

    public void renameTask(int id, String name) {
//        List<Task> updatedTasks = routine.tasks().stream()
//                .map(task -> (task.id() != null && task.id() == id) ? task.withName(name) : task) // Rename the matching task
//                .toList(); // Creates a new immutable list
//
//        Routine updatedRoutine = routine.withTasks(updatedTasks);
//
//        repo.save(updatedRoutine);
        repo.renameTask(id, name);

    }

    public void appendTask(Task task, int routineID) {
        repo.appendTask(task, routineID);
    }


    public void prependTask(Task task, int routineID){
        repo.prependTask(task, routineID);
    }
 
    public void swap(Integer taskID1, Integer sortorder1, Integer taskID2, Integer sortorder2){
        repo.swapTasks(taskID1, sortorder1, taskID2, sortorder2);
    }

    // routine methods
    public void removeRoutine(int id) {
        repo.remove(id);
    }

    public void renameRoutine(int id, String name) {
        repo.rename(id, name);
    }

    public void appendRoutine(Routine routine) {
        repo.append(routine);
    }

    public void prependRoutine(Routine routine){
        repo.prepend(routine);
    }

}
