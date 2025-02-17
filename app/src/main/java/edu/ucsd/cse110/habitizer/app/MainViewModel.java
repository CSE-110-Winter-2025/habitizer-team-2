package edu.ucsd.cse110.habitizer.app;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.domain.TaskRepository;
import edu.ucsd.cse110.habitizer.lib.util.observables.PlainMutableSubject;

public class MainViewModel extends ViewModel {
    // Domain state (true "Model" state)
    private final TaskRepository eveningTaskRepository;

    private final TaskRepository morningTaskRepository;
    // UI state
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
                        return new MainViewModel(app.getMorningTaskRepository(), app.getEveningTaskRepository());
                    });

    public MainViewModel(TaskRepository morningTaskRepository, TaskRepository eveningTaskRepository) {
        this.morningTaskRepository = morningTaskRepository;
        this.eveningTaskRepository = eveningTaskRepository;
        // Create the observable subjects.
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
        morningTaskRepository.findAll().observe(tasks -> {
            if (tasks == null) return; // not ready yet, ignore

            var newOrderedTasks = tasks.stream()
                    .sorted(Comparator.comparingInt(Task::sortOrder))
                    .collect(Collectors.toList());
            morningOrderedTasks.setValue(newOrderedTasks);
        });

        morningTaskRepository.findAll().observe(tasks -> {
            if(tasks == null) return;

            var orderedTasks = getMorningOrderedTasks();
            Objects.requireNonNull(orderedTasks.getValue()).forEach(task -> {
                if(task == null) return;
                morningIsCheckedOff.setValue(task.checkedOff());
            });

        });

        // When the list of tasks changes (or is first loaded), reset the ordering.
        eveningTaskRepository.findAll().observe(tasks -> {
            if (tasks == null) return; // not ready yet, ignore

            var newOrderedTasks = tasks.stream()
                    .sorted(Comparator.comparingInt(Task::sortOrder))
                    .collect(Collectors.toList());
            eveningOrderedTasks.setValue(newOrderedTasks);
        });

        eveningTaskRepository.findAll().observe(tasks -> {
            if(tasks == null) return;

            var orderedTasks = getEveningOrderedTasks();
            Objects.requireNonNull(orderedTasks.getValue()).forEach(task -> {
                if(task == null) return;
                eveningIsCheckedOff.setValue(task.checkedOff());
            });

        });

    }

    public TaskRepository getMorningTaskRepository(){return morningTaskRepository;}

    public PlainMutableSubject<String> getMorningDisplayedText() {return morningDisplayedText;}

    public PlainMutableSubject<List<Task>> getMorningOrderedTasks() {
        return morningOrderedTasks;
    }

    public PlainMutableSubject<Boolean> getMorningIsCheckedOff() {
        return morningIsCheckedOff;
    }

    public TaskRepository getEveningTaskRepository(){return eveningTaskRepository;}


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

    public void checkOff(int id, TaskRepository taskRepository){
        var task = taskRepository.find(id);
        var checkedOffTask = new Task(task.getValue().id(), task.getValue().sortOrder(), task.getValue().name(), true);
        taskRepository.save(checkedOffTask);
    }

    public void remove(int id, TaskRepository taskRepository) {
        taskRepository.remove(id);
    }

    public void rename(int id, String name, TaskRepository taskRepository) {
        taskRepository.rename(id, name);
    }

    public void append(Task task, TaskRepository taskRepository) {
        taskRepository.append(task);
    }

    public void prepend(Task task, TaskRepository taskRepository){
        taskRepository.prepend(task);
    }

}
