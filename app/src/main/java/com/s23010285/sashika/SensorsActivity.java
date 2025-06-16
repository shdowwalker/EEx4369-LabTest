package com.s23010285.sashika;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class SensorsActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor tempSensor;
    private MediaPlayer mediaPlayer;
    private TextView tempValueText;
    private final float TEMP_THRESHOLD = 85.0f;
    private boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensors);

        tempValueText = findViewById(R.id.tempValue);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        if (sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE) != null) {
            tempSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        } else {
            tempValueText.setText("Temperature Sensor Not Available");
        }

        // Place your audio file in res/raw/alert_sound.mp3
        mediaPlayer = MediaPlayer.create(this, R.raw.alert_sound);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            float currentTemp = event.values[0];
            tempValueText.setText(String.format("Current Temperature: %.1f°C", currentTemp));

            if (currentTemp > TEMP_THRESHOLD && !isPlaying) {
                mediaPlayer.start();
                isPlaying = true;
                Toast.makeText(this, "Temperature exceeded threshold!", Toast.LENGTH_SHORT).show();
            } else if (currentTemp <= TEMP_THRESHOLD && isPlaying) {
                mediaPlayer.pause();
                isPlaying = false;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }

    @Override
    protected void onResume() {
        super.onResume();
        if (tempSensor != null)
            sensorManager.registerListener(this, tempSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }
}
