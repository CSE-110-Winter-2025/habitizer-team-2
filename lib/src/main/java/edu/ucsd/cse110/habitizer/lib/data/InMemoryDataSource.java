package edu.ucsd.cse110.habitizer.lib.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.util.observables.PlainMediatorSubject;
import edu.ucsd.cse110.habitizer.lib.util.observables.PlainMutableSubject;
public class InMemoryDataSource {

    private int nextId = 0;

    private int minSortOrder = Integer.MAX_VALUE;
    private int maxSortOrder = Integer.MIN_VALUE;

    private final Map<Integer, Task> tasks
            = new HashMap<>();
    private final Map<Integer, PlainMutableSubject<Task>> taskSubjects
            = new HashMap<>();
    private final PlainMutableSubject<List<Task>> allTasksSubject
            = new PlainMutableSubject<>();

    public InMemoryDataSource() {
    }

    public final static List<Task> DEFAULT_TASKS_MORNING = List.of(

            new Task(0,0 , "Shower", false),
            new Task(1,1,"Brush Teeth", false),
            new Task(2,2,"Dress", false),
            new Task(3,3,"Make Coffee", false),
            new Task(4,4,"Make Lunch", false),
            new Task(5,5,"Dinner Prep", false),
            new Task(6,6,"Pack Bag", false)

    );

    public final static List<Task> DEFAULT_TASKS_EVENING = List.of(

            new Task(0,0 , "Prepare Dinner", false),
            new Task(1,1,"Eat Dinner", false),
            new Task(2,2,"Do Laundry", false),
            new Task(3,3,"Watch TV", false),
            new Task(4,4,"Walk Dog", false),
            new Task(5,5,"Shower", false),
            new Task(6,6,"Get in Bed", false)

    );

    public static InMemoryDataSource fromDefaultMorning() {
        var data = new InMemoryDataSource();
        data.putTasks(DEFAULT_TASKS_MORNING);
        return data;
    }


    public static InMemoryDataSource fromDefaultEvening() {
        var data = new InMemoryDataSource();
        data.putTasks(DEFAULT_TASKS_EVENING);
        return data;
    }


    public List<Task> getTasks() {
        return List.copyOf(tasks.values());
    }

    public Task getTask(int id) {
        return tasks.get(id);
    }

    public PlainMutableSubject<Task> getTaskSubject(int id) {
        if (!taskSubjects.containsKey(id)) {
            var subject = new PlainMutableSubject<Task>();
            subject.setValue(getTask(id));
            taskSubjects.put(id, subject);
        }
        return taskSubjects.get(id);
    }

    public PlainMutableSubject<List<Task>> getAllTasksSubject() {
        return allTasksSubject;
    }

    public int getMinSortOrder() {
        return minSortOrder;
    }

    public int getMaxSortOrder() {
        return maxSortOrder;
    }

    public void putTask(Task task) {
        var fixedTask = preInsert(task);

        tasks.put(fixedTask.id(), fixedTask);
        postInsert();
        assertSortOrderConstraints();

        if (taskSubjects.containsKey(fixedTask.id())) {
            taskSubjects.get(fixedTask.id()).setValue(fixedTask);
        }
        allTasksSubject.setValue(getTasks());
    }

    public void putTasks(List<Task> tasks) {
        var fixedTasks = tasks.stream()
                .map(this::preInsert)
                .collect(Collectors.toList());

        fixedTasks.forEach(task -> this.tasks.put(task.id(), task));
        postInsert();
        assertSortOrderConstraints();

        fixedTasks.forEach(task -> {
            if (taskSubjects.containsKey(task.id())) {
                taskSubjects.get(task.id()).setValue(task);
            }
        });
        allTasksSubject.setValue(getTasks());
    }

    public void removeTask(int id) {
        var task = tasks.get(id);
        var sortOrder = task.sortOrder();

        tasks.remove(id);
        shiftSortOrders(sortOrder, maxSortOrder, -1);

        if (taskSubjects.containsKey(id)) {
            taskSubjects.get(id).setValue(null);
        }
        allTasksSubject.setValue(getTasks());
    }

    public void replaceName(int id, String name) {
        var task = tasks.get(id);
        if (task != null) {
            var updatedTask = task.withName(name); // Create a new Task with the updated name
            tasks.put(id, updatedTask); // Store the updated Task back in the map

            // Notify observers (if using reactive programming)
            if (taskSubjects.containsKey(id)) {
                taskSubjects.get(id).setValue(updatedTask);
            }
            allTasksSubject.setValue(getTasks()); // Store the new Task instance in the map
        }
    }

    public void shiftSortOrders(int from, int to, int by) {
        var tasks = this.tasks.values().stream()
                .filter(task -> task.sortOrder() >= from && task.sortOrder() <= to)
                .map(task -> task.withSortOrder(task.sortOrder() + by))
                .collect(Collectors.toList());

        putTasks(tasks);
    }

    /**
     * Private utility method to maintain state of the fake DB: ensures that new
     * tasks inserted have an id, and updates the nextId if necessary.
     */
    private Task preInsert(Task task) {
        var id = task.id();
        if (id == null) {
            // If the task has no id, give it one.
            task = task.withId(nextId++);
        }
        else if (id > nextId) {
            // If the task has an id, update nextId if necessary to avoid giving out the same
            // one. This is important for when we pre-load tasks like in fromDefault().
            nextId = id + 1;
        }

        return task;
    }

    /**
     * Private utility method to maintain state of the fake DB: ensures that the
     * min and max sort orders are up to date after an insert.
     */
    private void postInsert() {
        // Keep the min and max sort orders up to date.
        minSortOrder = tasks.values().stream()
                .map(Task::sortOrder)
                .min(Integer::compareTo)
                .orElse(Integer.MAX_VALUE);

        maxSortOrder = tasks.values().stream()
                .map(Task::sortOrder)
                .max(Integer::compareTo)
                .orElse(Integer.MIN_VALUE);
    }

    /**
     * Safety checks to ensure the sort order constraints are maintained.
     * <p></p>
     * Will throw an AssertionError if any of the constraints are violated,
     * which should never happen. Mostly here to make sure I (Dylan) don't
     * write incorrect code by accident!
     */
    private void assertSortOrderConstraints() {
        // Get all the sort orders...
        var sortOrders = tasks.values().stream()
                .map(Task::sortOrder)
                .collect(Collectors.toList());

        // Non-negative...
        assert sortOrders.stream().allMatch(i -> i >= 0);

        // Unique...
        assert sortOrders.size() == sortOrders.stream().distinct().count();

        // Between min and max...
        assert sortOrders.stream().allMatch(i -> i >= minSortOrder);
        assert sortOrders.stream().allMatch(i -> i <= maxSortOrder);
    }

}
