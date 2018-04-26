package com.paktalin.receiptanalyzer.activities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.paktalin.receiptanalyzer.R;
import com.paktalin.receiptanalyzer.data.DatabaseHelper;

import static com.paktalin.receiptanalyzer.data.Contracts.ReceiptEntry.*;


/**
 * Created by Paktalin on 26/04/2018.
 */

public class OverviewActivity extends AppCompatActivity{
    private static final String TAG = ViewReceiptActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        ((TextView)findViewById(R.id.expenses)).setText(String.valueOf(getExpenses()));
    }

    private float getExpenses() {
        DatabaseHelper helper = new DatabaseHelper(OverviewActivity.this);
        SQLiteDatabase db = helper.getReadableDatabase();

        long currentTime = System.currentTimeMillis();
        long month = 2592000000L;
        long startingTime = currentTime - month;

        String selection = COLUMN_DATE + ", " + COLUMN_FINAL_PRICE;
        String query = "SELECT " + selection + " FROM " + TABLE_NAME_RECEIPT + " WHERE " + COLUMN_DATE + " BETWEEN "
                + startingTime + " AND " + currentTime;

        Cursor cursor = db.rawQuery(query, null);

        int finalPriceIndex = cursor.getColumnIndex(COLUMN_FINAL_PRICE);

        float expenses = 0;
        while (cursor.moveToNext()) {
            expenses += cursor.getFloat(finalPriceIndex);
        }
        return expenses;
    }
}
