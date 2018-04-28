package com.paktalin.receiptanalyzer.activities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
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

    TreeMap<String, Integer> categories, supermarkets;
    SQLiteDatabase db;
    long[] periodsMillisec = {7776000000L, 2592000000L, 1209600000L, 604800000L};
    long currentTime, startingTime;
    float expenses = 0;

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
        calculateDataInPeriod();
        setPieChart();
        setBarChart();
    }

    AdapterView.OnItemSelectedListener periodListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            currentTime = System.currentTimeMillis();
            startingTime = currentTime - periodsMillisec[position];
            calculateDataInPeriod();
            setCategories();
            setPieChart();
            setBarChart();
            String expensesStr = "You spent " + expenses + "€ ";
            ((TextView)findViewById(R.id.expenses)).setText(expensesStr);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    };

    private void calculateDataInPeriod() {
        String selection = COLUMN_SUPERMARKET + ", " + COLUMN_FINAL_PRICE;
        String query = "SELECT " + selection + " FROM " + TABLE_NAME_RECEIPT + " WHERE " + COLUMN_DATE_RECEIPT + " BETWEEN "
                + startingTime + " AND " + currentTime;
        Cursor cursor = db.rawQuery(query, null);

        int finalPriceIndex = cursor.getColumnIndex(COLUMN_FINAL_PRICE);
        int supermarketIndex = cursor.getColumnIndex(COLUMN_SUPERMARKET);

        supermarkets = new TreeMap<>();
        expenses = 0;
        while (cursor.moveToNext()) {
            expenses += cursor.getFloat(finalPriceIndex);
            String key = cursor.getString(supermarketIndex);
            if (supermarkets.containsKey(key))
                supermarkets.put(key, supermarkets.get(key) + 1);
            else
                supermarkets.put(key, 1);
        }
        cursor.close();
    }

    private void setCategories() {
        String query = "SELECT " + COLUMN_CATEGORY + " FROM " + TABLE_NAME_PURCHASE + " WHERE " + COLUMN_DATE_RECEIPT + " BETWEEN "
                + startingTime + " AND " + currentTime;
        Cursor cursor = db.rawQuery(query, null);
        int categoryIndex = cursor.getColumnIndex(COLUMN_CATEGORY);

        categories = new TreeMap<>();
        while (cursor.moveToNext()) {
            String key = cursor.getString(categoryIndex);
            if (categories.containsKey(key))
                categories.put(key, categories.get(key) + 1);
            else
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

        String[] labels = new String[supermarkets.size()];
        int i = 0;
        for (Map.Entry<String, Integer> entry : supermarkets.entrySet()) {
            int value = entry.getValue();
            labels[i] = entry.getKey();
            yValues.add(new BarEntry(i, value));
            i++;
        }

        BarDataSet dataSet = new BarDataSet(yValues, "Supermarkets");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        BarData data = new BarData(dataSet);
        data.setValueFormatter(new IntFormatter());

        barChart.getXAxis().setDrawGridLines(false);
        barChart.getXAxis().setDrawAxisLine(false);

        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.getAxisLeft().setEnabled(false);

        barChart.getAxisRight().setDrawGridLines(false);
        barChart.getAxisLeft().setAxisMinimum(0);
        barChart.getAxisRight().setAxisMinimum(0);

        barChart.getXAxis().setValueFormatter(new LabelFormatter(labels));
        barChart.getXAxis().setGranularity(1);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        barChart.getLegend().setEnabled(false);
        barChart.getDescription().setEnabled(false);

        int height = 120 * supermarkets.size();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
        findViewById(R.id.bar_chart_layout).setLayoutParams(params);

        barChart.setData(data);
    }

    public class LabelFormatter implements IAxisValueFormatter {
        private final String[] mLabels;

        LabelFormatter(String[] labels) {
            mLabels = labels;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mLabels[(int) value];
        }
    }

    public class IntFormatter implements IValueFormatter {
        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return "" + (int)value;
        }
    }
}

