// SoundSettingsActivity.java
package com.example.myapplication;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SoundSettingsActivity extends AppCompatActivity {
    private Spinner soundOptionsSpinner;
    private Button previewButton, saveButton;
    private MediaPlayer mediaPlayer; // MediaPlayer instance to play sound

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_settings);

        // Initialize the Spinner and Buttons
        soundOptionsSpinner = findViewById(R.id.soundOptionsSpinner);
        previewButton = findViewById(R.id.previewButton);
        saveButton = findViewById(R.id.saveButton);

        // Populate the Spinner with sound options from strings.xml
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sound_options, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the Spinner
        soundOptionsSpinner.setAdapter(adapter);

        // Set an OnClickListener for the "Preview Sound" button
        previewButton.setOnClickListener(view -> {
            String selectedSound = soundOptionsSpinner.getSelectedItem().toString();
            playSound(selectedSound); // Call the method to play the selected sound
        });

        // Set an OnClickListener for the "Save Sound" button
        saveButton.setOnClickListener(view -> {
            String selectedSound = soundOptionsSpinner.getSelectedItem().toString();
            saveSelectedSound(selectedSound); // Save the selected sound in SharedPreferences
        });
    }

    private void playSound(String selectedSound) {
        // Stop any previously playing sound
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        // Play sound based on the selected option
        if ("Sound 1".equals(selectedSound)) {
            mediaPlayer = MediaPlayer.create(this, R.raw.sound1); // sound1.mp3 in the raw folder
        } else if ("Sound 2".equals(selectedSound)) {
            mediaPlayer = MediaPlayer.create(this, R.raw.sound2); // sound2.mp3 in the raw folder
        } else if ("Sound 3".equals(selectedSound)) {
            mediaPlayer = MediaPlayer.create(this, R.raw.sound3); // sound3.mp3 in the raw folder
        }

        // Start playing the sound
        if (mediaPlayer != null) {
            mediaPlayer.start();
        } else {
            // If the mediaPlayer is null, show an error
            Toast.makeText(this, "Unable to play sound", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveSelectedSound(String selectedSound) {
        // Save the selected sound in SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("selectedSound", selectedSound); // Save the selected sound
        editor.apply(); // Apply changes

        // Show a confirmation toast
        Toast.makeText(this, "Sound setting saved", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release the MediaPlayer when the activity is destroyed
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }
}
