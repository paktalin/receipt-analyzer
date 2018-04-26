package com.paktalin.receiptanalyzer.activities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
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

        Spinner spinner = findViewById(R.id.spinner);
        spinner.setSelection(1);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String expenses;
                switch (position) {
                    case 0:
                        expenses = "You spent " + getExpenses(7776000000L) + "€ ";
                        break;
                    case 2:
                        expenses = "You spent " + getExpenses(1209600000L) + "€ ";
                        break;
                    case 3:
                        expenses = "You spent " + getExpenses(604800000L) + "€ ";
                        break;
                    default:
                        expenses = "You spent " + getExpenses(2592000000L) + "€ ";
                        break;
                }

                ((TextView)findViewById(R.id.expenses)).setText(expenses);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        String expenses = "You spent " + getExpenses(0) + "€ in the last 30 days";
        ((TextView)findViewById(R.id.expenses)).setText(expenses);
    }

    private float getExpenses(long period) {
        DatabaseHelper helper = new DatabaseHelper(OverviewActivity.this);
        SQLiteDatabase db = helper.getReadableDatabase();

        long currentTime = System.currentTimeMillis();
        long startingTime = currentTime - period;

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
