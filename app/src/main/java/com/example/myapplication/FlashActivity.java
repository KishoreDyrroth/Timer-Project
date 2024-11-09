package com.example.myapplication;

// FlashActivity.java
public class FlashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash);

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(FlashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }, 2000); // 2 seconds delay
    }
}

