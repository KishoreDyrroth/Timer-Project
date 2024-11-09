// TimerHistoryActivity.java
package com.example.myapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class TimerHistoryActivity extends AppCompatActivity {

    private ListView historyListView;
    private ArrayList<String> timerHistoryList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_history);

        historyListView = findViewById(R.id.historyListView);

        // Initialize the list to hold history items
        timerHistoryList = new ArrayList<>();

        // Load timer history from SharedPreferences
        loadTimerHistory();

        // Set up the adapter for displaying the history in the ListView
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, timerHistoryList);
        historyListView.setAdapter(adapter);
    }

    // Method to load timer history from SharedPreferences
    private void loadTimerHistory() {
        SharedPreferences sharedPreferences = getSharedPreferences("TimerHistoryPreferences", MODE_PRIVATE);
        int historyCount = sharedPreferences.getInt("historyCount", 0);  // Get the number of history items

        if (historyCount > 0) {
            // Load each history entry
            for (int i = 1; i <= historyCount; i++) {
                String duration = sharedPreferences.getString("timer_duration_" + i, "No data");
                String startTime = sharedPreferences.getString("timer_start_time_" + i, "No data");
                String endTime = sharedPreferences.getString("timer_end_time_" + i, "No data");

                // Format the history item as a string
                String historyItem = "Duration: " + duration + "\nStart: " + startTime + "\nEnd: " + endTime;

                // Add it to the list
                timerHistoryList.add(historyItem);
            }
        } else {
            // If no history is found, show a message
            Toast.makeText(this, "No timer history found", Toast.LENGTH_SHORT).show();
        }
    }
}
