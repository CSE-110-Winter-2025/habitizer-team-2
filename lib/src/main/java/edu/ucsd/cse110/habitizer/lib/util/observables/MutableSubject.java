package edu.ucsd.cse110.habitizer.lib.util.observables;

import edu.ucsd.cse110.habitizer.lib.util.observables.Subject;

/**
 * A mutable subject that can be set to a new value.
 * <p>
 * You can up-cast a {@link MutableSubject} to a {@link Subject} and return that
 * if you want to expose it as a read-only subject to consumers (hide the setter).
 *
 * @param <T> The type of the value that the subject holds.
 */
public interface MutableSubject<T> extends Subject<T> {
    /**
     * Sets the value of the subject.
     *
     * @param newValue The new value.
     */
    void setValue(T newValue);
}