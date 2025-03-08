package edu.ucsd.cse110.habitizer.lib.data;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import edu.ucsd.cse110.habitizer.lib.domain.Task;
import edu.ucsd.cse110.habitizer.lib.util.observables.PlainMutableSubject;
public class InMemoryTaskDataSource {

    private int nextId = 0;

    private int minSortOrder = Integer.MAX_VALUE;
    private int maxSortOrder = Integer.MIN_VALUE;


    private int goalTime;

    private final Map<Integer, Task> tasks
            = new HashMap<>();
    private final Map<Integer, PlainMutableSubject<Task>> taskSubjects
            = new HashMap<>();
    private final PlainMutableSubject<List<Task>> allTasksSubject
            = new PlainMutableSubject<>();

    public InMemoryTaskDataSource() {
    }

    // default time for both morning and evening routine goal time
    public final static int DEFAULT_GOAL_TIME = 45;


    public final static List<Task> DEFAULT_TASKS_MORNING = List.of(

            new Task(0,0 , "Shower", false ),
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

    public void setGoalTime(int goalTime){
        this.goalTime = goalTime;
    }

    public static InMemoryTaskDataSource fromDefaultMorning() {
        var data = new InMemoryTaskDataSource();
        data.putTasks(DEFAULT_TASKS_MORNING);
        data.setGoalTime(DEFAULT_GOAL_TIME);
        return data;
    }


    public static InMemoryTaskDataSource fromDefaultEvening() {
        var data = new InMemoryTaskDataSource();
        data.putTasks(DEFAULT_TASKS_EVENING);
        data.setGoalTime(DEFAULT_GOAL_TIME);
        return data;
      }

    public static InMemoryTaskDataSource fromDefaultNew() {
        var data = new InMemoryTaskDataSource();
        data.putTasks(List.of()); //no tasks
        data.setGoalTime(0); //0 goal time
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


    public int getGoalTime() {
        return goalTime;
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

        tasks.remove(id); //remove task from list
        shiftSortOrders(sortOrder, maxSortOrder, -1);

        if (taskSubjects.containsKey(id)) {
            taskSubjects.get(id).setValue(null); //ask about this line
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

    public void shiftSortOrders(int from, int to, int by) { //by is how much you want to switch
        var tasks = this.tasks.values().stream()
                .filter(task -> task.sortOrder() >= from && task.sortOrder() <= to) //-> means for each
                .map(task -> task.withSortOrder(task.sortOrder() + by)) //shifting happens here
                .collect(Collectors.toList()); //put into list (making new list)
        putTasks(tasks);
    }

    //set sort orders here (flipping orders of tasks in Routine would be 'swap' method)
    public void swapTasks(Integer id1, Integer id2){ //makes persistence easier when tasks are swapped
        var task1 = tasks.get(id1); //getting id
        var task2 = tasks.get(id2);

        if(task1 != null && task2 != null){
            var t1_sortOrder = task1.sortOrder(); //getting sort order of task
            var t2_sortOrder = task2.sortOrder();//.sortOrder does not include moving things in the list

            var updatedTask1 = task1.withSortOrder(t2_sortOrder);
            var updatedTask2 = task2.withSortOrder(t1_sortOrder);

            //notify observers
            putTask(updatedTask1);
            Objects.requireNonNull(id1); //contract
            taskSubjects.get(id1).setValue(tasks.get(id1));

            putTask(updatedTask2);
            Objects.requireNonNull(id2);
            taskSubjects.get(id2).setValue(tasks.get(id2));

//            var sortedTasks = this.tasks.values().stream()
//                    .sorted(Comparator.comparingInt(Task::sortOrder)) //-> means for each
//                    .collect(Collectors.toList()); //put into list (making new list)
//            putTasks(sortedTasks);

//            var updatedTask1 = task1.withSortOrder(t2_sortOrder);
//            var updatedTask2 = task2.withSortOrder(t1_sortOrder);
//
//            //delete old tasks?
//            removeTask(id1);
//            removeTask(id2);
//
//            putTask(updatedTask1);
//            putTask(updatedTask2);
        }

    }

    public int getNumTasks(){
        return tasks.size(); //get number of tasks in list
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

    //private void
}
