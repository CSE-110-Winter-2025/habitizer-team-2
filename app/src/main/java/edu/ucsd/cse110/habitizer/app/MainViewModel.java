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
//    private final Map<Integer, Routine> routines;

    // UI state

    // routine implementation
    private final PlainMutableSubject<List<Routine>> orderedRoutines;

    Map<Integer, Routine> routineMap;

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
        this.routineMap = new HashMap<>();
        this.orderedRoutines = new PlainMutableSubject<>();
        this.orderedTasksByRoutine = new HashMap<>();
        this.isCheckedOffByRoutine = new HashMap<>();
        this.goalTimeByRoutine = new HashMap<>();
        this.elapsedTimeByRoutine = new HashMap<>();
        this.displayedTextByRoutine = new HashMap<>();

        // When the list of tasks changes (or is first loaded), reset the ordering.

        routineRepository.findAll().observe(routines -> {
            if (routines == null) return; // not ready yet, ignore

            var newOrderedRoutines = routines.stream()
                    .sorted(Comparator.comparingInt(Routine::sortOrder))
                    .collect(Collectors.toList());
            orderedRoutines.setValue(newOrderedRoutines);
            routines.forEach( routine -> {if(routine==null) return;
            routineMap.put(routine.id(),routine);});
        });

        routineRepository.findAll().getValue().forEach(routine -> {
            if (routine == null) return;

            PlainMutableSubject<List<Task>> orderedTasks = getOrderedTasks(routine.id());
            routine.findAll().observe(tasks->{
                if (tasks == null) return; // not ready yet, ignore

                PlainMutableSubject<Boolean> isCheckedOff = getIsCheckedOff(routine.id());

                var newOrderedTasks = tasks.stream()
                        .sorted(Comparator.comparingInt(Task::sortOrder))
                        .collect(Collectors.toList());
                orderedTasks.setValue(newOrderedTasks);

                orderedTasks.getValue().forEach( task -> {
                    if(task == null){return;}
                    isCheckedOff.setValue(task.checkedOff());
                });
            });
            orderedTasksByRoutine.put(routine.id(), orderedTasks);
        });


    }

    public PlainMutableSubject<List<Routine>> getOrderedRoutines() {
        return orderedRoutines;
    }

    public PlainMutableSubject<List<Task>> getOrderedTasks(int routineID) {
        if(orderedTasksByRoutine.get(routineID) == null){return new PlainMutableSubject<>();}
        return orderedTasksByRoutine.get(routineID);
    }

    // retrieves a routine's repository based on the ID
    public Routine getRoutine(int routineID){return routineMap.get(routineID);}

    public RoutineRepository getRoutineRepository(){return this.routineRepository;}

    public PlainMutableSubject<Boolean> getIsCheckedOff(int routineID){
        if(isCheckedOffByRoutine.get(routineID) == null){return new PlainMutableSubject<>();}
            return isCheckedOffByRoutine.get(routineID);
    }
    public void checkOff(int id, Routine routine){
        var task = routine.find(id);
        var checkedOffTask = new Task(task.getValue().id(), task.getValue().sortOrder(), task.getValue().name(), true);
        routine.save(checkedOffTask);
    }

    public void removeCheckOff(int id, Routine routine){
        var task = routine.find(id);
        var checkedOffTask = new Task(task.getValue().id(), task.getValue().sortOrder(), task.getValue().name(), false);
        routine.save(checkedOffTask);
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
     * Removes a routine from the repository by its ID.
     * This method permanently deletes the routine and all its associated tasks.
     * @param id The unique identifier of the routine to be removed
     * @param routineRepository The specific repository from which to remove the routine
     * @require routineRepository != null && id >= 0
     */
    public void removeRoutine(int id, RoutineRepository routineRepository) {
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
