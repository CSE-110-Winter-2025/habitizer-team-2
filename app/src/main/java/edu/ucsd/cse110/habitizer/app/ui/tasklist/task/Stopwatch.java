package edu.ucsd.cse110.habitizer.app.ui.tasklist.task;

import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

/**
 * Stopwatch is a utility class that tracks elapsed time in minutes and updates a TextView with the elapsed time.
 * It uses a Handler to schedule updates every minute.
 */
public class Stopwatch {
    private int elapsedTimeSeconds;
    private final Handler handler;
    private final TextView elapsedTimeTextView;
    private final Runnable updateRunnable;

    /**
     * Constructs a new Stopwatch instance.
     *
     * @param elapsedTimeTextView The TextView to update with the elapsed time.
     */
    public Stopwatch(TextView elapsedTimeTextView) {
        this.elapsedTimeSeconds = 0;
        this.handler = new Handler();
        this.elapsedTimeTextView = elapsedTimeTextView;

        // Runnable to update the elapsed time every minute
        this.updateRunnable = new Runnable() {
            @Override
            public void run() {
                // Log the elapsed time in seconds (for debugging)
                Log.d("Stopwatch", String.valueOf(elapsedTimeSeconds) + " seconds.");

                // Update the elapsedTimeTextView with the current elapsed time in minutes
                elapsedTimeTextView.setText(String.valueOf(elapsedTimeSeconds/60));

                // Schedule the Runnable to run again every second
                handler.postDelayed(this, 1000); // Update every minute

                // Increment the elapsed time by one second
                elapsedTimeSeconds++;
            }
        };
    }

    /**
     * Starts the stopwatch by posting the updateRunnable to the handler.
     * This schedules the Runnable to be executed on the thread associated with the handler.
     */
    public void start() {
        handler.post(updateRunnable);
    }

    /**
     * Stops the stopwatch by removing any pending callbacks for the updateRunnable.
     * This prevents the Runnable from being executed further.
     */
    public void stop() {
        handler.removeCallbacks(updateRunnable);
    }

    /**
     * Returns the elapsed time in minutes.
     *
     * @return The elapsed time in minutes.
     */
    public int getElapsedTimeInMinutes() {
        return elapsedTimeSeconds / 60;
    }
}