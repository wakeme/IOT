package com.wakeme.shakeme;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView m_text;
    private ImageView m_image;

    private SensorManager m_sm;
    private Sensor m_accelerometer;
    private Sensor m_proximity;

    private ShakeDetector m_sd;
    private HugDetector m_hd;

    int shakeCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m_text = (TextView) findViewById(R.id.textView);
        m_image = (ImageView) findViewById(R.id.imageView);

        m_sm = (SensorManager)this.getSystemService(Context.SENSOR_SERVICE);

        shakeDetector();
        hugDetector();
    }

    @Override
    public void onResume() {
        super.onResume();
        m_sm.registerListener(m_sd, m_accelerometer, SensorManager.SENSOR_DELAY_UI);
        m_sm.registerListener(m_hd, m_proximity, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        super.onPause();
        m_sm.unregisterListener(m_sd);
    }

    private void shakeDetector() {
        m_sd = new ShakeDetector(new ShakeDetector.OnShakeListener() {
            @Override
            public void onShakeBegan() {
                shakeCount++;
                System.out.println("ShakeBegan");
            }

            @Override
            public void onShake() {
                System.out.println("OnShake: " + shakeCount);
                m_text.setText("Shaking..._(눈_눈」∠)_");
                m_image.setImageResource(R.mipmap.face_3_1);
            }

            @Override
            public void onShakeEnd() {
                System.out.println("ShakeEnd");
                doWithShake();
            }
        });
        m_accelerometer = m_sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        m_sm.registerListener(m_sd, m_accelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    private void hugDetector() {
        m_hd = new HugDetector(new HugDetector.OnHugListener() {
            @Override
            public void onHug() {
                System.out.println("OnHug");
                shakeCount = 0;
                m_image.setImageResource(R.mipmap.face_0_0);
                m_text.setText("Come on! Shake me! ");
            }
        });
        m_proximity = m_sm.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        m_sm.registerListener(m_hd, m_proximity, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void doWithShake() {
        switch (shakeCount) {
            case 0:
                m_image.setImageResource(R.mipmap.face_0_0);
                m_text.setText("Come on. Shake Me! ");
                break;
            case 1:
                m_image.setImageResource(R.mipmap.face_1_1);
                m_text.setText("Dude, you are so weak, LOL.");
                break;
            case 2:
                m_image.setImageResource(R.mipmap.face_2_1);
                m_text.setText("Just a little bit violent...");
                break;
            case 3:
                m_image.setImageResource(R.mipmap.face_3_1);
                m_text.setText("Uh, what's going on.");
                break;
            case 4:
                m_image.setImageResource(R.mipmap.face_4_1);
                m_text.setText("Stop, ToT, that's enough.");
                break;
            default:
                m_image.setImageResource(R.mipmap.face_4_1);
                m_text.setText("Hug me to start again. ");
                break;
        }
    }

}
