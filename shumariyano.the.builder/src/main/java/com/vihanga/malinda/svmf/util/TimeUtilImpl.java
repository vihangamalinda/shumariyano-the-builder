package com.vihanga.malinda.svmf.util;

public class TimeUtilImpl implements TimeUtil {

    private final float timeStarted;

    public TimeUtilImpl() {
        this.timeStarted = System.nanoTime();
    }

    public float getElapsedTimeBySeconds() {
        return (float) (System.nanoTime() - this.timeStarted) * 1e-9f;
    }
}
