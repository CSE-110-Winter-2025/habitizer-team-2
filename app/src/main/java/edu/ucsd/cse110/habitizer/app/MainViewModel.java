package edu.ucsd.cse110.habitizer.app;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import edu.ucsd.cse110.habitizer.lib.domain.RoutineRepository;
import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.util.observables.PlainMutableSubject;

public class MainViewModel extends ViewModel {

    private final RoutineRepository routineRepository;
    private final Map<Integer, Routine> routines;

    // UI state

    // routine implementation
    private final PlainMutableSubject<List<Routine>> orderedRoutines;

    private final Map<Integer, PlainMutableSubject<List<Task>>> orderedTasksByRoutine;
    private final Map<Integer, PlainMutableSubject<Boolean>> isCheckedOffByRoutine;
    private final Map<Integer, PlainMutableSubject<Integer>> goalTimeByRoutine;
    private final Map<Integer, PlainMutableSubject<Double>> elapsedTimeByRoutine;
    private final Map<Integer, PlainMutableSubject<String>> displayedTextByRoutine;



    public static final ViewModelInitializer<MainViewModel> initializer =
            new ViewModelInitializer<>(
                    MainViewModel.class,
                    creationExtras -> {
                        var app = (HabitizerApplication) creationExtras.get(APPLICATION_KEY);
                        assert app != null;
                        return new MainViewModel(app.getRoutineRepository());
                    });

    public MainViewModel(RoutineRepository routineRepository) {
        this.routineRepository = routineRepository;
        this.routines = new HashMap<>();
        this.orderedRoutines = new PlainMutableSubject<>();
        this.orderedTasksByRoutine = new HashMap<>();
        this.isCheckedOffByRoutine = new HashMap<>();
        this.goalTimeByRoutine = new HashMap<>();
        this.elapsedTimeByRoutine = new HashMap<>();
        this.displayedTextByRoutine = new HashMap<>();

        // When the list of tasks changes (or is first loaded), reset the ordering.

        routineRepository.findAll().observe(routineList -> {
            if (routineList == null) return; // not ready yet, ignore

            routines.clear();
            orderedTasksByRoutine.clear();

            for (Routine routine : routineList) {
                int routineID = routine.id();
                routines.put(routineID, routine);


                // track ordered tasks per routine
                PlainMutableSubject<List<Task>> orderedTasks = new PlainMutableSubject<>();
                orderedTasksByRoutine.put(routineID, orderedTasks);

                // observe changes in tasks
                routine.findAll().observe(tasks -> {
                    if (tasks == null) return;
                    orderedTasks.setValue(tasks.stream()
                            .sorted(Comparator.comparingInt(Task::sortOrder))
                            .collect(Collectors.toList()));
                });

                // initialize routine specific states
                isCheckedOffByRoutine.put(routineID, new PlainMutableSubject<>());
                goalTimeByRoutine.put(routineID, new PlainMutableSubject<>());
                elapsedTimeByRoutine.put(routineID, new PlainMutableSubject<>());
                displayedTextByRoutine.put(routineID, new PlainMutableSubject<>());

                routine.findAll().observe(tasks -> {
                    if (tasks == null) return;
                    boolean allChecked = tasks.stream().allMatch(Task::checkedOff);
                    isCheckedOffByRoutine.get(routineID).setValue(allChecked);
                });

            }

            orderedRoutines.setValue(routineList.stream()
                    .sorted(Comparator.comparingInt(Routine::sortOrder))
                    .collect(Collectors.toList()));


        });

    }

    public PlainMutableSubject<List<Routine>> getOrderedRoutines() {
        return orderedRoutines;
    }


    public PlainMutableSubject<List<Task>> getOrderedTasks(int routineID) {
        return orderedTasksByRoutine.getOrDefault(routineID, new PlainMutableSubject<>());
    }

    // retrieves a routine's repository based on the ID
    public Routine getRoutine(int routineID){return routineRepository.findAll().getValue().get(routineID);}


    public void checkOff(int taskID, int routineID){
        var routine = routines.get(routineID);
        if (routine == null) return;

        var task = routine.find(taskID);
        if (task.getValue() == null) return;

        var checkedOffTask = new Task(task.getValue().id(), task.getValue().sortOrder(), task.getValue().name(), true);
        routine.save(checkedOffTask);
    }

    public void removeCheckOff(int taskID, int routineID){
        var routine = routines.get(routineID);
        if (routine == null) return;

        var task = routine.find(taskID);
        if (task.getValue() == null) return;

        var uncheckedTask = new Task(task.getValue().id(), task.getValue().sortOrder(), task.getValue().name(), false);
        routine.save(uncheckedTask);
    }


    public void removeTask(int id, Routine routine) {
        routine.remove(id);
    }

    public void renameTask(int id, String name, Routine routine) {
        routine.rename(id, name);
    }

    public void appendTask(Task task, Routine routine) {
        routine.append(task);
    }

    public void prependTask(Task task, Routine routine){
        routine.prepend(task);
    }

    /**
     * Removes a routine from the specified repository by its ID.
     * This method allows removing a routine from a repository other than the default one.
     * The routine and all its associated tasks will be permanently deleted.
     * UI components observing the specified repository will be automatically updated.
     *
     * @param id The unique identifier of the routine to be removed
     * @param routineRepository The specific repository from which to remove the routine
     */
    public void removeRoutine(int id, RoutineRepository routineRepository) {
        routineRepository.remove(id);
    }

    /**
     * Removes a routine from the repository by its ID.
     * This method permanently deletes the routine and all its associated tasks.
     * This removes a routine from the default routine repository.
     * @param id The unique identifier of the routine to be removed
     */
    public void removeRoutine(int id){
        routineRepository.remove(id);
    }
    public void renameRoutine(int id, String name, RoutineRepository routineRepository) {
        routineRepository.rename(id, name);
    }

    public void appendRoutine(Routine routine, RoutineRepository routineRepository) {
        routineRepository.append(routine);
    }

    public void prependRoutine(Routine routine, RoutineRepository routineRepository){
        routineRepository.prepend(routine);
    }

}
