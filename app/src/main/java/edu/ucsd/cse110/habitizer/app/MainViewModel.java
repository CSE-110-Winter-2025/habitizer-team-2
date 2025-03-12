package edu.ucsd.cse110.habitizer.app;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

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
    private final MutableSubject<Integer> activeRoutineId;
    private final Subject<Routine> activeRoutine;
    private final Subject<List<Task>> activeRoutineOrderedTasks;

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
        this.activeRoutineId = new PlainMutableSubject<>();
        this.activeRoutine = Transformations.switchMap(activeRoutineId, repo::find);
        this.activeRoutineOrderedTasks = Transformations.map(activeRoutine, routine -> {
            return routine.tasks()
                    .stream()
                    .sorted(Comparator.comparingInt(Task::sortOrder))
                    .toList();
        });
//        this.activeRoutineOrderedTasks = Transformations.switchMap(activeRoutineId, repo::findTasksByRoutine);
    }

    // KICKS EVERYTHING OFF
    public void setActiveRoutine(int routineId) {
        this.activeRoutineId.setValue(routineId);
    }

    public Subject<List<Routine>> getOrderedRoutines() {
        return Transformations.map(repo.findAll(), unsorted -> {
            return unsorted.stream()
                    .sorted(Comparator.comparingInt(Routine::sortOrder))
                    .toList();
        });
    }

    public Subject<List<Task>> getOrderedTasks() {
        return activeRoutineOrderedTasks;
    }

    public Subject<String> getGoalTimeText() {
        return Transformations.map(activeRoutine, routine -> {
            return Integer.toString(routine.goalTime());
        });
    }
    public Subject<String> getRoutineName() {
        return Transformations.map(activeRoutine, routine -> {
            return routine.name();
        });
    }

    public Task getTask(int taskID){
        var tasks = getOrderedTasks().getValue();
        for(Task task : tasks){
            if(task.id() == taskID){return task;}
        }
        return null;
    }

    // retrieves a routine's repository based on the ID
    public Routine getRoutine(int routineID){
        return repo.find(routineID).getValue();
    }

    public Routine getActiveRoutine(){
        return this.activeRoutine.getValue();
    }

    public void checkOff(int taskID){
          repo.saveTask(getTask(taskID).withCheckedOff(true), activeRoutineId.getValue());

    }

    public void removeCheckOff(int taskID){
        repo.saveTask(getTask(taskID).withCheckedOff(false), activeRoutineId.getValue());
    }

    public void uncheckTasks(){
        var tasks = getOrderedTasks().getValue();
        if(tasks!=null){tasks.forEach( task -> {removeCheckOff(task.id());});}
    }

    public void setGoalTimeByRoutine(int routineId, int goalTime){
        repo.setGoalTime(routineId, goalTime);
    }

    public int getNumTasks(){
        return getOrderedTasks().getValue().size();
    }

    public boolean allTasksCompleted(){
        var tasks = getOrderedTasks().getValue();
        int numTasksCheckedOff = 0;
        for(Task task : tasks){if(task.checkedOff()){numTasksCheckedOff++;}};
        return numTasksCheckedOff == getNumTasks();
    }

// TODO : Everything below this!

    public void removeTask(int id, Routine routine) {
//        List<Task> updatedTasks = routine.tasks().stream()
//                .filter(task -> task.id() == null || task.id() != id) // Remove task with matching ID
//                .toList(); // Creates a new immutable list
//
//        Routine updatedRoutine = routine.withTasks(updatedTasks);

        repo.removeTask(id);
//        repo.save(updatedRoutine);
    }

    public void renameTask(int id, String name, Routine routine) {
//        List<Task> updatedTasks = routine.tasks().stream()
//                .map(task -> (task.id() != null && task.id() == id) ? task.withName(name) : task) // Rename the matching task
//                .toList(); // Creates a new immutable list
//
//        Routine updatedRoutine = routine.withTasks(updatedTasks);
//
//        repo.save(updatedRoutine);
        repo.renameTask(id, name);

    }

    public void appendTask(Task task, Routine routine) {
//        List<Task> updatedTasks = new ArrayList<>(routine.tasks());
//        updatedTasks.add(task); // Append the new task
//
//        Routine updatedRoutine = routine.withTasks(List.copyOf(updatedTasks));
//
        repo.appendTask(task, routine);
    }


    public void prependTask(Task task, Routine routine){
        repo.prependTask(task, routine);
    }
 
    public void swap(Integer taskID1, Integer taskID2){
        var task1 = getTask(taskID1);
        var task2 = getTask(taskID2);
        repo.swapTasks(taskID1, task2.sortOrder(), taskID2, task1.sortOrder());
    }

    // routine methods
    public void removeRoutine(int id) {
//        var tasks = getOrderedTasks().getValue();
//        var tasks = repo.findTasksByRoutine(id).getValue();
//        assert tasks!=null;
//        for (Task task: tasks){
//            repo.removeTask(task.id());
//            Log.d("Task", task.name());
//        }

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
