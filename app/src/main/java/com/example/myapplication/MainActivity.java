// MainActivity.java
package com.example.myapplication;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private TextView timerDisplay;
    private EditText hoursInput, minutesInput, secondsInput;
    private Button startButton, pauseButton, resetButton;
    private CountDownTimer countDownTimer;
    private boolean isRunning = false;
    private long timeLeftInMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerDisplay = findViewById(R.id.timerDisplay);
        hoursInput = findViewById(R.id.hours);
        minutesInput = findViewById(R.id.minutes);
        secondsInput = findViewById(R.id.seconds);
        startButton = findViewById(R.id.startButton);
        pauseButton = findViewById(R.id.pauseButton);
        resetButton = findViewById(R.id.resetButton);

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
                    timerDisplay.setText("Time's up!");
                    isRunning = false;
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
}
