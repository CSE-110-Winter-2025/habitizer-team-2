package edu.ucsd.cse110.habitizer.app;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import edu.ucsd.cse110.habitizer.lib.domain.RoutineRepository;
import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.util.observables.PlainMutableSubject;

public class MainViewModel extends ViewModel {
    // Domain state (true "Model" state)
    private final Routine eveningRoutine;

    private final Routine morningRoutine;

    private final RoutineRepository routineRepository;
    // UI state

    private final PlainMutableSubject<List<Routine>> orderedRoutines;
    private final PlainMutableSubject<List<Task>> morningOrderedTasks;
    private final PlainMutableSubject<Boolean> morningIsCheckedOff;

    private final PlainMutableSubject<Integer> morningGoalTime;

    private final PlainMutableSubject<Double> morningElapsedTime;
    private final PlainMutableSubject<String> morningDisplayedText;

    private final PlainMutableSubject<List<Task>> eveningOrderedTasks;
    private final PlainMutableSubject<Boolean> eveningIsCheckedOff;

    private final PlainMutableSubject<Integer> eveningGoalTime;

    private final PlainMutableSubject<Double> eveningElapsedTime;
    private final PlainMutableSubject<String> eveningDisplayedText;


    public static final ViewModelInitializer<MainViewModel> initializer =
            new ViewModelInitializer<>(
                    MainViewModel.class,
                    creationExtras -> {
                        var app = (HabitizerApplication) creationExtras.get(APPLICATION_KEY);
                        assert app != null;
                        return new MainViewModel(app.getMorningTaskRepository(), app.getEveningTaskRepository(), app.getRoutineRepository());
                    });

    public MainViewModel(Routine morningRoutine, Routine eveningRoutine, RoutineRepository routineRepository) {
        this.morningRoutine = morningRoutine;
        this.eveningRoutine = eveningRoutine;
        this.routineRepository = routineRepository;
        // Create the observable subjects.
        this.orderedRoutines = new PlainMutableSubject<>();
        this.morningOrderedTasks = new PlainMutableSubject<>();
        this.morningIsCheckedOff = new PlainMutableSubject<>();
        this.morningGoalTime = new PlainMutableSubject<>();
        this.morningElapsedTime  = new PlainMutableSubject<>();
        this.morningDisplayedText = new PlainMutableSubject<>();

        this.eveningOrderedTasks = new PlainMutableSubject<>();
        this.eveningIsCheckedOff = new PlainMutableSubject<>();
        this.eveningGoalTime = new PlainMutableSubject<>();
        this.eveningElapsedTime  = new PlainMutableSubject<>();
        this.eveningDisplayedText = new PlainMutableSubject<>();

        // When the list of tasks changes (or is first loaded), reset the ordering.

        routineRepository.findAll().observe(routines -> {
            if (routines == null) return; // not ready yet, ignore

            var newOrderedRoutines = routines.stream()
                    .sorted(Comparator.comparingInt(Routine::sortOrder))
                    .collect(Collectors.toList());
            orderedRoutines.setValue(newOrderedRoutines);
        });

        morningRoutine.findAll().observe(tasks -> {
            if (tasks == null) return; // not ready yet, ignore

            var newOrderedTasks = tasks.stream()
                    .sorted(Comparator.comparingInt(Task::sortOrder))
                    .collect(Collectors.toList());
            morningOrderedTasks.setValue(newOrderedTasks);
        });

        morningRoutine.findAll().observe(tasks -> {
            if(tasks == null) return;

            var orderedTasks = getMorningOrderedTasks();
            Objects.requireNonNull(orderedTasks.getValue()).forEach(task -> {
                if(task == null) return;
                morningIsCheckedOff.setValue(task.checkedOff());
            });

        });

        // When the list of tasks changes (or is first loaded), reset the ordering.
        eveningRoutine.findAll().observe(tasks -> {
            if (tasks == null) return; // not ready yet, ignore

            var newOrderedTasks = tasks.stream()
                    .sorted(Comparator.comparingInt(Task::sortOrder))
                    .collect(Collectors.toList());
            eveningOrderedTasks.setValue(newOrderedTasks);
        });

        eveningRoutine.findAll().observe(tasks -> {
            if(tasks == null) return;

            var orderedTasks = getEveningOrderedTasks();
            Objects.requireNonNull(orderedTasks.getValue()).forEach(task -> {
                if(task == null) return;
                eveningIsCheckedOff.setValue(task.checkedOff());
            });

        });

    }

    public PlainMutableSubject<List<Routine>> getOrderedRoutines() {return orderedRoutines;}
    public Routine getMorningTaskRepository(){return morningRoutine;}

    public PlainMutableSubject<String> getMorningDisplayedText() {return morningDisplayedText;}

    public PlainMutableSubject<List<Task>> getMorningOrderedTasks() {
        return morningOrderedTasks;
    }

    public PlainMutableSubject<Boolean> getMorningIsCheckedOff() {
        return morningIsCheckedOff;
    }

    public Routine getEveningTaskRepository(){return eveningRoutine;}


    public PlainMutableSubject<String> getEveningDisplayedText() {return eveningDisplayedText;}

    public PlainMutableSubject<List<Task>> getEveningOrderedTasks() {
        return eveningOrderedTasks;
    }

    public PlainMutableSubject<Boolean> getEveningIsCheckedOff() {
        return eveningIsCheckedOff;
    }

    public PlainMutableSubject<Integer> getMorningGoalTime() {
        return morningGoalTime;
    }

    public PlainMutableSubject<Integer> getEveningGoalTime() {
        return eveningGoalTime;
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

    public void remove(int id, Routine routine) {
        routine.remove(id);
    }

    public void rename(int id, String name, Routine routine) {
        routine.rename(id, name);
    }

    public void append(Task task, Routine routine) {
        routine.append(task);
    }

    public void prepend(Task task, Routine routine){
        routine.prepend(task);
    }

}
