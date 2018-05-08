package com.paktalin.receiptanalyzer;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.paktalin.receiptanalyzer.data.DatabaseHelper;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import static com.paktalin.receiptanalyzer.data.Contracts.PurchaseEntry.COLUMN_CATEGORY;
import static com.paktalin.receiptanalyzer.data.Contracts.PurchaseEntry.COLUMN_PRICE;
import static com.paktalin.receiptanalyzer.data.Contracts.PurchaseEntry.TABLE_NAME_PURCHASE;
import static com.paktalin.receiptanalyzer.data.Contracts.ReceiptEntry.COLUMN_DATE_RECEIPT;
import static com.paktalin.receiptanalyzer.data.Contracts.ReceiptEntry.COLUMN_FINAL_PRICE;
import static com.paktalin.receiptanalyzer.data.Contracts.ReceiptEntry.COLUMN_SUPERMARKET;
import static com.paktalin.receiptanalyzer.data.Contracts.ReceiptEntry.TABLE_NAME_RECEIPT;

/**
 * Created by Paktalin on 07/05/2018.
 */

public class ChartManager {
    private static final String TAG = ChartManager.class.getSimpleName();

    private TreeMap<String, Float> supermarkets, categories;
    private SQLiteDatabase db;
    private long to, from;

    public void retrieveData (Context context, long period) {
        DatabaseHelper helper = new DatabaseHelper(context);
        db = helper.getReadableDatabase();
        to = System.currentTimeMillis();
        from = to - period;

        retrieveSupermarkets();
        retrieveCategories();
    }

    private void retrieveSupermarkets() {
        supermarkets = new TreeMap<>();
        String selection = COLUMN_SUPERMARKET + ", " + COLUMN_FINAL_PRICE;
        String query = "SELECT " + selection + " FROM " + TABLE_NAME_RECEIPT + " WHERE " + COLUMN_DATE_RECEIPT + " BETWEEN "
                + from + " AND " + to;
        Cursor cursor = db.rawQuery(query, null);

        int finalPriceIndex = cursor.getColumnIndex(COLUMN_FINAL_PRICE);
        int supermarketIndex = cursor.getColumnIndex(COLUMN_SUPERMARKET);

        while (cursor.moveToNext()) {
            String key = cursor.getString(supermarketIndex);
            if (supermarkets.containsKey(key))
                supermarkets.put(key, supermarkets.get(key) + cursor.getFloat(finalPriceIndex));
            else
                supermarkets.put(key, cursor.getFloat(finalPriceIndex));
        }
        cursor.close();
    }

    private void retrieveCategories() {
        categories = new TreeMap<>();
        String selection = COLUMN_CATEGORY + ", " + COLUMN_PRICE;
        String query = "SELECT " + selection + " FROM " + TABLE_NAME_PURCHASE + " WHERE " + COLUMN_DATE_RECEIPT + " BETWEEN "
                + from + " AND " + to;
        Cursor cursor = db.rawQuery(query, null);

        int categoryIndex = cursor.getColumnIndex(COLUMN_CATEGORY);
        int priceIndex = cursor.getColumnIndex(COLUMN_PRICE);

        while (cursor.moveToNext()) {
            String key = cursor.getString(categoryIndex);
            if (categories.containsKey(key))
                categories.put(key, categories.get(key) + cursor.getFloat(priceIndex));
            else
                categories.put(key, cursor.getFloat(priceIndex));
        }
        cursor.close();
    }

    public HorizontalBarChart setSupermarketsChart(HorizontalBarChart barChart) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        float[] values = new float[supermarkets.size()];
        String[] labels = new String[supermarkets.size()];
        int i = 0;
        for (Map.Entry entry : supermarkets.entrySet()) {
            values[i] = (float)entry.getValue();
            labels[i] = (String) entry.getKey();
            i++;
        }
        entries.add(new BarEntry(0, values));

        BarDataSet dataSet = new BarDataSet(entries, "");
        dataSet.setStackLabels(labels);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setDrawValues(false);
        BarData data = new BarData(dataSet);
        barChart.setData(data);
        barChart.getDescription().setEnabled(false);

        barChart.getAxisLeft().setDrawAxisLine(false);
        barChart.getAxisLeft().setValueFormatter(new YAxisFormatter());
        barChart.getAxisLeft().setAxisMinimum(1);

        barChart.getAxisRight().setEnabled(false);
        barChart.getXAxis().setEnabled(false);
        barChart.setScaleEnabled(false);
        return barChart;
    }

    static class YAxisFormatter implements IAxisValueFormatter{
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return String.format("%.0f", value) + " €";
        }
    }

    public PieChart setPieChart(PieChart pieChart) {
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);

        ArrayList<PieEntry> yValues = new ArrayList<>();
        for (Map.Entry<String, Float> entry : categories.entrySet()) {
            String key = entry.getKey();
            float value = entry.getValue();
            yValues.add(new PieEntry(value, key));
        }
        PieDataSet dataSet = new PieDataSet(yValues, "");
        dataSet.setDrawValues(false);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData data = new PieData(dataSet);
        pieChart.setDrawEntryLabels(false);
        pieChart.setData(data);
        return pieChart;
    }
}
