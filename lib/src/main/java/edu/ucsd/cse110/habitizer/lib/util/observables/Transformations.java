package edu.ucsd.cse110.habitizer.lib.util.observables;

import androidx.annotation.Nullable;

import java.util.function.Function;

import edu.ucsd.cse110.habitizer.lib.util.observables.Subject;

/**
 * Utility methods for working with subjects.
 */
public class Transformations {
    /**
     * Applies the given function on the main thread to each value emitted by
     * the source Subject and returns a Subject that emits resulting values.
     * <p>
     * This allows for "chaining" of Subjects, where the output of one Subject
     * is used as the input to the new returned Subject.
     *
     * @param subject        The subject to map.
     * @param transformation The function to map the subject with.
     * @param <T>            The type of the values of source subject.
     * @param <R>            The type of the values of the result subject.
     * @return A new subject which has
     */
    public static <T, R> Subject<R> map(Subject<T> subject, Function<T, R> transformation) {
        var result = new PlainMediatorSubject<R>();
        result.addSource(subject, value -> {
            R resultValue = transformation.apply(value);
            result.setValue(resultValue);
        });
        return result;
    }

    /**
     * Advanced fancy transformation. Returns a new {@link Subject} that "switches"
     * to different {@link Subject}s depending on a trigger {@link Subject}.
     * <p>
     * This allows for dynamic switching of the source Subject based on the values
     * emitted by the trigger Subject.
     * <p>
     * If you are into functional programming, this is related to flatMapbind/>>=.
     *
     * @param trigger  The subject that triggers the switch.
     * @param switcher Takes in a value and returns a switched to source subject. MUST NOT RETURN NULL.
     * @param <T>      The type of the trigger subject.
     * @param <R>      The type of the resulting subject.
     * @return A new subject that emits values from a source subject based on the trigger subject.
     */
    public static <T, R> Subject<R> switchMap(Subject<T> trigger, Function<T, Subject<R>> switcher) {
        var result = new PlainMediatorSubject<R>();

        result.addSource(trigger, new Observer<T>() {
            @Nullable
            Subject<R> currentSource = null;

            @Override
            public void onChanged(@Nullable T value) {
                var switchedTo = switcher.apply(value);

                if (switchedTo.equals(currentSource)) {
                    return; // optimization: do nothing
                } else {
                    result.removeSource(currentSource);
                }

                this.currentSource = switchedTo;
                result.addSource(currentSource, result::setValue);
            }
        });

        return result;
    }
}
