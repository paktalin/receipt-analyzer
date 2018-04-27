package com.paktalin.receiptanalyzer.activities;

import android.content.Context;
import android.content.SharedPreferences;
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

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.paktalin.receiptanalyzer.R;
import com.paktalin.receiptanalyzer.data.DatabaseHelper;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import static com.paktalin.receiptanalyzer.data.Contracts.ReceiptEntry.*;
import static com.paktalin.receiptanalyzer.data.Contracts.PurchaseEntry.*;

/**
 * Created by Paktalin on 26/04/2018.
 */

public class OverviewActivity extends AppCompatActivity{
    private static final String TAG = ViewReceiptActivity.class.getSimpleName();

    TreeMap<String, Integer> categories;
    SQLiteDatabase db;

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
        setPieChart();
    }

    AdapterView.OnItemSelectedListener periodListener = new AdapterView.OnItemSelectedListener() {
        long[] periods = {7776000000L, 2592000000L, 1209600000L, 604800000L};

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
        while (cursor.moveToNext())
            expenses += cursor.getFloat(finalPriceIndex);
        cursor.close();
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

    private void setPieChart() {
        PieChart pieChart = findViewById(R.id.pie_chart);
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

    private void setBarChart() {
        HorizontalBarChart barChart = findViewById(R.id.bar_chart);

        ArrayList<BarEntry> yValues = new ArrayList<>();
        SharedPreferences appData = getSharedPreferences("app_data", Context.MODE_PRIVATE);
        int i = 0;
        for (Map.Entry<String, ?> entry : appData.getAll().entrySet()) {
            String key = entry.getKey();
            float value = (float)(Integer)entry.getValue();
            yValues.add(new BarEntry(value, i));
            Log.d(TAG, "value: " + value + "; i = " + i);
            i++;
        }

        BarDataSet dataSet = new BarDataSet(yValues, "Supermarkets");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        BarData data = new BarData(dataSet);
        barChart.setData(data);
    }
}
