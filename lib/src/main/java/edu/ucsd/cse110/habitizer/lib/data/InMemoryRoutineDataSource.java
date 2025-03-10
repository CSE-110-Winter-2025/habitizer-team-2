package edu.ucsd.cse110.habitizer.lib.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import edu.ucsd.cse110.habitizer.lib.domain.Routine;
import edu.ucsd.cse110.habitizer.lib.util.observables.PlainMutableSubject;
public class InMemoryRoutineDataSource {

    private int nextId = 0;

    private int minSortOrder = Integer.MAX_VALUE;
    private int maxSortOrder = Integer.MIN_VALUE;


    private final Map<Integer, Routine> routines
            = new HashMap<>();
    private final Map<Integer, PlainMutableSubject<Routine>> routineSubjects
            = new HashMap<>();
    private final PlainMutableSubject<List<Routine>> allRoutinesSubject
            = new PlainMutableSubject<>();

    public InMemoryRoutineDataSource() {
    }



    public final static List<Routine> DEFAULT_ROUTINES = List.of(
            new Routine(0, 0, "Morning Routine", InMemoryTaskDataSource.fromDefaultMorning()),
            new Routine(1, 1, "Evening Routine", InMemoryTaskDataSource.fromDefaultEvening())
    );

    public static InMemoryRoutineDataSource fromDefault() {
        var data = new InMemoryRoutineDataSource();
        data.putRoutines(DEFAULT_ROUTINES);
        return data;
    }

    
    public List<Routine> getRoutines() {
        return List.copyOf(routines.values());
    }
    
    public Routine getRoutine(int id) {
        return routines.get(id);
    }

    public PlainMutableSubject<Routine> getRoutineSubject(int id) {
        if (!routineSubjects.containsKey(id)) {
            var subject = new PlainMutableSubject<Routine>();
            subject.setValue(getRoutine(id));
            routineSubjects.put(id, subject);
        }
        return routineSubjects.get(id);
    }

    public PlainMutableSubject<List<Routine>> getAllRoutinesSubject() {
        return allRoutinesSubject;
    }

    public int getMinSortOrder() {
        return minSortOrder;
    }

    public int getMaxSortOrder() {
        return maxSortOrder;
    }


    public void putRoutine(Routine routine) {
        var fixedRoutine = preInsert(routine);

        routines.put(fixedRoutine.id(), fixedRoutine);
        postInsert();
        assertSortOrderConstraints();

        if (routineSubjects.containsKey(fixedRoutine.id())) {
            routineSubjects.get(fixedRoutine.id()).setValue(fixedRoutine);
        }
        allRoutinesSubject.setValue(getRoutines());
    }

    public void putRoutines(List<Routine> routines) {
        var fixedRoutines = routines.stream()
                .map(this::preInsert)
                .collect(Collectors.toList());

        fixedRoutines.forEach(routine -> this.routines.put(routine.id(), routine));
        postInsert();
        assertSortOrderConstraints();

        fixedRoutines.forEach(routine -> {
            if (routineSubjects.containsKey(routine.id())) {
                routineSubjects.get(routine.id()).setValue(routine);
            }
        });
        allRoutinesSubject.setValue(getRoutines());
    }

    /**
     * Removes a routine with the specified ID from the in-memory data store.
     *
     * This method performs the following actions:
     * - Removes the routine with the given ID from the collection.
     * - Adjusts the sort orders of other routines to maintain continuity.
     * - Updates associated subjects to notify observers about the changes.
     *
     * @param id The unique identifier of the routine to remove.
     * @throws IllegalArgumentException If no routine with the specified ID exists.
     * @require routines.containsKey(id)
     * @ensure routines.size()@pre - 1 == routines.size()@post
     */
    public void removeRoutine(int id) {
        if (!routines.containsKey(id)){
            throw new IllegalArgumentException("Cannot remove non-existent routine with id: " + id);
        }
        var routine = routines.get(id);
        var sortOrder = routine.sortOrder();

        routines.remove(id);
        shiftSortOrders(sortOrder, maxSortOrder, -1);

        if (routineSubjects.containsKey(id)) {
            routineSubjects.get(id).setValue(null);
        }
        allRoutinesSubject.setValue(getRoutines());
    }

    public void replaceName(int id, String name) {
        var routine = routines.get(id);
        if (routine != null) {
            var updatedRoutine = routine.withName(name); // Create a new Routine with the updated name
            routines.put(id, updatedRoutine); // Store the updated Routine back in the map

            // Notify observers (if using reactive programming)
            if (routineSubjects.containsKey(id)) {
                routineSubjects.get(id).setValue(updatedRoutine);
            }
            allRoutinesSubject.setValue(getRoutines()); // Store the new Routine instance in the map
        }
    }

    public void shiftSortOrders(int from, int to, int by) {
        var routines = this.routines.values().stream()
                .filter(routine -> routine.sortOrder() >= from && routine.sortOrder() <= to)
                .map(routine -> routine.withSortOrder(routine.sortOrder() + by))
                .collect(Collectors.toList());

        putRoutines(routines);
    }

    /**
     * Private utility method to maintain state of the fake DB: ensures that new
     * routines inserted have an id, and updates the nextId if necessary.
     */
    private Routine preInsert(Routine routine) {
        var id = routine.id();
        if (id == null) {
            // If the routine has no id, give it one.
            routine = routine.withId(nextId++);
        }
        else if (id > nextId) {
            // If the routine has an id, update nextId if necessary to avoid giving out the same
            // one. This is important for when we pre-load routines like in fromDefault().
            nextId = id + 1;
        }

        return routine;
    }

    /**
     * Private utility method to maintain state of the fake DB: ensures that the
     * min and max sort orders are up to date after an insert.
     */
    private void postInsert() {
        // Keep the min and max sort orders up to date.
        minSortOrder = routines.values().stream()
                .map(Routine::sortOrder)
                .min(Integer::compareTo)
                .orElse(Integer.MAX_VALUE);

        maxSortOrder = routines.values().stream()
                .map(Routine::sortOrder)
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
        var sortOrders = routines.values().stream()
                .map(Routine::sortOrder)
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
