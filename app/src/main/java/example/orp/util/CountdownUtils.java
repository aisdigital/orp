package example.orp.util;

import android.util.Log;

/**
 * Created by jonathan on 10/21/15.
 */
public class CountdownUtils {

    private Long startTime;
    private Long endTime;

    private static CountdownUtils instance;

    public static CountdownUtils getInstance() {
        if (instance == null) {
            instance = new CountdownUtils();
        }
        return instance;
    }

    private CountdownUtils() {
    }

    public void start() {
        setStartTime(System.nanoTime());
        Log.i("STARTED: ", getStartTime().toString());
    }

    public Double stop() {
        setEndTime(System.nanoTime());
        Double duration = (getEndTime().doubleValue() - getStartTime().doubleValue()) / 1000000000.0;
        Log.i("ENDED: ", duration.toString());
        reset();
        return duration;
    }

    public void reset() {
        start();
        setEndTime(0l);
    }

    public Long getStartTime() {
        return startTime;
    }

    private void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    private void setEndTime(Long endTime) {
        this.endTime = endTime;
    }
}
