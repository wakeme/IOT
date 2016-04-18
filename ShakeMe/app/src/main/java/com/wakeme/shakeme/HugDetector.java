package com.wakeme.shakeme;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

/**
 * Created by WakeMe on 3/16/2016.
 */
public class HugDetector implements SensorEventListener{

    private static final float MAX_DISTANCE = 0.5f;

    OnHugListener mHugListener;

    long startTime = 0;

    public HugDetector(OnHugListener onHugListener) {
        mHugListener = onHugListener;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float distance = event.values[0];
        long now = System.currentTimeMillis();
        if (startTime == 0) {
            startTime = now;
        }
        long elapsedTime = now - startTime;
        if (elapsedTime > 1000) {
            System.out.println(distance);
            if (distance < MAX_DISTANCE) {
                mHugListener.onHug();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public interface OnHugListener {
        public void onHug();
    }
}
