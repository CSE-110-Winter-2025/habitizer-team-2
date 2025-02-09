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
    private final TaskRepository taskRepository;
    // UI state
    private final PlainMutableSubject<List<Task>> orderedTasks;
    private final PlainMutableSubject<Boolean> isCheckedOff;

    private final PlainMutableSubject<Double> elapsedTime;
    private final PlainMutableSubject<String> displayedText;


    public static final ViewModelInitializer<MainViewModel> initializer =
            new ViewModelInitializer<>(
                    MainViewModel.class,
                    creationExtras -> {
                        var app = (HabitizerApplication) creationExtras.get(APPLICATION_KEY);
                        assert app != null;
                        return new MainViewModel(app.getTaskRepository());
                    });

    public MainViewModel(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;

        // Create the observable subjects.
        this.orderedTasks = new PlainMutableSubject<>();
        this.isCheckedOff = new PlainMutableSubject<>();
        this.elapsedTime  = new PlainMutableSubject<>();
        this.displayedText = new PlainMutableSubject<>();

        this.isCheckedOff.setValue(false);

        // When the list of tasks changes (or is first loaded), reset the ordering.
        taskRepository.findAll().observe(tasks -> {
            if (tasks == null) return; // not ready yet, ignore

            var newOrderedTasks = tasks.stream()
                    .sorted(Comparator.comparingInt(Task::sortOrder))
                    .collect(Collectors.toList());
            orderedTasks.setValue(newOrderedTasks);
        });

        taskRepository.findAll().observe(tasks -> {
            if(tasks == null) return;

            var orderedTasks = getOrderedTasks();
            Objects.requireNonNull(orderedTasks.getValue()).forEach(task -> {
                if(task == null) return;
                isCheckedOff.setValue(task.checkedOff());
            });

        });

    }

    public PlainMutableSubject<String> getDisplayedText() {
        return displayedText;
    }

    public PlainMutableSubject<List<Task>> getOrderedTasks() {
        return orderedTasks;
    }

    public PlainMutableSubject<Boolean> getIsCheckedOff() {
        return isCheckedOff;
    }

    public void checkOff(int id){
        var task = taskRepository.find(id);
        var checkedOffTask = new Task(task.getValue().id(), task.getValue().sortOrder(), task.getValue().name(), true);
        taskRepository.save(checkedOffTask);
    }

    public void remove(int id) {
        taskRepository.remove(id);
    }

    public void append(Task task) {
        taskRepository.append(task);
    }

    public void prepend(Task task){
        taskRepository.prepend(task);
    }

}
