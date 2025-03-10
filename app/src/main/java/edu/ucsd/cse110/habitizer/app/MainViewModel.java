package edu.ucsd.cse110.habitizer.app;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import edu.ucsd.cse110.habitizer.app.domain.RoomRoutineRepository;
import edu.ucsd.cse110.habitizer.lib.domain.RoutineRepository;
import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.util.observables.PlainMutableSubject;
import edu.ucsd.cse110.habitizer.lib.util.observables.Subject;
import edu.ucsd.cse110.habitizer.lib.util.observables.Transformations;

public class MainViewModel extends ViewModel {

    private final RoomRoutineRepository repo;

    // UI state

    // routine implementation


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
    }

    public Subject<List<Routine>> getOrderedRoutines() {
        // Equivalent Code to Map below:
        //
        // var result = new PlainMutableSubject<List<Routine>>();
        // repo.findAll().observe(input -> {
        //     var output = input.stream().sorted().toList();
        //     result.setValue(output);
        // })

        return Transformations.map(repo.findAll(), unsorted -> {
            return unsorted.stream().sorted().toList();
        });
    }


    public Subject<List<Task>> getOrderedTasks(int routineID) {
        return Transformations.map(repo.findTasksByRoutine(routineID), unsorted -> {
            return unsorted.stream().sorted().toList();
        });
    }

    // retrieves a routine's repository based on the ID
    public Routine getRoutine(int routineID){
        return repo.find(routineID).getValue();
    }

    public void checkOff(int taskID, int routineID){
//        var routine = repo.find(routineID);
//        Objects.requireNonNull(routine);

        var task = repo.findTask(taskID).getValue();
        Objects.requireNonNull(task);

        repo.saveTask(task.withCheckedOff(true), routineID);
    }

    public void removeCheckOff(int taskID, int routineID){
//        var routine = repo.find(routineID).getValue();
//        Objects.requireNonNull(routine);

        var task = repo.findTask(taskID).getValue();
        Objects.requireNonNull(task);

        repo.saveTask(task.withCheckedOff(false), routineID);
    }

    public void setGoalTimeByRoutine(int routineId, int goalTime){
        repo.setGoalTime(routineId, goalTime);
    }

// TODO : Everything below this!

    public void removeTask(int id, Routine routine) {
        List<Task> updatedTasks = routine.tasks().stream()
                .filter(task -> task.id() == null || task.id() != id) // Remove task with matching ID
                .toList(); // Creates a new immutable list

        Routine updatedRoutine = routine.withTasks(updatedTasks);

        repo.removeTask(id);
        repo.save(updatedRoutine);
    }

    public void renameTask(int id, String name, Routine routine) {
        List<Task> updatedTasks = routine.tasks().stream()
                .map(task -> (task.id() != null && task.id() == id) ? task.withName(name) : task) // Rename the matching task
                .toList(); // Creates a new immutable list

        Routine updatedRoutine = routine.withTasks(updatedTasks);

        repo.save(updatedRoutine);
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

    // routine methods
    public void removeRoutine(int id) {
        repo.remove(id);
        var tasks = repo.findTasksByRoutine(id).getValue();
        for (Task task: tasks){
            repo.removeTask(task.id());
        }
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
