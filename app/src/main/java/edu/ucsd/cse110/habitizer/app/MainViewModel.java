package edu.ucsd.cse110.habitizer.app;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import edu.ucsd.cse110.habitizer.app.data.db.RoomRoutineRepository;
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

                //pull routine's tasks from database
                var tasksForRoutine = routineRepository.findTasksByRoutine(routine.id());

                //Set the routine's task list to be equal to said routines
                InMemoryTaskDataSource routineTaskData = new InMemoryTaskDataSource();
                routineTaskData.putTasks(tasksForRoutine.getValue());
                routine.setDataSource(routineTaskData);
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

        var checkedOffTask = new Task(task.getValue().id(), task.getValue().sortOrder(), task.getValue().name(), true, routineID);
        routine.save(checkedOffTask);
    }

    public void removeCheckOff(int taskID, int routineID){
        var routine = routines.get(routineID);
        if (routine == null) return;

        var task = routine.find(taskID);
        if (task.getValue() == null) return;

        var uncheckedTask = new Task(task.getValue().id(), task.getValue().sortOrder(), task.getValue().name(), false, routineID);
        routine.save(uncheckedTask);
    }


    public void removeTask(int id, Routine routine) {
        routineRepository.removeTask(id);
        routine.remove(id);
    }

    public void renameTask(int id, String name, Routine routine) {
        routineRepository.renameTask(id, name);
        routine.rename(id, name);
    }

    public void appendTask(Task task, Routine routine) {
        routineRepository.appendTask(task);
        routine.append(task);
    }

    public void prependTask(Task task, Routine routine){
        routineRepository.prependTask(task);
        routine.prepend(task);
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
