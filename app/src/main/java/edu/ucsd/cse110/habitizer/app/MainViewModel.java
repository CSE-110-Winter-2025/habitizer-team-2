package edu.ucsd.cse110.habitizer.app;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import edu.ucsd.cse110.habitizer.app.domain.RoomRoutineRepository;
import edu.ucsd.cse110.habitizer.lib.data.InMemoryTaskDataSource;
import edu.ucsd.cse110.habitizer.lib.domain.RoutineRepository;
import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.util.observables.PlainMutableSubject;

public class MainViewModel extends ViewModel {

    private final RoomRoutineRepository routineRepository;
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

    public MainViewModel(RoomRoutineRepository routineRepository) {
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

                // track ordered tasks per routine
                PlainMutableSubject<List<Task>> orderedTasks = new PlainMutableSubject<>();
                orderedTasksByRoutine.put(routine.id(), orderedTasks);

                // observe changes in tasks
                routineRepository.findTasksByRoutine(routine.id()).observe(tasks -> {
                    if (tasks == null) return;
                    orderedTasks.setValue(tasks.stream()
                            .sorted(Comparator.comparingInt(Task::sortOrder))
                            .collect(Collectors.toList()));
                });

                // initialize routine specific states
                isCheckedOffByRoutine.put(routine.id(), new PlainMutableSubject<>());
                goalTimeByRoutine.put(routine.id(), new PlainMutableSubject<>());
                elapsedTimeByRoutine.put(routine.id(), new PlainMutableSubject<>());
                displayedTextByRoutine.put(routine.id(), new PlainMutableSubject<>());

                routineRepository.findTasksByRoutine(routine.id()).observe(tasks -> {
                    if (tasks == null) return;
                    boolean allChecked = tasks.stream().allMatch(Task::checkedOff);
                    isCheckedOffByRoutine.get(routine.id()).setValue(allChecked);
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
    public Routine getRoutine(int routineID){
        return routineRepository.findAll().getValue().get(routineID);
    }

    public void checkOff(int taskID, int routineID){
        var routine = routines.get(routineID);
        Objects.requireNonNull(routine);

        var task = routineRepository.findTask(taskID).getValue();
        Objects.requireNonNull(task);

        routineRepository.saveTask(task.withCheckedOff(true));
    }

    public void removeCheckOff(int taskID, int routineID){
        var routine = routines.get(routineID);
        Objects.requireNonNull(routine);

        var task = routineRepository.findTask(taskID).getValue();
        Objects.requireNonNull(task);

        routineRepository.saveTask(task.withCheckedOff(false));
    }

    public void setGoalTimeByRoutine(int routineId, int goalTime){
        routineRepository.setGoalTime(routineId, goalTime);
    }

// TODO : Everything below this!

    public void removeTask(int id, Routine routine) {
        List<Task> updatedTasks = routine.tasks().stream()
                .filter(task -> task.id() == null || task.id() != id) // Remove task with matching ID
                .toList(); // Creates a new immutable list

        Routine updatedRoutine = routine.withTasks(updatedTasks);

        routineRepository.removeTask(id);
        routineRepository.save(updatedRoutine);
    }

    public void renameTask(int id, String name, Routine routine) {
//        List<Task> updatedTasks = routine.tasks().stream()
//                .map(task -> (task.id() != null && task.id() == id) ? task.withName(name) : task) // Rename the matching task
//                .toList(); // Creates a new immutable list
//
//        Routine updatedRoutine = routine.withTasks(updatedTasks);
//
//        routine = updatedRoutine;

        routineRepository.renameTask(id, name);

    }

    public void appendTask(Task task, Routine routine) {
        routineRepository.appendTask(task);
    }

    public void prependTask(Task task, Routine routine){
        routineRepository.prependTask(task);
    }

    // routine methods
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
