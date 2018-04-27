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

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.paktalin.receiptanalyzer.R;
import com.paktalin.receiptanalyzer.data.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static com.paktalin.receiptanalyzer.data.Contracts.ReceiptEntry.*;
import static com.paktalin.receiptanalyzer.data.Contracts.PurchaseEntry.*;

/**
 * Created by Paktalin on 26/04/2018.
 */

public class OverviewActivity extends AppCompatActivity{
    private static final String TAG = ViewReceiptActivity.class.getSimpleName();

    long[] periods = {7776000000L, 2592000000L, 1209600000L, 604800000L};
    TreeMap<String, Integer> categories;
    SQLiteDatabase db;

    PieChart pieChart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        DatabaseHelper helper = new DatabaseHelper(OverviewActivity.this);
        db = helper.getReadableDatabase();

        Spinner spinner = findViewById(R.id.spinner);
        spinner.setSelection(1);
        spinner.setOnItemSelectedListener(periodListener);

        setCategories();

        pieChart = findViewById(R.id.chart);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);

        ArrayList<PieEntry> yValues = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : categories.entrySet()) {
            String key = entry.getKey();
            float value = (float)entry.getValue();
            yValues.add(new PieEntry(value, key));
        }

        PieDataSet dataSet = new PieDataSet(yValues, "Categories");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData data = new PieData(dataSet);

        pieChart.setData(data);

    }

    AdapterView.OnItemSelectedListener periodListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String expenses = "You spent " + setExpenses(periods[position]) + "€ ";
            ((TextView)findViewById(R.id.expenses)).setText(expenses);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    };

    private float setExpenses(long period) {
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

    private void setCategories() {
        categories = new TreeMap<>();

        Cursor cursor = db.query(TABLE_NAME_PURCHASE,
                new String[] {COLUMN_CATEGORY},
                null, null, null, null, null);

        int categoryIndex = cursor.getColumnIndex(COLUMN_CATEGORY);

        while (cursor.moveToNext()) {
            String key = cursor.getString(categoryIndex);
            if (key == null)
                key = "Unknown";
            if (categories.containsKey(key)) {
                int currentValue = categories.get(key);
                categories.put(key, currentValue + 1);
            } else
                categories.put(key, 1);
        }
        cursor.close();
    }
}
