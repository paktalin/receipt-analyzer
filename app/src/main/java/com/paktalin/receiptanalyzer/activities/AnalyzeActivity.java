package com.paktalin.receiptanalyzer.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.paktalin.receiptanalyzer.R;

import static com.paktalin.receiptanalyzer.DataKeeper.*;

/**
 * Created by Paktalin on 12/04/2018.
 */

public class AnalyzeActivity extends AppCompatActivity{


    private SharedPreferences mAppData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyze);
        TextView textView = findViewById(R.id.text_view);

        mAppData = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    }
}
