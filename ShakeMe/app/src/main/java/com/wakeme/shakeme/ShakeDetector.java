package com.wakeme.shakeme;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by WakeMe on 3/16/2016.
 */
public class ShakeDetector implements SensorEventListener {

    // Minimum acceleration needed to count as a shake movement
    private static final int MIN_SHAKE_ACCELERATION = 5;

    // Minimum number of movements to register a shake
    private static final int MIN_MOVEMENTS = 2;

    // Minimum time (in milliseconds) for judging if it stopped shaking
    private static final int MIN_REST_DURATION = 500;

    // Arrays to store gravity and linear acceleration values
    private float[] mGravity = {0.0f, 0.0f, 0.0f};
    private float[] mLinearAcceleration = {0.0f, 0.0f, 0.0f};

    // Indexes for x, y, and z values
    private static final int X = SensorManager.AXIS_X-1;
    private static final int Y = SensorManager.AXIS_Y-1;
    private static final int Z = SensorManager.AXIS_Z-1;

    // OnShakeListener that will be notified when the shake is detected
    private OnShakeListener mShakeListener;

    // Counter for shake movements
    int moveCount = 0;

    // Flag for shaking state
    boolean isShaking = false;

    // Time when the last shake detected
    long lastMotion = 0;

    // Constructor that sets the shake listener
    public ShakeDetector(OnShakeListener shakeListener) {
        mShakeListener = shakeListener;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // This method will be called when the accelerometer detects a change.

        // Call a helper method that wraps code from the Android developer site
        setCurrentAcceleration(event);

        // Get the max linear acceleration in any direction
        float maxLinearAcceleration = getMaxCurrentLinearAcceleration();

        // Check if the acceleration is greater than minimum threshold
        if (maxLinearAcceleration > MIN_SHAKE_ACCELERATION) {

            lastMotion = System.currentTimeMillis();
            moveCount++;
            // Check if enough movements have been made to qualify as a shake
            if (moveCount > MIN_MOVEMENTS) {
                // It's a shake! Notify the listener.
                if (!isShaking) {
                    isShaking = true;
                    mShakeListener.onShakeBegan();
                }
                mShakeListener.onShake();
            }
        } else {
            if (isShaking) {
                long now = System.currentTimeMillis();
                long motionElapsedTime = now - lastMotion;
                if (motionElapsedTime > MIN_REST_DURATION) {
                    resetShakeDetection();
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Intentionally blank
    }

    private void setCurrentAcceleration(SensorEvent event) {
       	/*
    	 *  BEGIN SECTION from Android developer site. This code accounts for
    	 *  gravity using a high-pass filter
    	 */

        // alpha is calculated as t / (t + dT)
        // with t, the low-pass filter's time-constant
        // and dT, the event delivery rate

        final float alpha = 0.8f;

        // Gravity components of x, y, and z acceleration
        mGravity[X] = alpha * mGravity[X] + (1 - alpha) * event.values[X];
        mGravity[Y] = alpha * mGravity[Y] + (1 - alpha) * event.values[Y];
        mGravity[Z] = alpha * mGravity[Z] + (1 - alpha) * event.values[Z];

        // Linear acceleration along the x, y, and z axes (gravity effects removed)
        mLinearAcceleration[X] = event.values[X] - mGravity[X];
        mLinearAcceleration[Y] = event.values[Y] - mGravity[Y];
        mLinearAcceleration[Z] = event.values[Z] - mGravity[Z];

        /*
         *  END SECTION from Android developer site
         */
    }

    private float getMaxCurrentLinearAcceleration() {
        // Start by setting the value to the x value
        float maxLinearAcceleration = mLinearAcceleration[X];

        // Check if the y value is greater
        if (mLinearAcceleration[Y] > maxLinearAcceleration) {
            maxLinearAcceleration = mLinearAcceleration[Y];
        }

        // Check if the z value is greater
        if (mLinearAcceleration[Z] > maxLinearAcceleration) {
            maxLinearAcceleration = mLinearAcceleration[Z];
        }

        // Return the greatest value
        return maxLinearAcceleration;
    }

    private void resetShakeDetection() {
        moveCount = 0;
        isShaking = false;
        mShakeListener.onShakeEnd();
    }

    /*
     * Definition for OnShakeListener definition. I would normally put this
     * into it's own .java file, but I included it here for quick reference
     * and to make it easier to include this file in our project.
     */
    public interface OnShakeListener {
        void onShakeBegan();
        void onShake();
        void onShakeEnd();
    }
}