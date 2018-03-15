package com.paktalin.receiptanalyzer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_GET_PICTURE = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.button);
        FileManager.setUpAppDir(MainActivity.this);

        button.setOnClickListener(view -> {
            Intent editIntent = new Intent(MainActivity.this, EditActivity.class);
            startActivityForResult(editIntent, REQUEST_GET_PICTURE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_GET_PICTURE && resultCode == RESULT_OK) {
            Recognizer.recognize(MainActivity.this);
        }
    }
}