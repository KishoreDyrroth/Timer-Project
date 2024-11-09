// MainActivity.java
package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.media.MediaPlayer;  // For playing the notification sound
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.util.Log;  // For logging

public class MainActivity extends AppCompatActivity {
    private TextView timerDisplay;
    private EditText hoursInput, minutesInput, secondsInput;
    private Button startButton, pauseButton, resetButton, soundSettingsButton, viewHistoryButton;
    private CountDownTimer countDownTimer;
    private boolean isRunning = false;
    private long timeLeftInMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        timerDisplay = findViewById(R.id.timerDisplay);
        hoursInput = findViewById(R.id.hours);
        minutesInput = findViewById(R.id.minutes);
        secondsInput = findViewById(R.id.seconds);
        startButton = findViewById(R.id.startButton);
        pauseButton = findViewById(R.id.pauseButton);
        resetButton = findViewById(R.id.resetButton);
        soundSettingsButton = findViewById(R.id.soundSettingsButton);  // Sound Settings Button
        viewHistoryButton = findViewById(R.id.viewHistoryButton); // Timer History Button

        // Set OnClickListener for the Sound Settings Button
        soundSettingsButton.setOnClickListener(view -> {
            // Navigate to SoundSettingsActivity when the button is clicked
            Intent intent = new Intent(MainActivity.this, SoundSettingsActivity.class);
            startActivity(intent);
        });

        // Set OnClickListener for the "See Timer History" Button
        viewHistoryButton.setOnClickListener(view -> openTimerHistory());

        startButton.setOnClickListener(view -> startTimer());
        pauseButton.setOnClickListener(view -> pauseTimer());
        resetButton.setOnClickListener(view -> resetTimer());
    }

    private void startTimer() {
        if (!isRunning) {
            long hours = Integer.parseInt(hoursInput.getText().toString()) * 3600 * 1000;
            long minutes = Integer.parseInt(minutesInput.getText().toString()) * 60 * 1000;
            long seconds = Integer.parseInt(secondsInput.getText().toString()) * 1000;
            timeLeftInMillis = hours + minutes + seconds;

            countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    timeLeftInMillis = millisUntilFinished;
                    updateTimer();
                }

                @Override
                public void onFinish() {
                    try {
                        // Show a "Time's up!" message
                        Toast.makeText(MainActivity.this, "Time's up!", Toast.LENGTH_SHORT).show();

                        // Play a notification sound
                        playNotificationSound();

                        // Save the completed timer to history
                        saveTimerHistory();

                        isRunning = false;
                        // Don't close the app, just reset the UI or keep it active
                    } catch (Exception e) {
                        // Log the error and handle gracefully
                        Log.e("MainActivity", "Error in onFinish", e);
                        Toast.makeText(MainActivity.this, "An error occurred.", Toast.LENGTH_SHORT).show();
                    }
                }
            }.start();

            isRunning = true;
        }
    }

    private void pauseTimer() {
        if (isRunning) {
            countDownTimer.cancel();
            isRunning = false;
        }
    }

    private void resetTimer() {
        if (isRunning) {
            countDownTimer.cancel();
        }
        timeLeftInMillis = 0;
        updateTimer();
    }

    private void updateTimer() {
        int hours = (int) (timeLeftInMillis / 3600000);
        int minutes = (int) (timeLeftInMillis % 3600000) / 60000;
        int seconds = (int) (timeLeftInMillis % 60000) / 1000;

        String timeLeftFormatted = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        timerDisplay.setText(timeLeftFormatted);
    }

    private void playNotificationSound() {
        // Play a notification sound from res/raw folder
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.sound2); // Replace with your sound file name
        mediaPlayer.start();
    }

    private void saveTimerHistory() {
        SharedPreferences sharedPreferences = getSharedPreferences("TimerHistoryPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        int historyCount = sharedPreferences.getInt("historyCount", 0);
        historyCount++; // Increment history count

        // Save the timer details
        editor.putInt("historyCount", historyCount);
        editor.putString("timer_duration_" + historyCount, timerDisplay.getText().toString());
        editor.putString("timer_start_time_" + historyCount, String.format("%02d:%02d:%02d", hoursInput, minutesInput, secondsInput));
        editor.putString("timer_end_time_" + historyCount, timerDisplay.getText().toString());
        editor.apply(); // Apply changes
    }

    // Open the Timer History Activity to display the saved timer history
    private void openTimerHistory() {
        Intent intent = new Intent(MainActivity.this, TimerHistoryActivity.class);
        startActivity(intent);
    }
}
