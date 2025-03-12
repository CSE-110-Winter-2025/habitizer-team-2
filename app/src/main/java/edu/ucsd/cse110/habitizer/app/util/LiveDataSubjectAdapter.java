package edu.ucsd.cse110.habitizer.app.util;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import java.util.Collections;
import java.util.List;

import edu.ucsd.cse110.habitizer.lib.util.observables.Observer;
import edu.ucsd.cse110.habitizer.lib.util.observables.PlainMutableSubject;
import edu.ucsd.cse110.habitizer.lib.util.observables.Subject;


public class LiveDataSubjectAdapter<T> implements Subject<T>{
    public final LiveData<T> adaptee;

    public LiveDataSubjectAdapter(LiveData<T> adaptee){
        this.adaptee = adaptee;
    }

    @Nullable
    @Override
    public T getValue() {
        return adaptee.getValue();
    }

    @Override
    public boolean hasObservers() {
        return adaptee.hasObservers();
    }

    @Override
    public boolean isInitialized() {
        return adaptee.isInitialized();
    }

    @Override
    public Observer<T> observe(Observer<T> observer) {
        adaptee.observeForever(observer::onChanged);
        return observer;
    }

    @Override
    public void removeObserver(Observer<T> observer) {
        adaptee.removeObserver(observer::onChanged);
    }

    //not implemented
    @Override
    public void removeObservers() {
        throw new UnsupportedOperationException("removeObservers() not supported by LiveDataToSubjectAdapter.");
    }

    //not implemented
    @Override
    public List<Observer<T>> getObservers() {
        throw new UnsupportedOperationException("getObservers() not supported by LiveDataToSubjectAdapter.");
    }

    public static void example() {
        var subj = new PlainMutableSubject<Integer>();

        var obs = subj.observe(value -> { /**/ });
        subj.removeObserver(obs);
    }

}

